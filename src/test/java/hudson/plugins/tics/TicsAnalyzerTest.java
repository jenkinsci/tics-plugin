package hudson.plugins.tics;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import hudson.EnvVars;
import hudson.util.ArgumentListBuilder;

public class TicsAnalyzerTest {

    public String ticsPath = "";
    public String ticsConfiguration = "http://192.168.1.204:42506/tiobeweb/TICS/api/cfg?name=default";
    public String environmentVariables = "";
    public boolean createTmpdir = true;
    public String extraArguments = "";
    public boolean installTics = false;
    public String credentialsId = "auth-token";


    private TicsAnalyzer getTicsAnalyzer(final Metrics calcMetrics, final Metrics recalcMetrics, final TicsArguments ticsArgs){
        return new TicsAnalyzer(ticsPath
                , ticsConfiguration
                , ticsArgs.projectName
                , ticsArgs.branchName
                , ticsArgs.branchDirectory
                , environmentVariables
                , createTmpdir
                , ticsArgs.tmpdir
                , extraArguments
                , calcMetrics
                , recalcMetrics
                , installTics
                , credentialsId);
    }

    private Metrics getMetrics(final boolean codingStandards, final boolean compilerWarnings, final boolean finalize, final boolean loc) {
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
        Linux,
        Windows,
    }

    private static class TicsArguments {
        public String projectName;
        public String branchName;
        public String branchDirectory;
        public String tmpdir;

        public TicsArguments(final String projectName, final String branchName, final String branchDirectory, final String tmpdir) {
            this.projectName = projectName;
            this.branchName = branchName;
            this.branchDirectory = branchDirectory;
            this.tmpdir = tmpdir;
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

        final TicsArguments windowsArgs = new TicsArguments("cpp-game-vs", "master", "D:\\Development\\dev_test\\projects\\cpp-game-vs", "D:\\Development\\dev_test\\tmp\\33733-tmpdir");
        final TicsArguments linuxArgs = new TicsArguments("game-gcc", "master", "/home/leila/development/dev-test/projects/game-gcc", "/home/leila/development/dev-test/tmp/33733-tmpdir");
        final TicsArguments noBranchAndTmpdirArgs = new TicsArguments("cpp-game", "", "", "");

        // Calc and Recalc
        testCases.add(new TicsAnalyzerCmdTestCase(getTicsAnalyzer(getMetrics(true, false, false, true), getMetrics(false, true, true, false), windowsArgs),
                Platform.Windows,
                "TICSQServer.exe -project cpp-game-vs -branchname master -branchdir D:\\Development\\dev_test\\projects\\cpp-game-vs -tmpdir D:\\Development\\dev_test\\tmp\\33733-tmpdir -calc CODINGSTANDARD,LOC -recalc COMPILERWARNING,FINALIZE"
        ));

        // Only Calc
        testCases.add(new TicsAnalyzerCmdTestCase(getTicsAnalyzer(getMetrics(true, false, false, true), getMetrics(false, false, false, false), windowsArgs),
                Platform.Windows,
                "TICSQServer.exe -project cpp-game-vs -branchname master -branchdir D:\\Development\\dev_test\\projects\\cpp-game-vs -tmpdir D:\\Development\\dev_test\\tmp\\33733-tmpdir -calc CODINGSTANDARD,LOC"
        ));

        // Only Recalc
        testCases.add(new TicsAnalyzerCmdTestCase(getTicsAnalyzer(getMetrics(false, false, false, false), getMetrics(false, true, true, false), windowsArgs),
                Platform.Windows,
                "TICSQServer.exe -project cpp-game-vs -branchname master -branchdir D:\\Development\\dev_test\\projects\\cpp-game-vs -tmpdir D:\\Development\\dev_test\\tmp\\33733-tmpdir -recalc COMPILERWARNING,FINALIZE"
        ));

        // No branch and no tmpdir
        testCases.add(new TicsAnalyzerCmdTestCase(getTicsAnalyzer(getMetrics(true, false, false, true), getMetrics(false, true, true, false), noBranchAndTmpdirArgs),
                Platform.Windows,
                "TICSQServer.exe -project cpp-game -calc CODINGSTANDARD,LOC -recalc COMPILERWARNING,FINALIZE"
        ));

        // Calc and Recalc
        testCases.add(new TicsAnalyzerCmdTestCase(getTicsAnalyzer(getMetrics(true, false, false, true), getMetrics(false, true, true, false), linuxArgs),
                Platform.Linux,
                "TICSQServer -project game-gcc -branchname master -branchdir /home/leila/development/dev-test/projects/game-gcc -tmpdir /home/leila/development/dev-test/tmp/33733-tmpdir -calc CODINGSTANDARD,LOC -recalc COMPILERWARNING,FINALIZE"
        ));

        // Only Calc
        testCases.add(new TicsAnalyzerCmdTestCase(getTicsAnalyzer(getMetrics(true, false, false, true), getMetrics(false, false, false, false), linuxArgs),
                Platform.Linux,
                "TICSQServer -project game-gcc -branchname master -branchdir /home/leila/development/dev-test/projects/game-gcc -tmpdir /home/leila/development/dev-test/tmp/33733-tmpdir -calc CODINGSTANDARD,LOC"
        ));

        // Only Recalc
        testCases.add(new TicsAnalyzerCmdTestCase(getTicsAnalyzer(getMetrics(false, false, false, false), getMetrics(false, true, true, false), linuxArgs),
                Platform.Linux,
                "TICSQServer -project game-gcc -branchname master -branchdir /home/leila/development/dev-test/projects/game-gcc -tmpdir /home/leila/development/dev-test/tmp/33733-tmpdir -recalc COMPILERWARNING,FINALIZE"
        ));

        // No branch and no tmpdir
        testCases.add(new TicsAnalyzerCmdTestCase(getTicsAnalyzer(getMetrics(true, false, false, true), getMetrics(false, true, true, false), noBranchAndTmpdirArgs),
                Platform.Linux,
                "TICSQServer -project cpp-game -calc CODINGSTANDARD,LOC -recalc COMPILERWARNING,FINALIZE"
        ));


        return testCases;
    }

