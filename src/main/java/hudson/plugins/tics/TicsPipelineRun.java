package hudson.plugins.tics;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

@SuppressFBWarnings("PA_PUBLIC_PRIMITIVE_ATTRIBUTE")
public class TicsPipelineRun extends Builder implements SimpleBuildStep {

    private static final ImmutableSet<String> ALL_METRICS = ImmutableSet.of("ABSTRACTINTERPRETATION",
            "ACCUCHANGERATE",
            "ACCUFIXRATE",
            "ACCULINESADDED",
            "ACCULINESCHANGED",
            "ACCULINESDELETED",
            "ALL",
            "AVGCYCLOMATICCOMPLEXITY",
            "BUILDRELATIONS",
            "CHANGEDFILES",
            "CHANGERATE",
            "CODINGSTANDARD",
            "COMPILERWARNING",
            "DEADCODE",
            "DUPLICATEDCODE",
            "ELOC",
            "FANOUT",
            "FINALIZE",
            "FIXRATE",
            "GLOC",
            "INCLUDERELATIONS",
            "INTEGRATIONTESTCOVERAGE",
            "LINESADDED",
            "LINESCHANGED",
            "LINESDELETED",
            "LOC",
            "MAXCYCLOMATICCOMPLEXITY",
            "PREPARE",
            "SECURITY",
            "SYSTEMTESTCOVERAGE",
            "TOTALTESTCOVERAGE",
            "UNITTESTCOVERAGE");
    public final String projectName;
    public final String branchName;
    public List<String> calc;
    public List<String> recalc;
    public String ticsBin;
    public String ticsConfiguration;
    public String branchDirectory;
    public String extraArguments;
    public String tmpdir;
    public LinkedHashMap<String, String> environmentVariables;
    public boolean installTics;
    public String credentialsId;


    @DataBoundConstructor
    public TicsPipelineRun(
            final String projectName,
            final String branchName) {
        this.projectName = projectName;
        this.branchName = branchName;
    }

    @Override
    public void perform(@NonNull final Run<?, ?> run,
                        @NonNull final FilePath workspace,
                        @NonNull final EnvVars envvars,
                        @NonNull final Launcher launcher,
                        @NonNull final TaskListener listener) throws IOException, InterruptedException {

        final boolean createTmpdir = !Strings.isNullOrEmpty(tmpdir);
        final TicsAnalyzer ta = new TicsAnalyzer(
                ticsBin,
                ticsConfiguration,
                projectName,
                branchName,
                branchDirectory,
                convertEnvironmentVariablesToString(),
                createTmpdir,
                tmpdir,
                extraArguments,
                createMetricsObject(calc),
                createMetricsObject(recalc),
                installTics,
                credentialsId
        );
        ta.perform(run, workspace, envvars, launcher, listener);
    }

    private String convertEnvironmentVariablesToString() {
        return environmentVariables == null ? null : Joiner.on("\n").withKeyValueSeparator("=").useForNull("").join(environmentVariables);
    }

    private Metrics createMetricsObject(@Nullable final List<String> metrics) {
        if (metrics == null || metrics.isEmpty()) {
            return null;
        }
        final List<String> incorrectMetrics = metrics.stream().filter(a -> !ALL_METRICS.contains(a)).toList();
        if (!incorrectMetrics.isEmpty()) {
            throw new IllegalArgumentException("The following metrics are incorrect: " + incorrectMetrics + ". \nThe available metrics are: " + String.join(", ", ALL_METRICS));
        }

        return new Metrics(
                metrics.contains("ABSTRACTINTERPRETATION"),
                metrics.contains("ACCUCHANGERATE"),
                metrics.contains("ACCUFIXRATE"),
                metrics.contains("ACCULINESADDED"),
                metrics.contains("ACCULINESCHANGED"),
                metrics.contains("ACCULINESDELETED"),
                metrics.contains("ALL"),
                metrics.contains("AVGCYCLOMATICCOMPLEXITY"),
                metrics.contains("BUILDRELATIONS"),
                metrics.contains("CHANGEDFILES"),
                metrics.contains("CHANGERATE"),
                metrics.contains("CODINGSTANDARD"),
                metrics.contains("COMPILERWARNING"),
                metrics.contains("DEADCODE"),
                metrics.contains("DUPLICATEDCODE"),
                metrics.contains("ELOC"),
                metrics.contains("FANOUT"),
                metrics.contains("FINALIZE"),
                metrics.contains("FIXRATE"),
                metrics.contains("GLOC"),
                metrics.contains("INCLUDERELATIONS"),
                metrics.contains("INTEGRATIONTESTCOVERAGE"),
                metrics.contains("LINESADDED"),
                metrics.contains("LINESCHANGED"),
                metrics.contains("LINESDELETED"),
                metrics.contains("LOC"),
                metrics.contains("MAXCYCLOMATICCOMPLEXITY"),
                metrics.contains("PREPARE"),
                metrics.contains("SECURITY"),
                metrics.contains("SYSTEMTESTCOVERAGE"),
                metrics.contains("TOTALTESTCOVERAGE"),
                metrics.contains("UNITTESTCOVERAGE")
        );
    }

    @DataBoundSetter
    public void setRecalc(final List<String> value) {
        this.recalc = value;
    }

    @DataBoundSetter
    public void setCalc(final List<String> value) {
        this.calc = value;
    }

    @DataBoundSetter
    public void setTicsConfiguration(final String value) {
        this.ticsConfiguration = value;
    }

    @DataBoundSetter
    public void setTicsBin(final String value) {
        this.ticsBin = value;
    }

    @DataBoundSetter
    public void setBranchDirectory(final String value) {
        this.branchDirectory = value;
    }

    @DataBoundSetter
    public void setExtraArguments(final String value) {
        this.extraArguments = value;
    }

    @DataBoundSetter
    public void setTmpdir(final String value) {
        this.tmpdir = value;
    }

    @DataBoundSetter
    public void setEnvironmentVariables(final LinkedHashMap<String, String> value) {
        this.environmentVariables = value;
    }

    @DataBoundSetter
    public void setInstallTics(final boolean value) {
        this.installTics = value;
    }

    @DataBoundSetter
    public void setCredentialsId(final String value) {
        this.credentialsId = value;
    }

    @Symbol("runTics")
    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Builder> {

        @NonNull
        @Override
        public String getDisplayName() {
            return "";
        }

        @Override
        public boolean isApplicable(final Class<? extends AbstractProject> jobType) {
            return true;
        }

    }
}

