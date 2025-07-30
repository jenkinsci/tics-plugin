package hudson.plugins.tics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class TicsPipelineRunTest {

    private class CreateMetricsObjectTestCase {
        public List<String> metricsList;
        public Metrics expectedResult;
        public CreateMetricsObjectTestCase(List<String> metrics, Metrics createdMetricsObject) {
            this.metricsList = metrics;
            this.expectedResult = createdMetricsObject;
        }
    }


    private List<CreateMetricsObjectTestCase> getCreateMetricsObjectTestCases() {
        final List<CreateMetricsObjectTestCase> testCases = new ArrayList<>();
        
        testCases.add(new CreateMetricsObjectTestCase(Arrays.asList("CODINGSTANDARD", "COMPILERWARNING"), getMetrics(true, true, false, false, false)));
        testCases.add(new CreateMetricsObjectTestCase(Arrays.asList("BEGIN", "FINALIZE"), getMetrics(false, false, true, true, false)));
        testCases.add(new CreateMetricsObjectTestCase(Arrays.asList("CODINGSTANDARD", "COMPILERWARNING", "BEGIN", "FINALIZE"), getMetrics(true, true, true, true, false)));
   
        return testCases;
    }

    
    private Metrics getMetrics(final boolean codingStandard, final boolean compilerWarning, final boolean begin, final boolean finalize, final boolean loc) {
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


    @Test
    public void testCreateMetricsObject() {
        // Test cases where no exception is thrown
    	final TicsPipelineRun ticsPipeLineRun = new TicsPipelineRun("myProject", "myBranch");
        for (CreateMetricsObjectTestCase testCase : getCreateMetricsObjectTestCases()) {
        	Metrics createdMetricsObject = ticsPipeLineRun.createMetricsObject(testCase.metricsList);

        	assertEquals(testCase.expectedResult.ABSTRACTINTERPRETATION, createdMetricsObject.ABSTRACTINTERPRETATION);
        	assertEquals(testCase.expectedResult.ACCUCHANGERATE, createdMetricsObject.ACCUCHANGERATE);
        	assertEquals(testCase.expectedResult.ACCUFIXRATE, createdMetricsObject.ACCUFIXRATE);
        	assertEquals(testCase.expectedResult.ACCULINESADDED, createdMetricsObject.ACCULINESADDED);
        	assertEquals(testCase.expectedResult.ACCULINESCHANGED, createdMetricsObject.ACCULINESCHANGED);
        	assertEquals(testCase.expectedResult.ACCULINESDELETED, createdMetricsObject.ACCULINESDELETED);
        	assertEquals(testCase.expectedResult.AI, createdMetricsObject.AI);
        	assertEquals(testCase.expectedResult.ALL, createdMetricsObject.ALL);
        	assertEquals(testCase.expectedResult.AVGCYCLOMATICCOMPLEXITY, createdMetricsObject.AVGCYCLOMATICCOMPLEXITY);
        	assertEquals(testCase.expectedResult.BEGIN, createdMetricsObject.BEGIN);
        	assertEquals(testCase.expectedResult.BUILDRELATIONS, createdMetricsObject.BUILDRELATIONS);
        	assertEquals(testCase.expectedResult.CHANGEDFILES, createdMetricsObject.CHANGEDFILES);
        	assertEquals(testCase.expectedResult.CHANGERATE, createdMetricsObject.CHANGERATE);
        	assertEquals(testCase.expectedResult.CODINGSTANDARD, createdMetricsObject.CODINGSTANDARD);
        	assertEquals(testCase.expectedResult.COMPILERWARNING, createdMetricsObject.COMPILERWARNING);
        	assertEquals(testCase.expectedResult.CS, createdMetricsObject.CS);
        	assertEquals(testCase.expectedResult.CW, createdMetricsObject.CW);
        	assertEquals(testCase.expectedResult.CY, createdMetricsObject.CY);
        	assertEquals(testCase.expectedResult.CYCLOMATICCOMPLEXITY, createdMetricsObject.CYCLOMATICCOMPLEXITY);
        	assertEquals(testCase.expectedResult.DEADCODE, createdMetricsObject.DEADCODE);
        	assertEquals(testCase.expectedResult.DUP, createdMetricsObject.DUP);
        	assertEquals(testCase.expectedResult.DUPLICATEDCODE, createdMetricsObject.DUPLICATEDCODE);
        	assertEquals(testCase.expectedResult.DUPLICATEDCODECUSTOM, createdMetricsObject.DUPLICATEDCODECUSTOM);
        	assertEquals(testCase.expectedResult.ELOC, createdMetricsObject.ELOC);
        	assertEquals(testCase.expectedResult.END, createdMetricsObject.END);
        	assertEquals(testCase.expectedResult.FANOUT, createdMetricsObject.FANOUT);
        	assertEquals(testCase.expectedResult.FANOUT_INTEXT, createdMetricsObject.FANOUT_INTEXT);
        	assertEquals(testCase.expectedResult.FINALIZE, createdMetricsObject.FINALIZE);
        	assertEquals(testCase.expectedResult.FIXRATE, createdMetricsObject.FIXRATE);
        	assertEquals(testCase.expectedResult.GLOC, createdMetricsObject.GLOC);
        	assertEquals(testCase.expectedResult.HIS_AVGCYCLOMATICCOMPLEXITY, createdMetricsObject.HIS_AVGCYCLOMATICCOMPLEXITY);
        	assertEquals(testCase.expectedResult.HIS_CALLLEVELS, createdMetricsObject.HIS_CALLLEVELS);
        	assertEquals(testCase.expectedResult.HIS_CALLLEVELS_MAX, createdMetricsObject.HIS_CALLLEVELS_MAX);
        	assertEquals(testCase.expectedResult.HIS_CODINGSTANDARD, createdMetricsObject.HIS_CODINGSTANDARD);
        	assertEquals(testCase.expectedResult.HIS_COMMENTDENSITY, createdMetricsObject.HIS_COMMENTDENSITY);
        	assertEquals(testCase.expectedResult.HIS_FUNCCALLS, createdMetricsObject.HIS_FUNCCALLS);
        	assertEquals(testCase.expectedResult.HIS_FUNCCALLS_MAX, createdMetricsObject.HIS_FUNCCALLS_MAX);
        	assertEquals(testCase.expectedResult.HIS_FUNCCYCLE, createdMetricsObject.HIS_FUNCCYCLE);
        	assertEquals(testCase.expectedResult.HIS_FUNCCYCLE_MAX, createdMetricsObject.HIS_FUNCCYCLE_MAX);
        	assertEquals(testCase.expectedResult.HIS_FUNCSIZE, createdMetricsObject.HIS_FUNCSIZE);
        	assertEquals(testCase.expectedResult.HIS_FUNCSIZE_MAX, createdMetricsObject.HIS_FUNCSIZE_MAX);
        	assertEquals(testCase.expectedResult.HIS_GOTOSTATEMENTS, createdMetricsObject.HIS_GOTOSTATEMENTS);
        	assertEquals(testCase.expectedResult.HIS_GOTOSTATEMENTS_MAX, createdMetricsObject.HIS_GOTOSTATEMENTS_MAX);
        	assertEquals(testCase.expectedResult.HIS_MAXCYCLOMATICCOMPLEXITY, createdMetricsObject.HIS_MAXCYCLOMATICCOMPLEXITY);
        	assertEquals(testCase.expectedResult.HIS_PARAMCOUNT, createdMetricsObject.HIS_PARAMCOUNT);
        	assertEquals(testCase.expectedResult.HIS_PARAMCOUNT_MAX, createdMetricsObject.HIS_PARAMCOUNT_MAX);
        	assertEquals(testCase.expectedResult.HIS_PATHCOUNT, createdMetricsObject.HIS_PATHCOUNT);
        	assertEquals(testCase.expectedResult.HIS_PATHCOUNT_MAX, createdMetricsObject.HIS_PATHCOUNT_MAX);
        	assertEquals(testCase.expectedResult.HIS_RETURNPOINTS, createdMetricsObject.HIS_RETURNPOINTS);
        	assertEquals(testCase.expectedResult.HIS_RETURNPOINTS_MAX, createdMetricsObject.HIS_RETURNPOINTS_MAX);
        	assertEquals(testCase.expectedResult.HIS_VOCF, createdMetricsObject.HIS_VOCF);
        	assertEquals(testCase.expectedResult.INCLUDERELATIONS, createdMetricsObject.INCLUDERELATIONS);
        	assertEquals(testCase.expectedResult.INTEGRATIONBRANCHCOVERAGE, createdMetricsObject.INTEGRATIONBRANCHCOVERAGE);
        	assertEquals(testCase.expectedResult.INTEGRATIONDECISIONCOVERAGE, createdMetricsObject.INTEGRATIONDECISIONCOVERAGE);
        	assertEquals(testCase.expectedResult.INTEGRATIONFUNCTIONCOVERAGE, createdMetricsObject.INTEGRATIONFUNCTIONCOVERAGE);
        	assertEquals(testCase.expectedResult.INTEGRATIONSTATEMENTCOVERAGE, createdMetricsObject.INTEGRATIONSTATEMENTCOVERAGE);
        	assertEquals(testCase.expectedResult.INTEGRATIONTESTCOVERAGE, createdMetricsObject.INTEGRATIONTESTCOVERAGE);
        	assertEquals(testCase.expectedResult.ITC, createdMetricsObject.ITC);
        	assertEquals(testCase.expectedResult.LINESADDED, createdMetricsObject.LINESADDED);
        	assertEquals(testCase.expectedResult.LINESCHANGED, createdMetricsObject.LINESCHANGED);
        	assertEquals(testCase.expectedResult.LINESDELETED, createdMetricsObject.LINESDELETED);
        	assertEquals(testCase.expectedResult.LOC, createdMetricsObject.LOC);
        	assertEquals(testCase.expectedResult.MAXCYCLOMATICCOMPLEXITY, createdMetricsObject.MAXCYCLOMATICCOMPLEXITY);
        	assertEquals(testCase.expectedResult.PATHCOUNT, createdMetricsObject.PATHCOUNT);
        	assertEquals(testCase.expectedResult.PATHCOUNT_MAX, createdMetricsObject.PATHCOUNT_MAX);
        	assertEquals(testCase.expectedResult.POSTANA, createdMetricsObject.POSTANA);
        	assertEquals(testCase.expectedResult.PREPARE, createdMetricsObject.PREPARE);
        	assertEquals(testCase.expectedResult.SEC, createdMetricsObject.SEC);
        	assertEquals(testCase.expectedResult.SECURITY, createdMetricsObject.SECURITY);
        	assertEquals(testCase.expectedResult.STC, createdMetricsObject.STC);
        	assertEquals(testCase.expectedResult.SYSTEMBRANCHCOVERAGE, createdMetricsObject.SYSTEMBRANCHCOVERAGE);
        	assertEquals(testCase.expectedResult.SYSTEMDECISIONCOVERAGE, createdMetricsObject.SYSTEMDECISIONCOVERAGE);
        	assertEquals(testCase.expectedResult.SYSTEMFUNCTIONCOVERAGE, createdMetricsObject.SYSTEMFUNCTIONCOVERAGE);
        	assertEquals(testCase.expectedResult.SYSTEMSTATEMENTCOVERAGE, createdMetricsObject.SYSTEMSTATEMENTCOVERAGE);
        	assertEquals(testCase.expectedResult.SYSTEMTESTCOVERAGE, createdMetricsObject.SYSTEMTESTCOVERAGE);
        	assertEquals(testCase.expectedResult.TOTALBRANCHCOVERAGE, createdMetricsObject.TOTALBRANCHCOVERAGE);
        	assertEquals(testCase.expectedResult.TOTALDECISIONCOVERAGE, createdMetricsObject.TOTALDECISIONCOVERAGE);
        	assertEquals(testCase.expectedResult.TOTALFUNCTIONCOVERAGE, createdMetricsObject.TOTALFUNCTIONCOVERAGE);
        	assertEquals(testCase.expectedResult.TOTALSTATEMENTCOVERAGE, createdMetricsObject.TOTALSTATEMENTCOVERAGE);
        	assertEquals(testCase.expectedResult.TOTALTESTCOVERAGE, createdMetricsObject.TOTALTESTCOVERAGE);
        	assertEquals(testCase.expectedResult.TTC, createdMetricsObject.TTC);
        	assertEquals(testCase.expectedResult.UNITBRANCHCOVERAGE, createdMetricsObject.UNITBRANCHCOVERAGE);
        	assertEquals(testCase.expectedResult.UNITDECISIONCOVERAGE, createdMetricsObject.UNITDECISIONCOVERAGE);
        	assertEquals(testCase.expectedResult.UNITFUNCTIONCOVERAGE, createdMetricsObject.UNITFUNCTIONCOVERAGE);
        	assertEquals(testCase.expectedResult.UNITSTATEMENTCOVERAGE, createdMetricsObject.UNITSTATEMENTCOVERAGE);
        	assertEquals(testCase.expectedResult.UNITTESTCOVERAGE, createdMetricsObject.UNITTESTCOVERAGE);
        	assertEquals(testCase.expectedResult.UTC, createdMetricsObject.UTC);
        }

        // Test case where an exception is thrown
       
        final IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ticsPipeLineRun.createMetricsObject(Arrays.asList("RANDOM")));
            
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
