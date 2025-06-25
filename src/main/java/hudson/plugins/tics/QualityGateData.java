package hudson.plugins.tics;

import edu.umd.cs.findbugs.annotations.Nullable;
import org.joda.time.Instant;

import java.util.Optional;

/**
 * Holds data produced by {@link QualityGateApiCall}.
 */
public class QualityGateData {
    public final @Nullable String project;
    public final @Nullable String branch;
    public final @Nullable QualityGateApiResponse apiResponse;
    public final String measurementDate = Instant.now().toString();
    public final @Nullable String errorMessage;
    /**
     * True when ApiCall was successful and project passed quality gate.
     */
    public final boolean passed;

    public QualityGateData(
            final @Nullable String project,
            final @Nullable String branch,
            final @Nullable QualityGateApiResponse apiResponse,
            final @Nullable String errorMessage
    ) {
        this.project = project;
        this.branch = branch;
        this.apiResponse = apiResponse;
        this.errorMessage = errorMessage;
        this.passed = Optional.ofNullable(apiResponse).map(resp -> resp.passed).orElse(false);
    }

    public static QualityGateData error(final String message) {
        return new QualityGateData(null, null, null, message);
    }

    public static QualityGateData success(final String project, final String branch, final QualityGateApiResponse apiResponse) {
        return new QualityGateData(project, branch, apiResponse, null);
    }

}