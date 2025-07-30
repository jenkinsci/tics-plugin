package hudson.plugins.tics;

import com.google.common.collect.ImmutableList;
import hudson.EnvVars;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TicsAnalyzerTest {

    public String ticsPath = "";
    public String ticsConfiguration = "http://192.168.1.204:42506/tiobeweb/TICS/api/cfg?name=default";
    public String environmentVariables = "";
    public boolean createTmpdir = true;
    public boolean installTics = false;
    public String credentialsId = "auth-token";


    private TicsAnalyzer getTicsAnalyzer(final Metrics calcMetrics, final Metrics recalcMetrics, final TicsArguments ticsArgs) {
        return new TicsAnalyzer(ticsPath
                , ticsConfiguration
                , ticsArgs.projectName
                , ticsArgs.branchName
                , ticsArgs.branchDirectory
                , environmentVariables
                , createTmpdir
                , ticsArgs.tmpdir
                , ticsArgs.extraArguments
                , calcMetrics
                , recalcMetrics
                , installTics
                , credentialsId);
    }

    private Metrics getMetrics(final boolean codingStandard, final boolean compilerWarning, final boolean finalize, final boolean loc) {
    	final boolean ABSTRACTINTERPRETATION = false;
    	final boolean ACCUCHANGERATE = false;
    	final boolean ACCUFIXRATE = false;
    	final boolean ACCULINESADDED = false;
    	final boolean ACCULINESCHANGED = false;
    	final boolean ACCULINESDELETED = false;
    	final boolean AI = false;
    	final boolean ALL = false;
    	final boolean AVGCYCLOMATICCOMPLEXITY = false;
    	final boolean BEGIN = false;
    	final boolean BUILDRELATIONS = false;
    	final boolean CHANGEDFILES = false;
    	final boolean CHANGERATE = false;
    	final boolean CODINGSTANDARD = codingStandard;
    	final boolean COMPILERWARNING = compilerWarning;
    	final boolean CS = false;
    	final boolean CW = false;
    	final boolean CY = false;
    	final boolean CYCLOMATICCOMPLEXITY = false;
    	final boolean DEADCODE = false;
    	final boolean DUP = false;
    	final boolean DUPLICATEDCODE = false;
    	final boolean DUPLICATEDCODECUSTOM = false;
    	final boolean ELOC = false;
    	final boolean END = false;
    	final boolean FANOUT = false;
    	final boolean FANOUT_INTEXT = false;
    	final boolean FINALIZE = finalize;
    	final boolean FIXRATE = false;
    	final boolean GLOC = false;
    	final boolean HIS_AVGCYCLOMATICCOMPLEXITY = false;
    	final boolean HIS_CALLLEVELS = false;
    	final boolean HIS_CALLLEVELS_MAX = false;
    	final boolean HIS_CODINGSTANDARD = false;
    	final boolean HIS_COMMENTDENSITY = false;
    	final boolean HIS_FUNCCALLS = false;
    	final boolean HIS_FUNCCALLS_MAX = false;
    	final boolean HIS_FUNCCYCLE = false;
    	final boolean HIS_FUNCCYCLE_MAX = false;
    	final boolean HIS_FUNCSIZE = false;
    	final boolean HIS_FUNCSIZE_MAX = false;
    	final boolean HIS_GOTOSTATEMENTS = false;
    	final boolean HIS_GOTOSTATEMENTS_MAX = false;
    	final boolean HIS_MAXCYCLOMATICCOMPLEXITY = false;
    	final boolean HIS_PARAMCOUNT = false;
    	final boolean HIS_PARAMCOUNT_MAX = false;
    	final boolean HIS_PATHCOUNT = false;
    	final boolean HIS_PATHCOUNT_MAX = false;
    	final boolean HIS_RETURNPOINTS = false;
    	final boolean HIS_RETURNPOINTS_MAX = false;
    	final boolean HIS_VOCF = false;
    	final boolean INCLUDERELATIONS = false;
    	final boolean INTEGRATIONBRANCHCOVERAGE = false;
    	final boolean INTEGRATIONDECISIONCOVERAGE = false;
    	final boolean INTEGRATIONFUNCTIONCOVERAGE = false;
    	final boolean INTEGRATIONSTATEMENTCOVERAGE = false;
    	final boolean INTEGRATIONTESTCOVERAGE = false;
    	final boolean ITC = false;
    	final boolean LINESADDED = false;
    	final boolean LINESCHANGED = false;
    	final boolean LINESDELETED = false;
    	final boolean LOC = loc;
    	final boolean MAXCYCLOMATICCOMPLEXITY = false;
    	final boolean PATHCOUNT = false;
    	final boolean PATHCOUNT_MAX = false;
    	final boolean POSTANA = false;
    	final boolean PREPARE = false;
    	final boolean SEC = false;
    	final boolean SECURITY = false;
    	final boolean STC = false;
    	final boolean SYSTEMBRANCHCOVERAGE = false;
    	final boolean SYSTEMDECISIONCOVERAGE = false;
    	final boolean SYSTEMFUNCTIONCOVERAGE = false;
    	final boolean SYSTEMSTATEMENTCOVERAGE = false;
    	final boolean SYSTEMTESTCOVERAGE = false;
    	final boolean TOTALBRANCHCOVERAGE = false;
    	final boolean TOTALDECISIONCOVERAGE = false;
    	final boolean TOTALFUNCTIONCOVERAGE = false;
    	final boolean TOTALSTATEMENTCOVERAGE = false;
    	final boolean TOTALTESTCOVERAGE = false;
    	final boolean TTC = false;
    	final boolean UNITBRANCHCOVERAGE = false;
    	final boolean UNITDECISIONCOVERAGE = false;
    	final boolean UNITFUNCTIONCOVERAGE = false;
    	final boolean UNITSTATEMENTCOVERAGE = false;
    	final boolean UNITTESTCOVERAGE = false;
    	final boolean UTC = false;

        return new Metrics(
        		ABSTRACTINTERPRETATION,
        		ACCUCHANGERATE,
        		ACCUFIXRATE,
        		ACCULINESADDED,
        		ACCULINESCHANGED,
        		ACCULINESDELETED,
        		AI,
        		ALL,
        		AVGCYCLOMATICCOMPLEXITY,
        		BEGIN,
        		BUILDRELATIONS,
        		CHANGEDFILES,
        		CHANGERATE,
        		CODINGSTANDARD,
        		COMPILERWARNING,
        		CS,
        		CW,
        		CY,
        		CYCLOMATICCOMPLEXITY,
        		DEADCODE,
        		DUP,
        		DUPLICATEDCODE,
        		DUPLICATEDCODECUSTOM,
        		ELOC,
        		END,
        		FANOUT,
        		FANOUT_INTEXT,
        		FINALIZE,
        		FIXRATE,
        		GLOC,
        		HIS_AVGCYCLOMATICCOMPLEXITY,
        		HIS_CALLLEVELS,
        		HIS_CALLLEVELS_MAX,
        		HIS_CODINGSTANDARD,
        		HIS_COMMENTDENSITY,
        		HIS_FUNCCALLS,
        		HIS_FUNCCALLS_MAX,
        		HIS_FUNCCYCLE,
        		HIS_FUNCCYCLE_MAX,
        		HIS_FUNCSIZE,
        		HIS_FUNCSIZE_MAX,
        		HIS_GOTOSTATEMENTS,
        		HIS_GOTOSTATEMENTS_MAX,
        		HIS_MAXCYCLOMATICCOMPLEXITY,
        		HIS_PARAMCOUNT,
        		HIS_PARAMCOUNT_MAX,
        		HIS_PATHCOUNT,
        		HIS_PATHCOUNT_MAX,
        		HIS_RETURNPOINTS,
        		HIS_RETURNPOINTS_MAX,
        		HIS_VOCF,
        		INCLUDERELATIONS,
        		INTEGRATIONBRANCHCOVERAGE,
        		INTEGRATIONDECISIONCOVERAGE,
        		INTEGRATIONFUNCTIONCOVERAGE,
        		INTEGRATIONSTATEMENTCOVERAGE,
        		INTEGRATIONTESTCOVERAGE,
        		ITC,
        		LINESADDED,
        		LINESCHANGED,
        		LINESDELETED,
        		LOC,
        		MAXCYCLOMATICCOMPLEXITY,
        		PATHCOUNT,
        		PATHCOUNT_MAX,
        		POSTANA,
        		PREPARE,
        		SEC,
        		SECURITY,
        		STC,
        		SYSTEMBRANCHCOVERAGE,
        		SYSTEMDECISIONCOVERAGE,
        		SYSTEMFUNCTIONCOVERAGE,
        		SYSTEMSTATEMENTCOVERAGE,
        		SYSTEMTESTCOVERAGE,
        		TOTALBRANCHCOVERAGE,
        		TOTALDECISIONCOVERAGE,
        		TOTALFUNCTIONCOVERAGE,
        		TOTALSTATEMENTCOVERAGE,
        		TOTALTESTCOVERAGE,
        		TTC,
        		UNITBRANCHCOVERAGE,
        		UNITDECISIONCOVERAGE,
        		UNITFUNCTIONCOVERAGE,
        		UNITSTATEMENTCOVERAGE,
        		UNITTESTCOVERAGE,
        		UTC
		);
    }

    enum Platform {
        Linux,
        Windows,
    }

    private static class TicsArguments {
        public String projectName;
        public String branchName;
        public String branchDirectory;
        public String tmpdir;
        public String extraArguments;

        public TicsArguments(final String projectName, final String branchName, final String branchDirectory, final String tmpdir, final String extraArguments) {
            this.projectName = projectName;
            this.branchName = branchName;
            this.branchDirectory = branchDirectory;
            this.tmpdir = tmpdir;
            this.extraArguments = extraArguments;
        }
    }

    private static class TicsAnalyzerCmdTestCase {
        public TicsAnalyzer analyzer;
        public Platform platform;
        public String expectedResult;

        public TicsAnalyzerCmdTestCase(final TicsAnalyzer analyzer, final Platform platform, final String expectedResult) {
            this.analyzer = analyzer;
            this.platform = platform;
            this.expectedResult = expectedResult;
        }
    }

    private List<TicsAnalyzerCmdTestCase> getTicsAnalysisCmdEscapedTestCases() {
        final List<TicsAnalyzerCmdTestCase> testCases = new ArrayList<>();

        final TicsArguments windowsArgs = new TicsArguments("cpp game", "master branch", "D:\\Development\\dev_test\\projects\\cpp game", "D:\\Development\\dev_test\\tmp\\33733-tmpdir", "");
        final TicsArguments linuxArgs = new TicsArguments("cpp game", "master branch", "/home/leila/development/dev-test/projects/cpp game", "/home/leila/development/dev-test/tmp/33733-tmpdir", "");
        final TicsArguments noBranchAndTmpdirArgs = new TicsArguments("cpp-game", "", "", "", "");

        // Calc and Recalc
        testCases.add(new TicsAnalyzerCmdTestCase(getTicsAnalyzer(getMetrics(true, false, false, true), getMetrics(false, true, true, false), windowsArgs),
                Platform.Windows,
                "TICSQServer.exe -project 'cpp game' -branchname 'master branch' -branchdir 'D:\\Development\\dev_test\\projects\\cpp game' -tmpdir 'D:\\Development\\dev_test\\tmp\\33733-tmpdir' -calc CODINGSTANDARD,LOC -recalc COMPILERWARNING,FINALIZE"
        ));

        // Only Calc
        testCases.add(new TicsAnalyzerCmdTestCase(getTicsAnalyzer(getMetrics(true, false, false, true), getMetrics(false, false, false, false), windowsArgs),
                Platform.Windows,
                "TICSQServer.exe -project 'cpp game' -branchname 'master branch' -branchdir 'D:\\Development\\dev_test\\projects\\cpp game' -tmpdir 'D:\\Development\\dev_test\\tmp\\33733-tmpdir' -calc CODINGSTANDARD,LOC"
        ));

        // Only Recalc
        testCases.add(new TicsAnalyzerCmdTestCase(getTicsAnalyzer(getMetrics(false, false, false, false), getMetrics(false, true, true, false), windowsArgs),
                Platform.Windows,
                "TICSQServer.exe -project 'cpp game' -branchname 'master branch' -branchdir 'D:\\Development\\dev_test\\projects\\cpp game' -tmpdir 'D:\\Development\\dev_test\\tmp\\33733-tmpdir' -recalc COMPILERWARNING,FINALIZE"
        ));

        // No branch and no tmpdir
        testCases.add(new TicsAnalyzerCmdTestCase(getTicsAnalyzer(getMetrics(true, false, false, true), getMetrics(false, true, true, false), noBranchAndTmpdirArgs),
                Platform.Windows,
                "TICSQServer.exe -project 'cpp-game' -calc CODINGSTANDARD,LOC -recalc COMPILERWARNING,FINALIZE"
        ));

        // Calc and Recalc
        testCases.add(new TicsAnalyzerCmdTestCase(getTicsAnalyzer(getMetrics(true, false, false, true), getMetrics(false, true, true, false), linuxArgs),
                Platform.Linux,
                "TICSQServer -project 'cpp game' -branchname 'master branch' -branchdir '/home/leila/development/dev-test/projects/cpp game' -tmpdir '/home/leila/development/dev-test/tmp/33733-tmpdir' -calc CODINGSTANDARD,LOC -recalc COMPILERWARNING,FINALIZE"
        ));

        // Only Calc
        testCases.add(new TicsAnalyzerCmdTestCase(getTicsAnalyzer(getMetrics(true, false, false, true), getMetrics(false, false, false, false), linuxArgs),
                Platform.Linux,
                "TICSQServer -project 'cpp game' -branchname 'master branch' -branchdir '/home/leila/development/dev-test/projects/cpp game' -tmpdir '/home/leila/development/dev-test/tmp/33733-tmpdir' -calc CODINGSTANDARD,LOC"
        ));

        // Only Recalc
        testCases.add(new TicsAnalyzerCmdTestCase(getTicsAnalyzer(getMetrics(false, false, false, false), getMetrics(false, true, true, false), linuxArgs),
                Platform.Linux,
                "TICSQServer -project 'cpp game' -branchname 'master branch' -branchdir '/home/leila/development/dev-test/projects/cpp game' -tmpdir '/home/leila/development/dev-test/tmp/33733-tmpdir' -recalc COMPILERWARNING,FINALIZE"
        ));

        // No branch and no tmpdir
        testCases.add(new TicsAnalyzerCmdTestCase(getTicsAnalyzer(getMetrics(true, false, false, true), getMetrics(false, true, true, false), noBranchAndTmpdirArgs),
                Platform.Linux,
                "TICSQServer -project 'cpp-game' -calc CODINGSTANDARD,LOC -recalc COMPILERWARNING,FINALIZE"
        ));


        return testCases;
    }

    @Test
    public void testGetTicsAnalysis() {
        final EnvVars buildEnv = new EnvVars();

        for (final TicsAnalyzerCmdTestCase testCase : getTicsAnalysisCmdEscapedTestCases()) {
            final boolean isLauncherUnix = testCase.platform == Platform.Linux;
            final ImmutableList<String> ticsAnalysisCmd = testCase.analyzer.getTicsQServerArgs(buildEnv, isLauncherUnix);

            assertEquals(testCase.expectedResult, testCase.analyzer.getTicsAnalysisCmd(ticsAnalysisCmd));
        }
    }

    @Test
    public void testCreateCommandLinux() {
        final EnvVars buildEnv = new EnvVars();

        final TicsAnalyzer analyzer = getTicsAnalyzer(
                getMetrics(true, false, false, true),
                getMetrics(false, true, true, false),
                new TicsArguments("cpp-game", "main", ".", "/tmp/cpp game (TEST)", "-log 9 -viewer"));

        final ImmutableList<String> args = analyzer.getTicsQServerArgs(buildEnv, true);
        final String bootstrapCmd = analyzer.getBootstrapCmd(analyzer.ticsConfiguration, true);

        final String commandWithBootstrap = analyzer.createCommand(bootstrapCmd, args, true);
        assertEquals("bash -c \". <(curl --silent --show-error 'http://192.168.1.204:42506/tiobeweb/TICS/api/cfg?name=default') && TICSQServer -project 'cpp-game' -branchname 'main' -branchdir '.' -tmpdir '/tmp/cpp game (TEST)' -log 9 -viewer -calc CODINGSTANDARD,LOC -recalc COMPILERWARNING,FINALIZE\"", commandWithBootstrap);

        final String commandNoBootstrap = analyzer.createCommand("", args, true);
        assertEquals("bash -c \" TICSQServer -project 'cpp-game' -branchname 'main' -branchdir '.' -tmpdir '/tmp/cpp game (TEST)' -log 9 -viewer -calc CODINGSTANDARD,LOC -recalc COMPILERWARNING,FINALIZE\"", commandNoBootstrap);
    }

    @Test
    public void testCreateCommandWindows() {
        final EnvVars buildEnv = new EnvVars();

        final TicsAnalyzer analyzer = getTicsAnalyzer(
                getMetrics(true, false, false, true),
                getMetrics(false, true, true, false),
                new TicsArguments("cpp-game", "main", ".", "", ""));

        final ImmutableList<String> args = analyzer.getTicsQServerArgs(buildEnv, false);
        final String bootstrapCmd = analyzer.getBootstrapCmd(analyzer.ticsConfiguration, false);

        final String commandWithBootstrap = analyzer.createCommand(bootstrapCmd, args, false);
        assertEquals("powershell \"[System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('http://192.168.1.204:42506/tiobeweb/TICS/api/cfg?name=default')); if ($?) { TICSQServer.exe -project 'cpp-game' -branchname 'main' -branchdir '.' -calc CODINGSTANDARD,LOC -recalc COMPILERWARNING,FINALIZE }\"", commandWithBootstrap);

        final String commandNoBootstrap = analyzer.createCommand("", args, false);
        assertEquals("powershell \"; if ($?) { TICSQServer.exe -project 'cpp-game' -branchname 'main' -branchdir '.' -calc CODINGSTANDARD,LOC -recalc COMPILERWARNING,FINALIZE }\"", commandNoBootstrap);
    }
}
