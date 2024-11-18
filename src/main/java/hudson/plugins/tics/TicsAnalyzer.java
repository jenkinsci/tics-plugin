package hudson.plugins.tics;

import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.client.utils.URIBuilder;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.verb.POST;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.Launcher.ProcStarter;
import hudson.Util;
import hudson.model.AbstractProject;
import hudson.model.Item;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.plugins.tics.TicsPublisher.InvalidTicsViewerUrl;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import jenkins.tasks.SimpleBuildStep;
import net.sf.json.JSONObject;

public class TicsAnalyzer extends Builder implements SimpleBuildStep {
    static final String LOGGING_PREFIX = "[TICS Analyzer] ";

    // DO NOT RENAME THESE FIELDS, as they are serialized (by Jenkins) in jobs/<project>/config.xml
    public final String ticsPath;
    public final String ticsConfiguration;
    public final String projectName;
    public final String branchName;
    public final String branchDirectory;
    public final String environmentVariables;
    public final boolean createTmpdir;
    public final String tmpdir;
    public final String extraArguments;
    public final Metrics calc;
    public final Metrics recalc;
    public final boolean installTics;
    public final String credentialsId;

    /**
     * This annotation tells Hudson to call this constructor, with values from the configuration form page with matching parameter names.
     * See <a href="https://wiki.jenkins-ci.org/display/JENKINS/Basic+guide+to+Jelly+usage+in+Jenkins">...</a> for explanation of DataBoundConstructor
     * DO NOT RENAME THESE PARAMETERS, as they are serialized (by Jenkins) in jobs/[PROJECT]/config.xml
     */
    @DataBoundConstructor
    public TicsAnalyzer(
            final String ticsPath
            , final String ticsConfiguration
            , final String projectName
            , final String branchName
            , final String branchDirectory
            , final String environmentVariables
            , final boolean createTmpdir
            , final String tmpdir
            , final String extraArguments
            , final Metrics calc
            , final Metrics recalc
            , final boolean installTics
            , final String credentialsId
            ) {
        this.ticsPath = ticsPath;
        this.ticsConfiguration = ticsConfiguration;
        this.projectName = projectName;
        this.branchName = branchName;
        this.branchDirectory = branchDirectory;
        this.environmentVariables = environmentVariables;
        this.createTmpdir = createTmpdir;
        this.tmpdir = tmpdir;
        this.extraArguments = extraArguments;
        this.calc = calc == null ? new Metrics() : calc;
        this.recalc = recalc == null ? new Metrics() : recalc;
        this.installTics = installTics;
        this.credentialsId = credentialsId;
    }

    @Override
    public void perform(@Nonnull final Run<?, ?> run, @Nonnull final FilePath workspace, @Nonnull final EnvVars envvars, @Nonnull final Launcher launcher, @Nonnull final TaskListener listener) throws IOException, InterruptedException {
        final String errorPrefix = "TICS Analysis failed with exit code: ";
        final PrintStream logger = listener.getLogger();
        try {
            final EnvVars buildEnv = run.getEnvironment(listener);
            String installTicsApiFullUrl = "";
            final int exitCode;

            if (installTics) {
                final String tiobeWebBaseUrl;
                final String ticsInstallApiBaseUrl;

                try {
                    tiobeWebBaseUrl = ValidationHelper.getTiobewebBaseUrlFromGivenUrl(ticsConfiguration);
                    ticsInstallApiBaseUrl = getInstallTicsApiUrl(tiobeWebBaseUrl, getNodeOs(launcher));
                } catch (final InvalidTicsViewerUrl | URISyntaxException ex) {
                    ex.printStackTrace(listener.getLogger());
                    throw new IllegalArgumentException(LOGGING_PREFIX + "Invalid TICS Viewer URL", ex);
                }

                final EnvVars env2 = new EnvVars(buildEnv);
                env2.putAll(AuthHelper.getPluginEnvMap(buildEnv, environmentVariables));
                final Optional<Pair<String, String>> usernameAndPassword = AuthHelper.lookupUsernameAndPasswordFromCredentialsId(run.getParent(), credentialsId, env2);

                final InstallTicsApiCall installTicsApiCall = new InstallTicsApiCall(ticsInstallApiBaseUrl, usernameAndPassword, listener);
                final String installTicsApiData = installTicsApiCall.retrieveInstallTics();
                installTicsApiFullUrl = tiobeWebBaseUrl + installTicsApiData;
            }

            exitCode = launchTicsQServer(installTicsApiFullUrl, run, launcher, listener, buildEnv);
            if (exitCode != 0) {
                logger.println(LOGGING_PREFIX + "Exit code " + exitCode);
                throw new RuntimeException(LOGGING_PREFIX + errorPrefix + exitCode);
            }
        } catch (final IOException e) {
            logger.println(LOGGING_PREFIX + e.getMessage());
            throw e;
        }
    }

