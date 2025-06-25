package hudson.plugins.tics;

import com.google.gson.Gson;
import hudson.model.TaskListener;
import hudson.plugins.tics.MeasureApiCall.MeasureApiCallException;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;


public class QualityGateApiCall extends AbstractApiCall {

    private static final String LOGGING_PREFIX = "[TICS Quality Gating] ";
    private final String qualityGateUrl;
    private final String project;
    private final String branch;

    public QualityGateApiCall(final String qualityGateUrl, final String ticsPath, final Optional<Pair<String, String>> credentials, final TaskListener listener) {
        super(LOGGING_PREFIX, listener.getLogger(), credentials, qualityGateUrl);
        final String projectAndBranch = ticsPath.split("://")[1];
        final String[] parts = projectAndBranch.split("/", 2);
        this.project = parts[0];
        this.branch = parts[1];
        this.qualityGateUrl = qualityGateUrl;
    }

    public QualityGateData retrieveQualityGateData() {
        final String url;
        try {
            final URIBuilder builder = new URIBuilder(this.qualityGateUrl)
                    .setParameter("project", this.project)
                    .setParameter("branch", this.branch);
            url = builder.build().toString();
        } catch (final URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL: " + e.getMessage());
        }

        final String response = this.performHttpRequest(url);

        final QualityGateApiResponse resp = new Gson().fromJson(response, QualityGateApiResponse.class);
        return QualityGateData.success(this.project, this.branch, resp);
    }

    private String performHttpRequest(final String url) {
        final HttpGet httpGet = new HttpGet(url);
        try (final CloseableHttpClient httpclient = this.createHttpClient();
             final CloseableHttpResponse response = httpclient.execute(httpGet)
        ) {
            final String body = EntityUtils.toString(response.getEntity());
            this.throwIfStatusNotOk(response, body);
            return body;
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException | IOException |
                 MeasureApiCallException ex) {
            throw new RuntimeException("Error while performing API request to " + url, ex);
        }
    }

}
