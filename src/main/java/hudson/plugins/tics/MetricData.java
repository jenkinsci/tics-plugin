package hudson.plugins.tics;

import com.google.common.collect.ImmutableList;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.joda.time.Instant;

import java.util.List;

/**
 * Holds metric data produced by {@link TqiPublisherResultBuilder}.
 */
public class MetricData {

    public static class MetricValue {
        public final String status;
        public final String formattedValue;
        public final String letter;

        public MetricValue(
                final String status,
                final String formattedValue,
                final String letter
        ) {
            this.status = status;
            this.formattedValue = formattedValue;
            this.letter = letter;
        }
    }

    public static class Run {
        public final String name;
        public final String description;
        public final List<String> metricNames;
        public final List<MetricValue> metricValues;
        /**
         * Date of the run in ISO format
         */
        public final String date;

        public Run(
                final String name,
                final String description,
                final List<String> metricNames,
                final List<MetricValue> metricValues,
                final String date
        ) {
            this.name = name;
            this.description = description;
            this.metricNames = metricNames;
            this.metricValues = metricValues;
            this.date = date;
        }
    }

    public final String ticsPath;
    public final List<String> metrics;
    public final String measurementDate = Instant.now().toString();
    public final List<Run> runs;
    public final String errorMessage;

    public MetricData(
            final List<String> metrics,
            final List<Run> runs,
            final String ticsPath,
            final @Nullable String errorMessage
    ) {
        this.metrics = metrics;
        this.runs = runs;
        this.ticsPath = ticsPath;
        this.errorMessage = errorMessage;
    }

    public static MetricData error(final String ticsPath, final String message) {
        return new MetricData(ImmutableList.of(), ImmutableList.of(), ticsPath, message);
    }
}
