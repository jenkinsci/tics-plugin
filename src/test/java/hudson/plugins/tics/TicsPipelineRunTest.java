package hudson.plugins.tics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TicsPipelineRunTest {

    private static Metrics getMetrics(final boolean codingStandard, final boolean compilerWarning, final boolean begin, final boolean finalize, final boolean loc) {
        final boolean ABSTRACTINTERPRETATION = false;
        final boolean ACCUCHANGERATE = false;
        final boolean ACCUFIXRATE = false;
        final boolean ACCULINESADDED = false;
        final boolean ACCULINESCHANGED = false;
        final boolean ACCULINESDELETED = false;
        final boolean AI = false;
        final boolean ALL = false;
        final boolean AVGCYCLOMATICCOMPLEXITY = false;
        final boolean BEGIN = begin;
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

    static Stream<Arguments> parameters() {
        return Stream.of(
                Arguments.of(Arrays.asList("CODINGSTANDARD", "COMPILERWARNING"), getMetrics(true, true, false, false, false)),
                Arguments.of(Arrays.asList("BEGIN", "FINALIZE"), getMetrics(false, false, true, true, false)),
                Arguments.of(Arrays.asList("CODINGSTANDARD", "COMPILERWARNING", "BEGIN", "FINALIZE"), getMetrics(true, true, true, true, false))
        );
    }

    @ParameterizedTest
    @MethodSource("parameters")
    void testCreateMetricsObject(List<String> metricsList, Metrics expectedResult) {
        final TicsPipelineRun ticsPipeLineRun = new TicsPipelineRun("myProject", "myBranch");
        final Metrics createdMetricsObject = ticsPipeLineRun.createMetricsObject(metricsList);

        assertEquals(expectedResult.ABSTRACTINTERPRETATION, createdMetricsObject.ABSTRACTINTERPRETATION);
        assertEquals(expectedResult.ACCUCHANGERATE, createdMetricsObject.ACCUCHANGERATE);
        assertEquals(expectedResult.ACCUFIXRATE, createdMetricsObject.ACCUFIXRATE);
        assertEquals(expectedResult.ACCULINESADDED, createdMetricsObject.ACCULINESADDED);
        assertEquals(expectedResult.ACCULINESCHANGED, createdMetricsObject.ACCULINESCHANGED);
        assertEquals(expectedResult.ACCULINESDELETED, createdMetricsObject.ACCULINESDELETED);
        assertEquals(expectedResult.AI, createdMetricsObject.AI);
        assertEquals(expectedResult.ALL, createdMetricsObject.ALL);
        assertEquals(expectedResult.AVGCYCLOMATICCOMPLEXITY, createdMetricsObject.AVGCYCLOMATICCOMPLEXITY);
        assertEquals(expectedResult.BEGIN, createdMetricsObject.BEGIN);
        assertEquals(expectedResult.BUILDRELATIONS, createdMetricsObject.BUILDRELATIONS);
        assertEquals(expectedResult.CHANGEDFILES, createdMetricsObject.CHANGEDFILES);
        assertEquals(expectedResult.CHANGERATE, createdMetricsObject.CHANGERATE);
        assertEquals(expectedResult.CODINGSTANDARD, createdMetricsObject.CODINGSTANDARD);
        assertEquals(expectedResult.COMPILERWARNING, createdMetricsObject.COMPILERWARNING);
        assertEquals(expectedResult.CS, createdMetricsObject.CS);
        assertEquals(expectedResult.CW, createdMetricsObject.CW);
        assertEquals(expectedResult.CY, createdMetricsObject.CY);
        assertEquals(expectedResult.CYCLOMATICCOMPLEXITY, createdMetricsObject.CYCLOMATICCOMPLEXITY);
        assertEquals(expectedResult.DEADCODE, createdMetricsObject.DEADCODE);
        assertEquals(expectedResult.DUP, createdMetricsObject.DUP);
        assertEquals(expectedResult.DUPLICATEDCODE, createdMetricsObject.DUPLICATEDCODE);
        assertEquals(expectedResult.DUPLICATEDCODECUSTOM, createdMetricsObject.DUPLICATEDCODECUSTOM);
        assertEquals(expectedResult.ELOC, createdMetricsObject.ELOC);
        assertEquals(expectedResult.END, createdMetricsObject.END);
        assertEquals(expectedResult.FANOUT, createdMetricsObject.FANOUT);
        assertEquals(expectedResult.FANOUT_INTEXT, createdMetricsObject.FANOUT_INTEXT);
        assertEquals(expectedResult.FINALIZE, createdMetricsObject.FINALIZE);
        assertEquals(expectedResult.FIXRATE, createdMetricsObject.FIXRATE);
        assertEquals(expectedResult.GLOC, createdMetricsObject.GLOC);
        assertEquals(expectedResult.HIS_AVGCYCLOMATICCOMPLEXITY, createdMetricsObject.HIS_AVGCYCLOMATICCOMPLEXITY);
        assertEquals(expectedResult.HIS_CALLLEVELS, createdMetricsObject.HIS_CALLLEVELS);
        assertEquals(expectedResult.HIS_CALLLEVELS_MAX, createdMetricsObject.HIS_CALLLEVELS_MAX);
        assertEquals(expectedResult.HIS_CODINGSTANDARD, createdMetricsObject.HIS_CODINGSTANDARD);
        assertEquals(expectedResult.HIS_COMMENTDENSITY, createdMetricsObject.HIS_COMMENTDENSITY);
        assertEquals(expectedResult.HIS_FUNCCALLS, createdMetricsObject.HIS_FUNCCALLS);
        assertEquals(expectedResult.HIS_FUNCCALLS_MAX, createdMetricsObject.HIS_FUNCCALLS_MAX);
        assertEquals(expectedResult.HIS_FUNCCYCLE, createdMetricsObject.HIS_FUNCCYCLE);
        assertEquals(expectedResult.HIS_FUNCCYCLE_MAX, createdMetricsObject.HIS_FUNCCYCLE_MAX);
        assertEquals(expectedResult.HIS_FUNCSIZE, createdMetricsObject.HIS_FUNCSIZE);
        assertEquals(expectedResult.HIS_FUNCSIZE_MAX, createdMetricsObject.HIS_FUNCSIZE_MAX);
        assertEquals(expectedResult.HIS_GOTOSTATEMENTS, createdMetricsObject.HIS_GOTOSTATEMENTS);
        assertEquals(expectedResult.HIS_GOTOSTATEMENTS_MAX, createdMetricsObject.HIS_GOTOSTATEMENTS_MAX);
        assertEquals(expectedResult.HIS_MAXCYCLOMATICCOMPLEXITY, createdMetricsObject.HIS_MAXCYCLOMATICCOMPLEXITY);
        assertEquals(expectedResult.HIS_PARAMCOUNT, createdMetricsObject.HIS_PARAMCOUNT);
        assertEquals(expectedResult.HIS_PARAMCOUNT_MAX, createdMetricsObject.HIS_PARAMCOUNT_MAX);
        assertEquals(expectedResult.HIS_PATHCOUNT, createdMetricsObject.HIS_PATHCOUNT);
        assertEquals(expectedResult.HIS_PATHCOUNT_MAX, createdMetricsObject.HIS_PATHCOUNT_MAX);
        assertEquals(expectedResult.HIS_RETURNPOINTS, createdMetricsObject.HIS_RETURNPOINTS);
        assertEquals(expectedResult.HIS_RETURNPOINTS_MAX, createdMetricsObject.HIS_RETURNPOINTS_MAX);
        assertEquals(expectedResult.HIS_VOCF, createdMetricsObject.HIS_VOCF);
        assertEquals(expectedResult.INCLUDERELATIONS, createdMetricsObject.INCLUDERELATIONS);
        assertEquals(expectedResult.INTEGRATIONBRANCHCOVERAGE, createdMetricsObject.INTEGRATIONBRANCHCOVERAGE);
        assertEquals(expectedResult.INTEGRATIONDECISIONCOVERAGE, createdMetricsObject.INTEGRATIONDECISIONCOVERAGE);
        assertEquals(expectedResult.INTEGRATIONFUNCTIONCOVERAGE, createdMetricsObject.INTEGRATIONFUNCTIONCOVERAGE);
        assertEquals(expectedResult.INTEGRATIONSTATEMENTCOVERAGE, createdMetricsObject.INTEGRATIONSTATEMENTCOVERAGE);
        assertEquals(expectedResult.INTEGRATIONTESTCOVERAGE, createdMetricsObject.INTEGRATIONTESTCOVERAGE);
        assertEquals(expectedResult.ITC, createdMetricsObject.ITC);
        assertEquals(expectedResult.LINESADDED, createdMetricsObject.LINESADDED);
        assertEquals(expectedResult.LINESCHANGED, createdMetricsObject.LINESCHANGED);
        assertEquals(expectedResult.LINESDELETED, createdMetricsObject.LINESDELETED);
        assertEquals(expectedResult.LOC, createdMetricsObject.LOC);
        assertEquals(expectedResult.MAXCYCLOMATICCOMPLEXITY, createdMetricsObject.MAXCYCLOMATICCOMPLEXITY);
        assertEquals(expectedResult.PATHCOUNT, createdMetricsObject.PATHCOUNT);
        assertEquals(expectedResult.PATHCOUNT_MAX, createdMetricsObject.PATHCOUNT_MAX);
        assertEquals(expectedResult.POSTANA, createdMetricsObject.POSTANA);
        assertEquals(expectedResult.PREPARE, createdMetricsObject.PREPARE);
        assertEquals(expectedResult.SEC, createdMetricsObject.SEC);
        assertEquals(expectedResult.SECURITY, createdMetricsObject.SECURITY);
        assertEquals(expectedResult.STC, createdMetricsObject.STC);
        assertEquals(expectedResult.SYSTEMBRANCHCOVERAGE, createdMetricsObject.SYSTEMBRANCHCOVERAGE);
        assertEquals(expectedResult.SYSTEMDECISIONCOVERAGE, createdMetricsObject.SYSTEMDECISIONCOVERAGE);
        assertEquals(expectedResult.SYSTEMFUNCTIONCOVERAGE, createdMetricsObject.SYSTEMFUNCTIONCOVERAGE);
        assertEquals(expectedResult.SYSTEMSTATEMENTCOVERAGE, createdMetricsObject.SYSTEMSTATEMENTCOVERAGE);
        assertEquals(expectedResult.SYSTEMTESTCOVERAGE, createdMetricsObject.SYSTEMTESTCOVERAGE);
        assertEquals(expectedResult.TOTALBRANCHCOVERAGE, createdMetricsObject.TOTALBRANCHCOVERAGE);
        assertEquals(expectedResult.TOTALDECISIONCOVERAGE, createdMetricsObject.TOTALDECISIONCOVERAGE);
        assertEquals(expectedResult.TOTALFUNCTIONCOVERAGE, createdMetricsObject.TOTALFUNCTIONCOVERAGE);
        assertEquals(expectedResult.TOTALSTATEMENTCOVERAGE, createdMetricsObject.TOTALSTATEMENTCOVERAGE);
        assertEquals(expectedResult.TOTALTESTCOVERAGE, createdMetricsObject.TOTALTESTCOVERAGE);
        assertEquals(expectedResult.TTC, createdMetricsObject.TTC);
        assertEquals(expectedResult.UNITBRANCHCOVERAGE, createdMetricsObject.UNITBRANCHCOVERAGE);
        assertEquals(expectedResult.UNITDECISIONCOVERAGE, createdMetricsObject.UNITDECISIONCOVERAGE);
        assertEquals(expectedResult.UNITFUNCTIONCOVERAGE, createdMetricsObject.UNITFUNCTIONCOVERAGE);
        assertEquals(expectedResult.UNITSTATEMENTCOVERAGE, createdMetricsObject.UNITSTATEMENTCOVERAGE);
        assertEquals(expectedResult.UNITTESTCOVERAGE, createdMetricsObject.UNITTESTCOVERAGE);
        assertEquals(expectedResult.UTC, createdMetricsObject.UTC);
    }

    @Test
    void testCreateMetricObjectThrowingException() {
        final TicsPipelineRun ticsPipeLineRun = new TicsPipelineRun("myProject", "myBranch");
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> ticsPipeLineRun.createMetricsObject(List.of("RANDOM")));

        assertEquals("The following metrics are incorrect: [RANDOM]. \n"
                + "The available metrics are: ABSTRACTINTERPRETATION, ACCUCHANGERATE, "
                + "ACCUFIXRATE, ACCULINESADDED, ACCULINESCHANGED, ACCULINESDELETED, AI, "
                + "ALL, AVGCYCLOMATICCOMPLEXITY, BEGIN, BUILDRELATIONS, CHANGEDFILES, "
                + "CHANGERATE, CODINGSTANDARD, COMPILERWARNING, CS, CW, CY, CYCLOMATICCOMPLEXITY, "
                + "DEADCODE, DUP, DUPLICATEDCODE, DUPLICATEDCODECUSTOM, ELOC, END, FANOUT, FANOUT_INTEXT, "
                + "FINALIZE, FIXRATE, GLOC, HIS_AVGCYCLOMATICCOMPLEXITY, HIS_CALLLEVELS, HIS_CALLLEVELS_MAX, "
                + "HIS_CODINGSTANDARD, HIS_COMMENTDENSITY, HIS_FUNCCALLS, HIS_FUNCCALLS_MAX, HIS_FUNCCYCLE, "
                + "HIS_FUNCCYCLE_MAX, HIS_FUNCSIZE, HIS_FUNCSIZE_MAX, HIS_GOTOSTATEMENTS, HIS_GOTOSTATEMENTS_MAX, "
                + "HIS_MAXCYCLOMATICCOMPLEXITY, HIS_PARAMCOUNT, HIS_PARAMCOUNT_MAX, HIS_PATHCOUNT, HIS_PATHCOUNT_MAX, "
                + "HIS_RETURNPOINTS, HIS_RETURNPOINTS_MAX, HIS_VOCF, INCLUDERELATIONS, INTEGRATIONBRANCHCOVERAGE, "
                + "INTEGRATIONDECISIONCOVERAGE, INTEGRATIONFUNCTIONCOVERAGE, INTEGRATIONSTATEMENTCOVERAGE, "
                + "INTEGRATIONTESTCOVERAGE, ITC, LINESADDED, LINESCHANGED, LINESDELETED, LOC, MAXCYCLOMATICCOMPLEXITY, "
                + "PATHCOUNT, PATHCOUNT_MAX, POSTANA, PREPARE, SEC, SECURITY, STC, SYSTEMBRANCHCOVERAGE, "
                + "SYSTEMDECISIONCOVERAGE, SYSTEMFUNCTIONCOVERAGE, SYSTEMSTATEMENTCOVERAGE, SYSTEMTESTCOVERAGE, "
                + "TOTALBRANCHCOVERAGE, TOTALDECISIONCOVERAGE, TOTALFUNCTIONCOVERAGE, TOTALSTATEMENTCOVERAGE, TOTALTESTCOVERAGE, "
                + "TTC, UNITBRANCHCOVERAGE, UNITDECISIONCOVERAGE, UNITFUNCTIONCOVERAGE, UNITSTATEMENTCOVERAGE, "
                + "UNITTESTCOVERAGE, UTC", exception.getMessage());
    }
}