    /** Prefixes given command with location of TICS, if available */
    private String getFullyQualifiedPath(final String command) {
        String path = MoreObjects.firstNonNull(ticsPath, "").trim();
        if (path.isEmpty() || installTics) {
            return command;
        }
        // Note: we do not use new File(), because we do not want use the local FileSystem
        if (!path.endsWith("/") && !path.endsWith("\\")) {
            path += "/";
        }
        return path + command;
    }


    int launchTicsQServer(final String url, final Run<?, ?> run, final Launcher launcher, final TaskListener listener, final EnvVars buildEnv) throws IOException, InterruptedException {
        final boolean isLauncherUnix = launcher.isUnix();

        final String bootstrapCommand =  installTics ? getBootstrapCmd(url, isLauncherUnix) : "";
        final ImmutableList<String> ticsAnalysisCommand = getTicsQServerArgs(buildEnv, isLauncherUnix);

        final String command = createCommand(bootstrapCommand, ticsAnalysisCommand, isLauncherUnix);
        final ProcStarter starter = launcher.new ProcStarter().stdout(listener).cmdAsSingleString(command)
                .envs(getEnvMap(buildEnv, run));

        return launcher.launch(starter).join();
    }


    protected ImmutableList<String> getTicsQServerArgs(final EnvVars buildEnv, final boolean isLauncherUnix) {
        final ImmutableList.Builder<String> args = ImmutableList.builder();
        final String ticsQServer = "TICSQServer" + (isLauncherUnix ? "" : ".exe");

        args.add(getFullyQualifiedPath(ticsQServer));

        if (isNotEmpty(projectName)) {
            args.add("-project");
            args.add(escapeValue(Util.replaceMacro(projectName, buildEnv)));
        }
        if (isNotEmpty(branchName)) {
            args.add("-branchname");
            args.add(escapeValue(Util.replaceMacro(branchName, buildEnv)));
        }

        if (!Strings.isNullOrEmpty(branchDirectory)) {
            args.add("-branchdir");
            args.add(escapeValue(Util.replaceMacro(branchDirectory, buildEnv)));
        }

        if (createTmpdir && isNotEmpty(tmpdir)) {
            args.add("-tmpdir");
            args.add(escapeValue(Util.replaceMacro(tmpdir.trim(), buildEnv)));
        }
        if (isNotEmpty(extraArguments)) {
            args.add(Objects.requireNonNull(Util.replaceMacro(extraArguments.trim(), buildEnv)));
        }
        addMetrics(args, "-calc", calc);
        addMetrics(args, "-recalc", recalc);

        return args.build();
    }

    private String escapeValue(final String value) {
        return "'" + value + "'";
    }

    protected String getBootstrapCmd(final String url, final boolean isLinux) {
        if (isLinux) {
            return ". <(curl --silent --show-error '" + url + "')";
        } else {
            return "[System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('" + url + "'))";
        }
    }

    protected String createCommand(final String bootstrapCmd, final ImmutableList<String> ticsAnalysisCmd, final boolean isLinux) {
        final String command;
        if (isLinux) {
            final String bootstrap = bootstrapCmd.isEmpty() ? "" : bootstrapCmd + " &&";
            command  = "bash -c \"" + bootstrap +  " " + getTicsAnalysisCmd(ticsAnalysisCmd) + "\"";
        } else {
            final String bootstrap = bootstrapCmd.isEmpty() ? "" : bootstrapCmd;
            command = "powershell \"" + bootstrap + "; if ($?) { " + getTicsAnalysisCmd(ticsAnalysisCmd) + " }\"";
        }
        return command;
    }

    protected String getTicsAnalysisCmd(final ImmutableList<String> ticsAnalysisCmd) {
        return String.join(" ", ticsAnalysisCmd);
    }

    private String getInstallTicsApiUrl(final String tiobewebBaseUrl, final String os) throws URISyntaxException {
        final URIBuilder builder = new URIBuilder(ticsConfiguration)
                .addParameter("platform", os)
                .addParameter("url", tiobewebBaseUrl);
        return builder.build().toString();
    }

    private String getNodeOs(final Launcher launcher) {
        return launcher.isUnix() ? "linux" : "windows";
    }

    private static boolean isNotEmpty(final String arg) {
        return !Strings.nullToEmpty(arg).trim().isEmpty();
    }

    void addMetrics(final ImmutableList.Builder<String> args, final String key, final Metrics metrics) {
        final ImmutableList<String> names = metrics.getEnabledMetrics();
        if (!names.isEmpty()) {
            args.add(key);
            args.add(Joiner.on(",").join(names));
        }
    }

