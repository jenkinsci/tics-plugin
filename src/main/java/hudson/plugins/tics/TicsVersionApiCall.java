package hudson.plugins.tics;

import com.google.gson.Gson;
import hudson.plugins.tics.MeasureApiCall.MeasureApiCallException;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.PrintStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class TicsVersionApiCall extends AbstractApiCall {

    private static final String LOGGING_PREFIX = "[TICS Version]";
    private final String ticsVersionUrl;

    public TicsVersionApiCall(final String ticsVersionUrl, final Optional<Pair<String, String>> credentials, final PrintStream logger) {
        super(LOGGING_PREFIX, logger, credentials, ticsVersionUrl);
        this.ticsVersionUrl = ticsVersionUrl;
    }

    public String retrieveTicsVersion() {
        final String url = this.ticsVersionUrl;
        final String response = this.performHttpRequest(url);

        final TicsVersionApiResponse resp = new Gson().fromJson(response, TicsVersionApiResponse.class);

        return resp.version;
    }

    private String performHttpRequest(final String url) {
        final HttpGet httpGet = new HttpGet(url);
        try (final CloseableHttpClient httpclient = this.createHttpClient();
             final CloseableHttpResponse response = httpclient.execute(httpGet)) {
            final String body = EntityUtils.toString(response.getEntity());

            this.throwIfStatusNotOk(response, body);
            return body;
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException | IOException |
                 MeasureApiCallException ex) {
            throw new RuntimeException("Error while performing API request to " + url, ex);
        }
    }
}

