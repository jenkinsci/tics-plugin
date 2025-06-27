package hudson.plugins.tics;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import edu.umd.cs.findbugs.annotations.Nullable;
import hudson.plugins.tics.MeasureApiCall.MeasureApiCallException;
import hudson.plugins.tics.MeasureApiSuccessResponse.Baseline;
import hudson.plugins.tics.MeasureApiSuccessResponse.MetricValue;
import hudson.plugins.tics.MeasureApiSuccessResponse.Run;
import hudson.plugins.tics.MeasureApiSuccessResponse.TqiVersion;
import org.joda.time.Instant;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class TqiPublisherResultBuilder {

    public static final ImmutableList<String> METRICS_3_11 = ImmutableList.of("tqi", "tqiTestCoverage", "tqiAbstrInt", "tqiComplexity", "tqiCompWarn", "tqiCodingStd", "tqiDupCode", "tqiFanOut", "tqiDeadCode", "loc");
    public static final ImmutableList<String> METRICS_4_0 = ImmutableList.of("tqi", "tqiTestCoverage", "tqiAbstrInt", "tqiComplexity", "tqiCompWarn", "tqiCodingStd", "tqiDupCode", "tqiFanOut", "tqiSecurity", "loc");
    public static final String TQI_VERSION = "tqiVersion";
    private final String ticsPath;
    private final MeasureApiCall measureApiCall;
    private final PrintStream logger;
    private final Supplier<ImmutableList<String>> metrics;

    public TqiPublisherResultBuilder(
            final PrintStream logger,
            final MeasureApiCall apiCall,
            final String ticsPath
    ) {
        this.logger = logger;
        this.ticsPath = ticsPath;
        this.measureApiCall = apiCall;
        this.metrics = Suppliers.memoize(() -> {
            final boolean hasSecurity = this.doesTqiVersionIncludeSecurity();
            return hasSecurity ? METRICS_4_0 : METRICS_3_11;
        });
    }

    private String formatDate(final Instant date) {
        return date.toDateTime().toString("YYYY-MM-dd HH:mm:ss");
    }

    public @Nullable MetricData run() throws MeasureApiCallException {
        final List<Run> runDatesDesc = getRunDatesDescending();
        if (runDatesDesc.isEmpty()) {
            return MetricData.error(this.ticsPath, "Project has no runs yet");
        }
        final Run lastRun = runDatesDesc.get(0);
        final MetricData.Run currentRun = this.getRunDataOrThrow("Current", Optional.empty(), "TQI Scores at " + this.formatDate(lastRun.getStarted()));

        final List<MetricData.Run> runsData = new ArrayList<>();
        runsData.add(currentRun);
        runDatesDesc.stream().skip(1).findFirst()
                .flatMap(run -> this.tryGetRunData("\u0394Previous", Optional.of(run.getStarted()), "Delta with previous run at " + this.formatDate(run.getStarted())))
                .ifPresent(runsData::add);
        this.baseline.get()
                .flatMap(bl -> this.tryGetRunData("\u0394" + bl.getName(), Optional.of(bl.getStarted()), "Delta with baseline '" + bl.getName() + "' at " + this.formatDate(bl.getStarted())))
                .ifPresent(runsData::add);
        return new MetricData(currentRun.metricNames, runsData, this.ticsPath, null);
    }

    private Optional<MetricData.Run> tryGetRunData(final String runName, final Optional<Instant> date, final String description) {
        try {
            return Optional.of(this.getRunDataOrThrow(runName, date, description));
        } catch (final MeasureApiCallException ex) {
            ex.printStackTrace(this.logger);
            return Optional.empty();
        }
    }

    private MetricData.Run getRunDataOrThrow(final String runName, final Optional<Instant> deltaDate, final String description) throws MeasureApiCallException {
        final String metricExpr = deltaDate.map(instant -> this.metrics.get().stream()
                        .map(m -> "Delta(" + m + "," + (instant.getMillis() / 1000L) + ")")
                        .collect(joining(",")))
                .orElseGet(() -> String.join(",", this.metrics.get()));
        final MeasureApiSuccessResponse<Number> resp = this.measureApiCall.execute(MeasureApiCall.RESPONSE_NUMBER_TYPETOKEN, this.ticsPath, metricExpr);
        final List<String> metricNames = resp.metrics.stream()
                .map(m -> m.fullName)
                .collect(toList());
        final List<MetricData.MetricValue> metricValues = new ArrayList<>();
        for (int i = 0; i < metricNames.size(); i++) {
            final MetricValue<Number> mv = resp.data.get(i);

            // We want to use formattedValue because it contains correct number of decimals, % symbol, thousand separators, etc.
            // However, formattedValue can contain HTML, such as for delta metrics and for errors.
            // We use JSoup for stripping this HTML. Note that the formattedValue will be rendered by Jelly (which does HTML-escaping),
            // so note that JSoup.clean() is not used for preventing XSS vulnerabilities.
            final String formattedValueStripped = Jsoup.clean(mv.formattedValue, Safelist.none());

            metricValues.add(new MetricData.MetricValue(
                    mv.status,
                    formattedValueStripped,
                    mv.letter
            ));
        }
        return new MetricData.Run(
                runName,
                description,
                metricNames,
                metricValues,
                deltaDate.map(Instant::toString).orElse(null)
        );
    }

    private boolean doesTqiVersionIncludeSecurity() {
        final MeasureApiSuccessResponse<TqiVersion> resp;
        try {
            resp = this.measureApiCall.execute(MeasureApiCall.RESPONSE_TQIVERSION_TYPETOKEN, this.ticsPath, TQI_VERSION);
        } catch (final MeasureApiCallException e) {
            e.printStackTrace(this.logger);
            return false;
        }
        if (resp.data.isEmpty() || resp.data.get(0).value == null) {
            return false;
        }
        final TqiVersion tqiVersion = resp.data.get(0).value;
        return tqiVersion.compareTo(new TqiVersion(4, 0)) >= 0;
    }

    private List<Run> getRunDatesDescending() throws MeasureApiCallException {
        final MeasureApiSuccessResponse<List<Run>> resp = measureApiCall.execute(MeasureApiCall.RESPONSE_RUNS_TYPETOKEN, TqiPublisherResultBuilder.this.ticsPath, "runs");
        if (resp.data.isEmpty()) {
            return new ArrayList<>();
        }
        final MetricValue<List<Run>> mv = resp.data.get(0);
        return Lists.reverse(Optional.ofNullable(mv.value).orElseGet(ArrayList::new));
    }

    private final Supplier<Optional<Baseline>> baseline = Suppliers.memoize(new Supplier<>() {
        @Override
        public Optional<Baseline> get() {
            final MeasureApiSuccessResponse<List<Baseline>> resp;
            try {
                resp = measureApiCall.execute(MeasureApiCall.RESPONSE_BASELINES_TYPETOKEN, ticsPath, "baselines");
            } catch (final MeasureApiCallException e) {
                e.printStackTrace(logger);
                return Optional.empty();
            }
            if (resp.data.isEmpty()) {
                return Optional.empty();
            }
            final MetricValue<List<Baseline>> mv = resp.data.get(0);
            final List<Baseline> baselines = mv.value;
            if (baselines == null || baselines.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(baselines.get(baselines.size() - 1));
            }
        }
    });


}
