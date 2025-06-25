package hudson.plugins.tics;

import com.cloudbees.plugins.credentials.CredentialsMatcher;
import com.cloudbees.plugins.credentials.CredentialsMatchers;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.common.StandardCredentials;
import com.cloudbees.plugins.credentials.common.StandardListBoxModel;
import com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials;
import com.cloudbees.plugins.credentials.domains.DomainRequirement;
import com.cloudbees.plugins.credentials.domains.URIRequirementBuilder;
import com.google.common.base.MoreObjects;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import hudson.EnvVars;
import hudson.Util;
import hudson.model.Item;
import hudson.model.Job;
import hudson.security.ACL;
import hudson.util.ListBoxModel;
import hudson.util.Secret;
import jenkins.model.Jenkins;
import org.apache.commons.lang3.tuple.Pair;
import org.jenkinsci.plugins.plaincredentials.StringCredentials;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.QueryParameter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class AuthHelper {
    private AuthHelper() {
    }

    public static final String TICSAUTHTOKEN = "TICSAUTHTOKEN";

    /**
     * Find credentials by id using the Credentials plugin.
     */
    private static <T extends StandardCredentials> Optional<T> lookupCredentials(final Class<T> clazz, final Job<?, ?> job, final String credentialsId) {
        if (Strings.isNullOrEmpty(credentialsId)) {
            return Optional.empty();
        }
        final List<DomainRequirement> domainRequirements = Collections.emptyList();
        final List<T> list = CredentialsProvider.lookupCredentialsInItem(clazz, job, ACL.SYSTEM2, domainRequirements);

        for (final T c : list) {
            if (credentialsId.equals(c.getId())) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    public static Optional<Pair<String, String>> lookupUsernameAndPasswordFromCredentialsId(final Job<?, ?> job, final String credentialsId, final EnvVars buildEnv) {
        final Optional<Pair<String, String>> credentialsPair = lookupCredentials(StandardUsernamePasswordCredentials.class, job, credentialsId)
                .map(credentials -> Pair.of(credentials.getUsername(), Secret.toString(credentials.getPassword())));
        if (credentialsPair.isPresent()) {
            return credentialsPair;
        } else {
            return AuthHelper.lookupTicsAuthToken(job, credentialsId, buildEnv)
                    .map(AuthHelper::decodeTokenToUsernamePassword);
        }
    }

    /**
     * Looks for the TICSAUTHTOKEN in the Credentials plugin for given credentialsId, or the or the provided {@link EnvVars}
     * and returns its value.
     * <b>Warning</b> If the provided variables contains
     **/
    public static Optional<String> lookupTicsAuthToken(final Job<?, ?> job, final String credentialsId, final EnvVars buildEnv) {
        final String errorMessage = "The provided credentials are of the wrong type. Only two types are supported; username & password and secret text.";
        // TICSAUTHTOKEN can be set through the credentials dropdown available within our plugin
        // In this case, the credentialsId is present
        if (!Strings.isNullOrEmpty(credentialsId)) {
            return AuthHelper.lookupCredentials(StringCredentials.class, job, credentialsId)
                    .map(c -> Optional.of(c.getSecret().getPlainText()))
                    .orElseThrow(() -> new IllegalArgumentException(errorMessage));
        } else {
            // TICSAUTHTOKEN can also be set as a secret or as a parameter.
            // In both cases, an environment variable is created.
            // If TICSAUTHTOKEN is set as a credentials parameter then the saved format of the environment variable is TICSAUTHTOKEN=credentialsId
            // For all the other cases, the environment variable is of the format TICSAUTHTOKEN=value
            final String ticsTokenEnv = buildEnv.get(TICSAUTHTOKEN);
            if (!Strings.isNullOrEmpty(ticsTokenEnv)) {
                return AuthHelper.lookupCredentials(StringCredentials.class, job, ticsTokenEnv)
                        .map(c -> c.getSecret().getPlainText()).or(() -> Optional.of(ticsTokenEnv));
            }
        }

        return Optional.empty();
    }

    private static Pair<String, String> decodeTokenToUsernamePassword(final String token) {
        try {
            final byte[] decodedToken = java.util.Base64.getDecoder().decode(token);
            final String decodedTokenStr = new String(decodedToken, StandardCharsets.UTF_8);
            final String[] splitted = decodedTokenStr.split(":");
            if (splitted.length != 2) {
                throw new IllegalStateException("Unexpected number of parts");
            }
            final Pair<String, String> splittedPair = Pair.of(splitted[0], splitted[1]);
            return splittedPair;
        } catch (final Exception ex) {
            throw new IllegalArgumentException("Malformed authentication token. Please make sure you are using a valid token from the TICS Viewer.", ex);
        }
    }

    public static Map<String, String> getPluginEnvMap(final EnvVars buildEnv, final String environmentVariables) {
        final Map<String, String> out = Maps.newLinkedHashMap();
        final ImmutableList<String> lines = ImmutableList.copyOf(Splitter.onPattern("\r?\n").split(MoreObjects.firstNonNull(environmentVariables, "")));
        for (final String line : lines) {
            final ArrayList<String> splitted = Lists.newArrayList(Splitter.on("=").limit(2).split(line));
            if (splitted.size() == 2) {
                out.put(splitted.get(0).trim(), Util.replaceMacro(splitted.get(1).trim(), buildEnv));
            }
        }

        return out;
    }

    /**
     * Called by Jenkins to fill credentials dropdown list
     */
    public static ListBoxModel fillCredentialsDropdown(@AncestorInPath final Item context, @QueryParameter final String credentialsId) {
        final List<DomainRequirement> domainRequirements;
        final CredentialsMatcher credentialsMatcher = CredentialsMatchers.anyOf(CredentialsMatchers.instanceOf(StandardCredentials.class));
        final StandardListBoxModel result = new StandardListBoxModel();
        if (context == null) {
            if (!Jenkins.get().hasPermission(Jenkins.ADMINISTER)) {
                return result.includeCurrentValue(credentialsId);
            }
        } else {
            if (!context.hasPermission(Item.CONFIGURE)) {
                return result.includeCurrentValue(credentialsId);
            }
        }
        if (credentialsId == null) {
            domainRequirements = Collections.emptyList();
        } else {
            domainRequirements = URIRequirementBuilder.fromUri(credentialsId.trim()).build();
        }
        return result
                .includeEmptyValue()
                .includeMatchingAs(ACL.SYSTEM2, context, StandardCredentials.class, domainRequirements, credentialsMatcher);
    }
}
