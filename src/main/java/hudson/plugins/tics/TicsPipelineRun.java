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

    private static final ImmutableSet<String> ALL_METRICS = ImmutableSet.of(
            "ABSTRACTINTERPRETATION",
            "ACCUCHANGERATE",
            "ACCUFIXRATE",
            "ACCULINESADDED",
            "ACCULINESCHANGED",
            "ACCULINESDELETED",
            "AI",
            "ALL",
            "AVGCYCLOMATICCOMPLEXITY",
            "BEGIN",
            "BUILDRELATIONS",
            "CHANGEDFILES",
            "CHANGERATE",
            "CODINGSTANDARD",
            "COMPILERWARNING",
            "CS",
            "CW",
            "CY",
            "CYCLOMATICCOMPLEXITY",
            "DEADCODE",
            "DUP",
            "DUPLICATEDCODE",
            "DUPLICATEDCODECUSTOM",
            "ELOC",
            "END",
            "FANOUT",
            "FANOUT_INTEXT",
            "FINALIZE",
            "FIXRATE",
            "GLOC",
            "HIS_AVGCYCLOMATICCOMPLEXITY",
            "HIS_CALLLEVELS",
            "HIS_CALLLEVELS_MAX",
            "HIS_CODINGSTANDARD",
            "HIS_COMMENTDENSITY",
            "HIS_FUNCCALLS",
            "HIS_FUNCCALLS_MAX",
            "HIS_FUNCCYCLE",
            "HIS_FUNCCYCLE_MAX",
            "HIS_FUNCSIZE",
            "HIS_FUNCSIZE_MAX",
            "HIS_GOTOSTATEMENTS",
            "HIS_GOTOSTATEMENTS_MAX",
            "HIS_MAXCYCLOMATICCOMPLEXITY",
            "HIS_PARAMCOUNT",
            "HIS_PARAMCOUNT_MAX",
            "HIS_PATHCOUNT",
            "HIS_PATHCOUNT_MAX",
            "HIS_RETURNPOINTS",
            "HIS_RETURNPOINTS_MAX",
            "HIS_VOCF",
            "INCLUDERELATIONS",
            "INTEGRATIONBRANCHCOVERAGE",
            "INTEGRATIONDECISIONCOVERAGE",
            "INTEGRATIONFUNCTIONCOVERAGE",
            "INTEGRATIONSTATEMENTCOVERAGE",
            "INTEGRATIONTESTCOVERAGE",
            "ITC",
            "LINESADDED",
            "LINESCHANGED",
            "LINESDELETED",
            "LOC",
            "MAXCYCLOMATICCOMPLEXITY",
            "PATHCOUNT",
            "PATHCOUNT_MAX",
            "POSTANA",
            "PREPARE",
            "SEC",
            "SECURITY",
            "STC",
            "SYSTEMBRANCHCOVERAGE",
            "SYSTEMDECISIONCOVERAGE",
            "SYSTEMFUNCTIONCOVERAGE",
            "SYSTEMSTATEMENTCOVERAGE",
            "SYSTEMTESTCOVERAGE",
            "TOTALBRANCHCOVERAGE",
            "TOTALDECISIONCOVERAGE",
            "TOTALFUNCTIONCOVERAGE",
            "TOTALSTATEMENTCOVERAGE",
            "TOTALTESTCOVERAGE",
            "TTC",
            "UNITBRANCHCOVERAGE",
            "UNITDECISIONCOVERAGE",
            "UNITFUNCTIONCOVERAGE",
            "UNITSTATEMENTCOVERAGE",
            "UNITTESTCOVERAGE",
            "UTC"
    );
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
    public void perform(
            @NonNull final Run<?, ?> run,
            @NonNull final FilePath workspace,
            @NonNull final EnvVars envvars,
            @NonNull final Launcher launcher,
            @NonNull final TaskListener listener
    ) throws IOException, InterruptedException {
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

    protected Metrics createMetricsObject(@Nullable final List<String> metrics) {
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
                metrics.contains("AI"),
                metrics.contains("ALL"),
                metrics.contains("AVGCYCLOMATICCOMPLEXITY"),
                metrics.contains("BEGIN"),
                metrics.contains("BUILDRELATIONS"),
                metrics.contains("CHANGEDFILES"),
                metrics.contains("CHANGERATE"),
                metrics.contains("CODINGSTANDARD"),
                metrics.contains("COMPILERWARNING"),
                metrics.contains("CS"),
                metrics.contains("CW"),
                metrics.contains("CY"),
                metrics.contains("CYCLOMATICCOMPLEXITY"),
                metrics.contains("DEADCODE"),
                metrics.contains("DUP"),
                metrics.contains("DUPLICATEDCODE"),
                metrics.contains("DUPLICATEDCODECUSTOM"),
                metrics.contains("ELOC"),
                metrics.contains("END"),
                metrics.contains("FANOUT"),
                metrics.contains("FANOUT_INTEXT"),
                metrics.contains("FINALIZE"),
                metrics.contains("FIXRATE"),
                metrics.contains("GLOC"),
                metrics.contains("HIS_AVGCYCLOMATICCOMPLEXITY"),
                metrics.contains("HIS_CALLLEVELS"),
                metrics.contains("HIS_CALLLEVELS_MAX"),
                metrics.contains("HIS_CODINGSTANDARD"),
                metrics.contains("HIS_COMMENTDENSITY"),
                metrics.contains("HIS_FUNCCALLS"),
                metrics.contains("HIS_FUNCCALLS_MAX"),
                metrics.contains("HIS_FUNCCYCLE"),
                metrics.contains("HIS_FUNCCYCLE_MAX"),
                metrics.contains("HIS_FUNCSIZE"),
                metrics.contains("HIS_FUNCSIZE_MAX"),
                metrics.contains("HIS_GOTOSTATEMENTS"),
                metrics.contains("HIS_GOTOSTATEMENTS_MAX"),
                metrics.contains("HIS_MAXCYCLOMATICCOMPLEXITY"),
                metrics.contains("HIS_PARAMCOUNT"),
                metrics.contains("HIS_PARAMCOUNT_MAX"),
                metrics.contains("HIS_PATHCOUNT"),
                metrics.contains("HIS_PATHCOUNT_MAX"),
                metrics.contains("HIS_RETURNPOINTS"),
                metrics.contains("HIS_RETURNPOINTS_MAX"),
                metrics.contains("HIS_VOCF"),
                metrics.contains("INCLUDERELATIONS"),
                metrics.contains("INTEGRATIONBRANCHCOVERAGE"),
                metrics.contains("INTEGRATIONDECISIONCOVERAGE"),
                metrics.contains("INTEGRATIONFUNCTIONCOVERAGE"),
                metrics.contains("INTEGRATIONSTATEMENTCOVERAGE"),
                metrics.contains("INTEGRATIONTESTCOVERAGE"),
                metrics.contains("ITC"),
                metrics.contains("LINESADDED"),
                metrics.contains("LINESCHANGED"),
                metrics.contains("LINESDELETED"),
                metrics.contains("LOC"),
                metrics.contains("MAXCYCLOMATICCOMPLEXITY"),
                metrics.contains("PATHCOUNT"),
                metrics.contains("PATHCOUNT_MAX"),
                metrics.contains("POSTANA"),
                metrics.contains("PREPARE"),
                metrics.contains("SEC"),
                metrics.contains("SECURITY"),
                metrics.contains("STC"),
                metrics.contains("SYSTEMBRANCHCOVERAGE"),
                metrics.contains("SYSTEMDECISIONCOVERAGE"),
                metrics.contains("SYSTEMFUNCTIONCOVERAGE"),
                metrics.contains("SYSTEMSTATEMENTCOVERAGE"),
                metrics.contains("SYSTEMTESTCOVERAGE"),
                metrics.contains("TOTALBRANCHCOVERAGE"),
                metrics.contains("TOTALDECISIONCOVERAGE"),
                metrics.contains("TOTALFUNCTIONCOVERAGE"),
                metrics.contains("TOTALSTATEMENTCOVERAGE"),
                metrics.contains("TOTALTESTCOVERAGE"),
                metrics.contains("TTC"),
                metrics.contains("UNITBRANCHCOVERAGE"),
                metrics.contains("UNITDECISIONCOVERAGE"),
                metrics.contains("UNITFUNCTIONCOVERAGE"),
                metrics.contains("UNITSTATEMENTCOVERAGE"),
                metrics.contains("UNITTESTCOVERAGE"),
                metrics.contains("UTC")
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

