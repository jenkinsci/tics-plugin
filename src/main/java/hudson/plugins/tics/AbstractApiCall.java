package hudson.plugins.tics;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import hudson.ProxyConfiguration;
import hudson.plugins.tics.MeasureApiCall.MeasureApiCallException;
import hudson.plugins.tics.MeasureApiErrorResponse.AlertMessage;
import hudson.util.Secret;
import jenkins.model.Jenkins;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.PrintStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractApiCall {
    private final PrintStream logger;
    private final Optional<Pair<String, String>> credentials;
    private final String apiCallPrefix;
    private final String url;
    public static final ImmutableList<Pattern> LOCALHOST_PATTERNS = ImmutableList.of(Pattern.compile("localhost"), Pattern.compile("127\\..*"));


    public AbstractApiCall(final String apiCallName, final PrintStream logger, final Optional<Pair<String, String>> credentials, final String url) {
        this.apiCallPrefix = apiCallName;
        this.logger = logger;
        this.credentials = credentials;
        this.url = url;
    }

    protected final CloseableHttpClient createHttpClient() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, MeasureApiCallException {
        final int timeoutMs = 300 * 1000;
        final RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(timeoutMs)
                .setSocketTimeout(timeoutMs)
                .setConnectionRequestTimeout(timeoutMs)
                .build();
        HttpClientBuilder builder = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig);
        final CredentialsProvider credsProvider = new BasicCredentialsProvider();

        if (credentials.isPresent()) {
            final String username = credentials.get().getLeft();
            final String password = credentials.get().getRight();
            credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        }

        String proxyUsageMsg = "";
        final Jenkins jenkins = Jenkins.get();

        final ProxyConfiguration proxy = jenkins.proxy;
        if (proxy != null) {
            final String proxyName = proxy.getName();
            final int proxyPort = proxy.getPort();
            final ImmutableList<Pattern> noProxyPatterns = ImmutableList.copyOf(proxy.getNoProxyHostPatterns());
            final String proxyUser = proxy.getUserName();
            final String proxyPass = Secret.toString(proxy.getSecretPassword());

            if (!isProxyExempted(url, noProxyPatterns)) {
                proxyUsageMsg += "Using proxy: " + proxyName + ":" + proxyPort;
                final HttpHost hostProxy = new HttpHost(proxyName, proxyPort);
                builder = builder.setProxy(hostProxy);
                // Only set credentials if provided.
                if (!Strings.isNullOrEmpty(proxyUser) && !Strings.isNullOrEmpty(proxyPass)) {
                    proxyUsageMsg += " with credentials for " + proxyUser;
                    credsProvider.setCredentials(
                            new AuthScope(proxyName, proxyPort),
                            new UsernamePasswordCredentials(proxyUser, proxyPass)
                    );
                }
            }
        }

        logger.println(proxyUsageMsg);
        builder = builder.setDefaultCredentialsProvider(credsProvider);
        return builder.build();
    }


    protected boolean isProxyExempted(final String urlStr, final ImmutableList<Pattern> noProxyPatterns) {
        Matcher matcher;
        // Bypassing proxy for internal addresses by default
        for (final Pattern p : Iterables.concat(noProxyPatterns, LOCALHOST_PATTERNS)) {
            matcher = p.matcher(urlStr);
            if (matcher.find()) {
                return true;
            }
        }
        return false;
    }

    protected void throwIfStatusNotOk(final HttpResponse response, final String body) throws MeasureApiCallException {
        final int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode == HttpStatus.SC_OK) {
            return;
        }

        if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
            if (credentials.isPresent()) {
                throw new MeasureApiCallException(apiCallPrefix + " 401 Unauthorized - Invalid username/password combination");
            } else {
                throw new MeasureApiCallException(apiCallPrefix + " 401 Unauthorized - Project requires authentication, but no credentials provided");
            }
        }

        if (statusCode == HttpStatus.SC_PROXY_AUTHENTICATION_REQUIRED) {
            throw new MeasureApiCallException(apiCallPrefix + " " + HttpStatus.SC_PROXY_AUTHENTICATION_REQUIRED + " Proxy Authentication Required");
        }

        final Optional<String> formattedError = tryExtractExceptionMessageFromBody(body);
        final String reason = response.getStatusLine().getReasonPhrase();
        if (formattedError.isPresent()) {
            throw new MeasureApiCallException(apiCallPrefix + " " + statusCode + " " + reason + " - " + formattedError.get());
        } else {
            // Do not give body in exception, as it might contain html, potentially messing up the Jenkins view.
            logger.println(apiCallPrefix + " " + body);
            throw new MeasureApiCallException(apiCallPrefix + " " + statusCode + " " + reason + " - See the build log for a detailed error report.");
        }
    }

    private Optional<String> tryExtractExceptionMessageFromBody(final String body) {
        if (body.startsWith("{")) {
            // body is Json
            final MeasureApiErrorResponse out;
            try {
                out = new Gson().fromJson(body, MeasureApiErrorResponse.class);
            } catch (final Exception ex) {
                return Optional.empty();
            }
            if (out == null || out.alertMessages.isEmpty()) {
                return Optional.empty();
            }
            final AlertMessage am0 = out.alertMessages.get(0);
            logger.println(am0.stackTrace);
            return Optional.ofNullable(am0.message);
        }

        // Body is html. Happens e.g. in case non-existing section is provided
        final Matcher matcher = Pattern.compile("Exception: ([^\n]+)").matcher(body);
        if (matcher.find()) {
            return Optional.of(matcher.group(1));
        }

        return Optional.empty();
    }

}