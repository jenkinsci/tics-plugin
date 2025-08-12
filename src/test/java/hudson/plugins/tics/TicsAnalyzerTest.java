package hudson.plugins.tics;

import com.google.common.collect.ImmutableList;
import hudson.EnvVars;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TicsAnalyzerTest {

    private static final String TICS_PATH = "";
    private static final String TICS_CONFIGURATION = "http://192.168.1.204:42506/tiobeweb/TICS/api/cfg?name=default";
    private static final String ENVIRONMENT_VARIABLES = "";
    private static final boolean CREATE_TMPDIR = true;
    private static final boolean INSTALL_TICS = false;
    private static final String CREDENTIALS_ID = "auth-token";


    private static TicsAnalyzer getTicsAnalyzer(
            final Metrics calcMetrics,
            final Metrics recalcMetrics,
            final TicsArguments ticsArgs
    ) {
        return new TicsAnalyzer(ticsPath
                , TICS_CONFIGURATION
                , ticsArgs.projectName
                , ticsArgs.branchName
                , ticsArgs.branchDirectory
                , ENVIRONMENT_VARIABLES
                , CREATE_TMPDIR
                , ticsArgs.tmpdir
                , ticsArgs.extraArguments
                , calcMetrics
                , recalcMetrics
                , INSTALL_TICS
                , CREDENTIALS_ID);
    }

    private static Metrics getMetrics(
            final boolean codingStandard,
            final boolean compilerWarning,
            final boolean finalize,
            final boolean loc
    ) {
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
        LINUX,
        WINDOWS,
    }

    private record TicsArguments(
            String projectName,
            String branchName,
            String branchDirectory,
            String tmpdir,
            String extraArguments) {
    }

    private static Stream<Arguments> parameters() {
        final TicsArguments windowsArgs = new TicsArguments("cpp game", "master branch", "D:\\Development\\dev_test\\projects\\cpp game", "D:\\Development\\dev_test\\tmp\\33733-tmpdir", "");
        final TicsArguments linuxArgs = new TicsArguments("cpp game", "master branch", "/home/leila/development/dev-test/projects/cpp game", "/home/leila/development/dev-test/tmp/33733-tmpdir", "");
        final TicsArguments noBranchAndTmpdirArgs = new TicsArguments("cpp-game", "", "", "", "");

        return Stream.of(
                // Calc and Recalc
                Arguments.of(getTicsAnalyzer(getMetrics(true, false, false, true), getMetrics(false, true, true, false), windowsArgs),
                        Platform.WINDOWS,
                        "TICSQServer.exe -project 'cpp game' -branchname 'master branch' -branchdir 'D:\\Development\\dev_test\\projects\\cpp game' -tmpdir 'D:\\Development\\dev_test\\tmp\\33733-tmpdir' -calc CODINGSTANDARD,LOC -recalc COMPILERWARNING,FINALIZE"
                ),

                // Only Calc
                Arguments.of(getTicsAnalyzer(getMetrics(true, false, false, true), getMetrics(false, false, false, false), windowsArgs),
                        Platform.WINDOWS,
                        "TICSQServer.exe -project 'cpp game' -branchname 'master branch' -branchdir 'D:\\Development\\dev_test\\projects\\cpp game' -tmpdir 'D:\\Development\\dev_test\\tmp\\33733-tmpdir' -calc CODINGSTANDARD,LOC"
                ),

                // Only Recalc
                Arguments.of(getTicsAnalyzer(getMetrics(false, false, false, false), getMetrics(false, true, true, false), windowsArgs),
                        Platform.WINDOWS,
                        "TICSQServer.exe -project 'cpp game' -branchname 'master branch' -branchdir 'D:\\Development\\dev_test\\projects\\cpp game' -tmpdir 'D:\\Development\\dev_test\\tmp\\33733-tmpdir' -recalc COMPILERWARNING,FINALIZE"
                ),

                // No branch and no tmpdir
                Arguments.of(getTicsAnalyzer(getMetrics(true, false, false, true), getMetrics(false, true, true, false), noBranchAndTmpdirArgs),
                        Platform.WINDOWS,
                        "TICSQServer.exe -project 'cpp-game' -calc CODINGSTANDARD,LOC -recalc COMPILERWARNING,FINALIZE"
                ),

                // Calc and Recalc
                Arguments.of(getTicsAnalyzer(getMetrics(true, false, false, true), getMetrics(false, true, true, false), linuxArgs),
                        Platform.LINUX,
                        "TICSQServer -project 'cpp game' -branchname 'master branch' -branchdir '/home/leila/development/dev-test/projects/cpp game' -tmpdir '/home/leila/development/dev-test/tmp/33733-tmpdir' -calc CODINGSTANDARD,LOC -recalc COMPILERWARNING,FINALIZE"
                ),

                // Only Calc
                Arguments.of(getTicsAnalyzer(getMetrics(true, false, false, true), getMetrics(false, false, false, false), linuxArgs),
                        Platform.LINUX,
                        "TICSQServer -project 'cpp game' -branchname 'master branch' -branchdir '/home/leila/development/dev-test/projects/cpp game' -tmpdir '/home/leila/development/dev-test/tmp/33733-tmpdir' -calc CODINGSTANDARD,LOC"
                ),

                // Only Recalc
                Arguments.of(getTicsAnalyzer(getMetrics(false, false, false, false), getMetrics(false, true, true, false), linuxArgs),
                        Platform.LINUX,
                        "TICSQServer -project 'cpp game' -branchname 'master branch' -branchdir '/home/leila/development/dev-test/projects/cpp game' -tmpdir '/home/leila/development/dev-test/tmp/33733-tmpdir' -recalc COMPILERWARNING,FINALIZE"
                ),

                // No branch and no tmpdir
                Arguments.of(getTicsAnalyzer(getMetrics(true, false, false, true), getMetrics(false, true, true, false), noBranchAndTmpdirArgs),
                        Platform.LINUX,
                        "TICSQServer -project 'cpp-game' -calc CODINGSTANDARD,LOC -recalc COMPILERWARNING,FINALIZE"
                ));
    }

    @ParameterizedTest
    @MethodSource("parameters")
    void testGetTicsAnalysis(final TicsAnalyzer analyzer, final Platform platform, final String expectedResult) {
        final EnvVars buildEnv = new EnvVars();

        final boolean isLauncherUnix = platform == Platform.LINUX;
        final ImmutableList<String> ticsAnalysisCmd = analyzer.getTicsQServerArgs(buildEnv, isLauncherUnix);
        assertEquals(expectedResult, analyzer.getTicsAnalysisCmd(ticsAnalysisCmd));
    }

    @Test
    void testCreateCommandLinux() {
        final EnvVars buildEnv = new EnvVars();

        final TicsAnalyzer analyzer = getTicsAnalyzer(
                getMetrics(true, false, false, true),
                getMetrics(false, true, true, false),
                new TicsArguments("cpp-game", "main", ".", "/tmp/cpp game (TEST)", "-log 9 -viewer"));

        final ImmutableList<String> args = analyzer.getTicsQServerArgs(buildEnv, true);
        final String bootstrapCmd = analyzer.getBootstrapCmd(analyzer.ticsConfiguration, true);

        final String commandWithBootstrap = analyzer.createCommand(bootstrapCmd, args, true);
        assertEquals("bash -c \". <(curl --silent --show-error 'http://192.168.1" +
                ".204:42506/tiobeweb/TICS/api/cfg?name=default') && TICSQServer -project 'cpp-game' -branchname " +
                "'main'" +
                " -branchdir '.' -tmpdir '/tmp/cpp game (TEST)' -log 9 -viewer -calc CODINGSTANDARD,LOC -recalc " +
                "COMPILERWARNING,FINALIZE\"", commandWithBootstrap);

        final String commandNoBootstrap = analyzer.createCommand("", args, true);
        assertEquals("bash -c \" TICSQServer -project 'cpp-game' -branchname 'main' -branchdir '.' -tmpdir '/tmp/cpp " +
                        "game (TEST)' -log 9 -viewer -calc CODINGSTANDARD,LOC -recalc COMPILERWARNING,FINALIZE\"",
                commandNoBootstrap);
    }

    @Test
    void testCreateCommandWindows() {
        final EnvVars buildEnv = new EnvVars();

        final TicsAnalyzer analyzer = getTicsAnalyzer(
                getMetrics(true, false, false, true),
                getMetrics(false, true, true, false),
                new TicsArguments("cpp-game", "main", ".", "", ""));

        final ImmutableList<String> args = analyzer.getTicsQServerArgs(buildEnv, false);
        final String bootstrapCmd = analyzer.getBootstrapCmd(analyzer.ticsConfiguration, false);

        final String commandWithBootstrap = analyzer.createCommand(bootstrapCmd, args, false);
        assertEquals("powershell \"[System.Net.ServicePointManager]::SecurityProtocol = [System.Net" +
                ".ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient)" +
                ".DownloadString('http://192.168.1.204:42506/tiobeweb/TICS/api/cfg?name=default')); if ($?) { " +
                "TICSQServer.exe -project 'cpp-game' -branchname 'main' -branchdir '.' -calc CODINGSTANDARD,LOC " +
                "-recalc COMPILERWARNING,FINALIZE }\"", commandWithBootstrap);

        final String commandNoBootstrap = analyzer.createCommand("", args, false);
        assertEquals("powershell \"; if ($?) { TICSQServer.exe -project 'cpp-game' -branchname 'main' -branchdir '.' " +
                "-calc CODINGSTANDARD,LOC -recalc COMPILERWARNING,FINALIZE }\"", commandNoBootstrap);
    }
}