    @Test
    public void testGetTicsAnalysisCmdEscaped() {
        final EnvVars buildEnv = new EnvVars();

        for (final TicsAnalyzerCmdTestCase testCase : getTicsAnalysisCmdEscapedTestCases()) {
            final boolean isLauncherUnix = testCase.platform == Platform.Linux;
            final ArgumentListBuilder ticsAnalysisCmd = testCase.analyzer.getTicsQServerArgs(buildEnv, isLauncherUnix);

            final String ticsAnalysisCmdEscaped;
            if (isLauncherUnix) {
                ticsAnalysisCmdEscaped = testCase.analyzer.getTicsAnalysisCmdEscapedLinux(ticsAnalysisCmd);
            } else {
                ticsAnalysisCmdEscaped = testCase.analyzer.getTicsAnalysisCmdEscapedWin(ticsAnalysisCmd);
            }

            assertEquals(testCase.expectedResult, ticsAnalysisCmdEscaped);
        }
    }

    @Test
    public void testCreateCommandLinux() {
        final EnvVars buildEnv = new EnvVars();

        final TicsAnalyzer analyzer = getTicsAnalyzer(
                getMetrics(true, false, false, true),
                getMetrics(false, true, true, false),
                new TicsArguments("cpp-game", "main", ".", ""));

        final ArgumentListBuilder args = analyzer.getTicsQServerArgs(buildEnv, true);
        final String bootstrapCmd = analyzer.getBootstrapCmd(analyzer.ticsConfiguration, true);

        final String commandWithBootstrap = analyzer.createCommand(bootstrapCmd, args, true);
        assertEquals("bash -c \". <(curl --silent --show-error 'http://192.168.1.204:42506/tiobeweb/TICS/api/cfg?name=default') && TICSQServer -project cpp-game -branchname main -branchdir . -calc CODINGSTANDARD,LOC -recalc COMPILERWARNING,FINALIZE\"", commandWithBootstrap);

        final String commandNoBootstrap = analyzer.createCommand("", args, true);
        assertEquals("bash -c \" TICSQServer -project cpp-game -branchname main -branchdir . -calc CODINGSTANDARD,LOC -recalc COMPILERWARNING,FINALIZE\"", commandNoBootstrap);
    }

