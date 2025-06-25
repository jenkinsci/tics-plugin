package hudson.plugins.tics;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import hudson.model.TaskListener;
import hudson.plugins.tics.MeasureApiCall.MeasureApiCallException;
import hudson.plugins.tics.TicsPublisher.InvalidTicsViewerUrl;
import hudson.util.FormValidation;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.URIBuilder;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Helper methods to be used in TICSAnalyzer and TICSPublisher for viewer url validation or formatting.
 */
public class ValidationHelper {

    public static Optional<FormValidation> checkViewerUrlIsEmpty(final String url) {
        if (Strings.isNullOrEmpty(url)) {
            return Optional.of(FormValidation.error("Field is required"));
        }

        return Optional.empty();
    }

    public static Optional<FormValidation> checkViewerUrlPattern(final String url, final Pattern pattern, final String example) {
        Matcher matcher = pattern.matcher(url);
        if (!matcher.matches()) {
            return Optional.of(FormValidation.errorWithMarkup("URL should be of the form <code>" + example + "</code>"));
        }

        return Optional.empty();
    }

    public static Optional<FormValidation> checkViewerBaseUrlAccessibility(final String url) {
        try {
            final String measureApiUrl = getMeasureApiUrl(getTiobewebBaseUrlFromGivenUrl(url));
            final PrintStream dummyLogger = new PrintStream(new ByteArrayOutputStream(), false,
                    StandardCharsets.UTF_8);
            final MeasureApiCall apiCall = new MeasureApiCall(dummyLogger, measureApiUrl, Optional.empty());
            apiCall.execute(MeasureApiCall.RESPONSE_DOUBLE_TYPETOKEN, "HIE://", "none");
            return Optional.empty();
        } catch (final MeasureApiCallException | InvalidTicsViewerUrl e) {
            return Optional.of(FormValidation.errorWithMarkup(e.getMessage()));
        }
    }

    public static Optional<FormValidation> checkVersionCompatibility(final TaskListener listener, final String url) {
        try {
            final String ticsversionApi = getTiobewebBaseUrlFromGivenUrl(url) + "/api/v1/version";
            final PrintStream dummyLogger = new PrintStream(new ByteArrayOutputStream(), false,
                    StandardCharsets.UTF_8);
            final TicsVersionApiCall ticsVersionApiCall = new TicsVersionApiCall(ticsversionApi, Optional.empty(), dummyLogger);
            final String actualVersion = ticsVersionApiCall.retrieveTicsVersion();

            if (Strings.isNullOrEmpty(actualVersion)) {
                return Optional.empty();
            }

            final String expectedVersion = "2021.4";
            final List<Integer> expectedVParts = parseVersion(expectedVersion);
            final List<Integer> actualVParts = parseVersion(actualVersion);

            if (compareVersions(expectedVParts, actualVParts) > 0) {
                return Optional.of(FormValidation.errorWithMarkup("The feature is not supported for version " + actualVersion + ". It is only available from version 2021.4.x and above."));
            }

        } catch (InvalidTicsViewerUrl e) {
            return Optional.of(FormValidation.errorWithMarkup(e.getMessage()));
        }

        return Optional.empty();
    }

    private static int compareVersions(final List<Integer> base, final List<Integer> other) {
        return ComparisonChain.start()
                .compare(base.get(0), other.get(0))
                .compare(base.get(1), other.get(1))
                .result();
    }

    private static List<Integer> parseVersion(final String version) {
        List<Integer> parts = Lists.newArrayList(Splitter.on(".").split(version))
                .stream()
                .map(Ints::tryParse)
                .filter(Objects::nonNull)
                .toList();

        if (parts.size() < 2) {
            throw new IllegalArgumentException("There was a problem parsing the TICS Viewer version: " + version);
        }

        return parts;
    }

    public static Optional<FormValidation> checkViewerUrlForWarningsCommon(final String value) {
        final String host;
        try {
            host = new URIBuilder(value).getHost();
        } catch (final URISyntaxException e) {
            return Optional.empty();
        }
        if (host.equals("localhost") || host.equals("127.0.0.1")) {
            return Optional.of(FormValidation.warning("Please provide a publicly accessible host, instead of " + host));
        }
        return Optional.empty();
    }

    /**
     * Generic usage helper methods
     */
    public static String getTiobewebBaseUrlFromGivenUrl(final String arg0) throws InvalidTicsViewerUrl {
        final String url = StringUtils.stripEnd(arg0, "/");

        final ArrayList<String> parts = Lists.newArrayList(Splitter.on("/").split(url));
        if (parts.size() < 3) {
            throw new InvalidTicsViewerUrl("Missing host name in TICS Viewer URL");
        }
        if (parts.size() < 5) {
            throw new InvalidTicsViewerUrl("Missing section name in TICS Viewer URL");
        }
        parts.set(3, "tiobeweb"); // change TIOBEPortal to tiobeweb
        return Joiner.on("/").join(parts.subList(0, 5) /* include section name */);
    }

    public static String getMeasureApiUrl(final String tiobewebBaseUrl) {
        return tiobewebBaseUrl + "/api/public/v1/Measure";
    }

}
