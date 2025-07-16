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
    private static final String STRING = "http://192.168.1.204:42506/tiobeweb/TICS/api/cfg?name=default";
    private static final String ENVIRONMENT_VARIABLES = "";
    private static final boolean CREATE_TMPDIR = true;
    private static final boolean INSTALL_TICS = false;
    private static final String CREDENTIALS_ID = "auth-token";

    private static TicsAnalyzer getTicsAnalyzer(final Metrics calcMetrics, final Metrics recalcMetrics, final TicsArguments ticsArgs) {
        return new TicsAnalyzer(TICS_PATH
                , STRING
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

    private static Metrics getMetrics(final boolean codingStandards, final boolean compilerWarnings, final boolean finalize, final boolean loc) {
        return new Metrics(
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                codingStandards,
                compilerWarnings,
                false,
                false,
                false,
                false,
                finalize,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                loc,
                false,
                false,
                false,
                false,
                false,
                false
        );
    }

    enum Platform {
        LINUX,
        WINDOWS,
    }

    private record TicsArguments(String projectName, String branchName, String branchDirectory, String tmpdir,
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
        assertEquals("bash -c \". <(curl --silent --show-error 'http://192.168.1.204:42506/tiobeweb/TICS/api/cfg?name=default') && TICSQServer -project 'cpp-game' -branchname 'main' -branchdir '.' -tmpdir '/tmp/cpp game (TEST)' -log 9 -viewer -calc CODINGSTANDARD,LOC -recalc COMPILERWARNING,FINALIZE\"", commandWithBootstrap);

        final String commandNoBootstrap = analyzer.createCommand("", args, true);
        assertEquals("bash -c \" TICSQServer -project 'cpp-game' -branchname 'main' -branchdir '.' -tmpdir '/tmp/cpp game (TEST)' -log 9 -viewer -calc CODINGSTANDARD,LOC -recalc COMPILERWARNING,FINALIZE\"", commandNoBootstrap);
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
        assertEquals("powershell \"[System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('http://192.168.1.204:42506/tiobeweb/TICS/api/cfg?name=default')); if ($?) { TICSQServer.exe -project 'cpp-game' -branchname 'main' -branchdir '.' -calc CODINGSTANDARD,LOC -recalc COMPILERWARNING,FINALIZE }\"", commandWithBootstrap);

        final String commandNoBootstrap = analyzer.createCommand("", args, false);
        assertEquals("powershell \"; if ($?) { TICSQServer.exe -project 'cpp-game' -branchname 'main' -branchdir '.' -calc CODINGSTANDARD,LOC -recalc COMPILERWARNING,FINALIZE }\"", commandNoBootstrap);
    }
}