    @Test
    public void testCreateCommandWindows() {
        final EnvVars buildEnv = new EnvVars();

        final TicsAnalyzer analyzer = getTicsAnalyzer(
                getMetrics(true, false, false, true),
                getMetrics(false, true, true, false),
                new TicsArguments("cpp-game", "main", ".", ""));

        final ArgumentListBuilder args = analyzer.getTicsQServerArgs(buildEnv, false);
        final String bootstrapCmd = analyzer.getBootstrapCmd(analyzer.ticsConfiguration, false);

        final String commandWithBootstrap = analyzer.createCommand(bootstrapCmd, args, false);
        assertEquals("powershell \"[System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('http://192.168.1.204:42506/tiobeweb/TICS/api/cfg?name=default')); if ($?) { TICSQServer.exe -project cpp-game -branchname main -branchdir . -calc CODINGSTANDARD,LOC -recalc COMPILERWARNING,FINALIZE }\"", commandWithBootstrap);

        final String commandNoBootstrap = analyzer.createCommand("", args, false);
        assertEquals("powershell \"; if ($?) { TICSQServer.exe -project cpp-game -branchname main -branchdir . -calc CODINGSTANDARD,LOC -recalc COMPILERWARNING,FINALIZE }\"", commandNoBootstrap);
    }

    @Test
    public void testRemoveDoubleQuoteFromCommand() {
        String ticsAnalysisCmd;
        String ticsAnalysisCmdExpected;
        final TicsAnalyzer ta = getTicsAnalyzer(null, null, new TicsArguments("", "", "", ""));
        ticsAnalysisCmd = "TICSQServer.exe -project game-gcc -branchname master -branchdir D:\\Development\\dev_test\\workspace\\tics-test-33733-UI -tmpdir D:\\Development\\dev_test\\tmp\\33733-tmpdir -calc \"CODINGSTANDARD,LOC\"";
        ticsAnalysisCmdExpected = "TICSQServer.exe -project game-gcc -branchname master -branchdir D:\\Development\\dev_test\\workspace\\tics-test-33733-UI -tmpdir D:\\Development\\dev_test\\tmp\\33733-tmpdir -calc CODINGSTANDARD,LOC";
        assertEquals(ticsAnalysisCmdExpected, ta.removeDoubleQuoteFromCommand("calc", ticsAnalysisCmd));

        ticsAnalysisCmd = "TICSQServer.exe -project cpp-game-vs -branchname master -branchdir D:\\Development\\dev_test\\projects\\cpp-game-vs -tmpdir D:\\Development\\dev_test\\tmp\\33733-tmpdir -calc \"CODINGSTANDARD,LOC\" -recalc \"COMPILERWARNING,FINALIZE\"";
        ticsAnalysisCmdExpected = "TICSQServer.exe -project cpp-game-vs -branchname master -branchdir D:\\Development\\dev_test\\projects\\cpp-game-vs -tmpdir D:\\Development\\dev_test\\tmp\\33733-tmpdir -calc CODINGSTANDARD,LOC -recalc COMPILERWARNING,FINALIZE";
        assertEquals(ticsAnalysisCmdExpected, ta.removeDoubleQuoteFromCommand("calc", ticsAnalysisCmd));

        ticsAnalysisCmd = "TICSQServer.exe -project cpp-game-vs -calc \"BEGIN,CODINGSTANDARD,LOC,ABSTRACTINTERPRETATION,SECURITY\" -recalc \"FINALIZE\"";
        ticsAnalysisCmdExpected = "TICSQServer.exe -project cpp-game-vs -calc BEGIN,CODINGSTANDARD,LOC,ABSTRACTINTERPRETATION,SECURITY -recalc FINALIZE";
        assertEquals(ticsAnalysisCmdExpected, ta.removeDoubleQuoteFromCommand("calc", ticsAnalysisCmd));
    }
}