    Map<String, String> getEnvMap(final EnvVars buildEnv, final Run<?, ?> run) {
        final Map<String, String> out = Maps.newLinkedHashMap();
        out.putAll(AuthHelper.getPluginEnvMap(buildEnv, environmentVariables));

        if (isNotEmpty(ticsConfiguration)) {
            out.put("TICS", Util.replaceMacro(ticsConfiguration, buildEnv));
        }

        if (isNotEmpty(ticsPath)) {
            out.put("TICSINSTALLDIR", Util.replaceMacro(ticsPath, buildEnv));
        }

        if (!out.containsKey(AuthHelper.TICSAUTHTOKEN)) {
            // Find TICSAUTHTOKEN and assign its value to an environment variable
            final Optional<String> optToken = AuthHelper.lookupTicsAuthToken(run.getParent(), this.credentialsId, buildEnv);
            optToken.ifPresent(token -> out.put(AuthHelper.TICSAUTHTOKEN, Util.replaceMacro(token, buildEnv)));
        }

        return out;
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {
        public DescriptorImpl() {
            load();
        }

        @Override
        public String getDisplayName() {
            return "Run TICS";
        }

        @Override
        public boolean isApplicable(final Class<? extends AbstractProject> type) {
            return true;
        }

        @Override
        public boolean configure(final StaplerRequest staplerRequest, final JSONObject json) {
            save();
            return true; // indicate that everything is good so far
        }

        /** Called by Jenkins to fill credentials dropdown list */
        public ListBoxModel doFillCredentialsIdItems(@AncestorInPath final Item context, @QueryParameter final String credentialsId) {
            return AuthHelper.fillCredentialsDropdown(context, credentialsId);
        }

        @POST
        public FormValidation doCheckProjectName(@AncestorInPath final Item item, @QueryParameter final String value) {
            if (item == null) { // no context
                return FormValidation.ok();
            }
            item.checkPermission(Item.CONFIGURE);

            if (Strings.nullToEmpty(value).trim().isEmpty()) {
                return FormValidation.error("Please provide a project name");
            }
            return FormValidation.ok();
        }

        @POST
        public FormValidation doCheckBranchName(@AncestorInPath final Item item, @QueryParameter final String value) {
            if (item == null) { // no context
                return FormValidation.ok();
            }
            item.checkPermission(Item.CONFIGURE);

            if (Strings.nullToEmpty(value).trim().isEmpty()) {
                return FormValidation.error("Please provide a branch name");
            }
            return FormValidation.ok();
        }

        @POST
        public FormValidation doCheckTmpdir(@AncestorInPath final Item item, @QueryParameter final String value, @QueryParameter final boolean createTmpdir) {
            if (item == null) { // no context
                return FormValidation.ok();
            }
            item.checkPermission(Item.CONFIGURE);

            if (createTmpdir && Strings.nullToEmpty(value).trim().isEmpty()) {
                return FormValidation.error("Please provide a directory");
            }
            return FormValidation.ok();
        }

        @POST
        public FormValidation doCheckTicsConfiguration(@AncestorInPath final Item item,  @AncestorInPath final TaskListener listener, @QueryParameter final String value,
                @QueryParameter("installTics") final boolean installTics) {
            if (item == null) { // no context
                return FormValidation.ok();
            }
            item.checkPermission(Item.CONFIGURE);

            Optional<FormValidation> validation = ValidationHelper.checkViewerUrlIsEmpty(value);
            if (validation.isPresent()) {
                return validation.get();
            }

            if (installTics || isUrlLike(value)) {
                // This validation is performed if:
                // 1. The user checked the checkbox to install TICS, in which case this value needs to be a URL
                // 2. The user did not check the checkbox, but still filled in something that resembles a URL
                // If the checkbox is not checked and the value is something else (probably a path), no check is performed.
                final Pattern urlPattern = Pattern.compile("[^:/]+://[^/]+/[^/]+/[^/]+/api/cfg\\?name=.+");
                final String urlErrorExample = "http(s)://hostname/tiobeweb/section/api/cfg?name=configurationΝame";
                validation = ValidationHelper.checkViewerUrlPattern(value, urlPattern, urlErrorExample);
                if (validation.isPresent()) {
                    return validation.get();
                }

                validation = ValidationHelper.checkViewerBaseUrlAccessibility(value);
                if (validation.isPresent()) {
                    return validation.get();
                }

                validation = ValidationHelper.checkVersionCompatibility(listener, value);
                if (validation.isPresent()) {
                    return validation.get();
                }

                validation = ValidationHelper.checkViewerUrlForWarningsCommon(value);
                if (validation.isPresent()) {
                    return validation.get();
                }
            }

            return FormValidation.ok();
        }

        private boolean isUrlLike(final String url) {
            try {
                new URL(url).toURI();
                return true;
            } catch (final MalformedURLException | URISyntaxException e) {
                return false;
            }
        }
    }
}
