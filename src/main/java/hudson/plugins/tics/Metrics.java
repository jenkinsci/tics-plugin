package hudson.plugins.tics;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import org.kohsuke.stapler.DataBoundConstructor;

import java.lang.reflect.Field;

/**
 * See resources/../Metrics/config.jelly for view that is used to configure this model
 */
public class Metrics extends AbstractDescribableImpl<Metrics> {
	public final boolean ABSTRACTINTERPRETATION;
	public final boolean ACCUCHANGERATE;
	public final boolean ACCUFIXRATE;
	public final boolean ACCULINESADDED;
	public final boolean ACCULINESCHANGED;
	public final boolean ACCULINESDELETED;
	public final boolean AI;
	public final boolean ALL;
	public final boolean AVGCYCLOMATICCOMPLEXITY;
	public final boolean BEGIN;
	public final boolean BUILDRELATIONS;
	public final boolean CHANGEDFILES;
	public final boolean CHANGERATE;
	public final boolean CODINGSTANDARD;
	public final boolean COMPILERWARNING;
	public final boolean CS;
	public final boolean CW;
	public final boolean CY;
	public final boolean CYCLOMATICCOMPLEXITY;
	public final boolean DEADCODE;
	public final boolean DUP;
	public final boolean DUPLICATEDCODE;
	public final boolean DUPLICATEDCODECUSTOM;
	public final boolean ELOC;
	public final boolean END;
	public final boolean FANOUT;
	public final boolean FANOUT_INTEXT;
	public final boolean FINALIZE;
	public final boolean FIXRATE;
	public final boolean GLOC;
	public final boolean HIS_AVGCYCLOMATICCOMPLEXITY;
	public final boolean HIS_CALLLEVELS;
	public final boolean HIS_CALLLEVELS_MAX;
	public final boolean HIS_CODINGSTANDARD;
	public final boolean HIS_COMMENTDENSITY;
	public final boolean HIS_FUNCCALLS;
	public final boolean HIS_FUNCCALLS_MAX;
	public final boolean HIS_FUNCCYCLE;
	public final boolean HIS_FUNCCYCLE_MAX;
	public final boolean HIS_FUNCSIZE;
	public final boolean HIS_FUNCSIZE_MAX;
	public final boolean HIS_GOTOSTATEMENTS;
	public final boolean HIS_GOTOSTATEMENTS_MAX;
	public final boolean HIS_MAXCYCLOMATICCOMPLEXITY;
	public final boolean HIS_PARAMCOUNT;
	public final boolean HIS_PARAMCOUNT_MAX;
	public final boolean HIS_PATHCOUNT;
	public final boolean HIS_PATHCOUNT_MAX;
	public final boolean HIS_RETURNPOINTS;
	public final boolean HIS_RETURNPOINTS_MAX;
	public final boolean HIS_VOCF;
	public final boolean INCLUDERELATIONS;
	public final boolean INTEGRATIONBRANCHCOVERAGE;
	public final boolean INTEGRATIONDECISIONCOVERAGE;
	public final boolean INTEGRATIONFUNCTIONCOVERAGE;
	public final boolean INTEGRATIONSTATEMENTCOVERAGE;
	public final boolean INTEGRATIONTESTCOVERAGE;
	public final boolean ITC;
	public final boolean LINESADDED;
	public final boolean LINESCHANGED;
	public final boolean LINESDELETED;
	public final boolean LOC;
	public final boolean MAXCYCLOMATICCOMPLEXITY;
	public final boolean PATHCOUNT;
	public final boolean PATHCOUNT_MAX;
	public final boolean POSTANA;
	public final boolean PREPARE;
	public final boolean SEC;
	public final boolean SECURITY;
	public final boolean STC;
	public final boolean SYSTEMBRANCHCOVERAGE;
	public final boolean SYSTEMDECISIONCOVERAGE;
	public final boolean SYSTEMFUNCTIONCOVERAGE;
	public final boolean SYSTEMSTATEMENTCOVERAGE;
	public final boolean SYSTEMTESTCOVERAGE;
	public final boolean TOTALBRANCHCOVERAGE;
	public final boolean TOTALDECISIONCOVERAGE;
	public final boolean TOTALFUNCTIONCOVERAGE;
	public final boolean TOTALSTATEMENTCOVERAGE;
	public final boolean TOTALTESTCOVERAGE;
	public final boolean TTC;
	public final boolean UNITBRANCHCOVERAGE;
	public final boolean UNITDECISIONCOVERAGE;
	public final boolean UNITFUNCTIONCOVERAGE;
	public final boolean UNITSTATEMENTCOVERAGE;
	public final boolean UNITTESTCOVERAGE;
	public final boolean UTC;

    @DataBoundConstructor
    public Metrics(final boolean ABSTRACTINTERPRETATION,
    		final boolean ACCUCHANGERATE,
    		final boolean ACCUFIXRATE,
    		final boolean ACCULINESADDED,
    		final boolean ACCULINESCHANGED,
    		final boolean ACCULINESDELETED,
    		final boolean AI,
    		final boolean ALL,
    		final boolean AVGCYCLOMATICCOMPLEXITY,
    		final boolean BEGIN,
    		final boolean BUILDRELATIONS,
    		final boolean CHANGEDFILES,
    		final boolean CHANGERATE,
    		final boolean CODINGSTANDARD,
    		final boolean COMPILERWARNING,
    		final boolean CS,
    		final boolean CW,
    		final boolean CY,
    		final boolean CYCLOMATICCOMPLEXITY,
    		final boolean DEADCODE,
    		final boolean DUP,
    		final boolean DUPLICATEDCODE,
    		final boolean DUPLICATEDCODECUSTOM,
    		final boolean ELOC,
    		final boolean END,
    		final boolean FANOUT,
    		final boolean FANOUT_INTEXT,
    		final boolean FINALIZE,
    		final boolean FIXRATE,
    		final boolean GLOC,
    		final boolean HIS_AVGCYCLOMATICCOMPLEXITY,
    		final boolean HIS_CALLLEVELS,
    		final boolean HIS_CALLLEVELS_MAX,
    		final boolean HIS_CODINGSTANDARD,
    		final boolean HIS_COMMENTDENSITY,
    		final boolean HIS_FUNCCALLS,
    		final boolean HIS_FUNCCALLS_MAX,
    		final boolean HIS_FUNCCYCLE,
    		final boolean HIS_FUNCCYCLE_MAX,
    		final boolean HIS_FUNCSIZE,
    		final boolean HIS_FUNCSIZE_MAX,
    		final boolean HIS_GOTOSTATEMENTS,
    		final boolean HIS_GOTOSTATEMENTS_MAX,
    		final boolean HIS_MAXCYCLOMATICCOMPLEXITY,
    		final boolean HIS_PARAMCOUNT,
    		final boolean HIS_PARAMCOUNT_MAX,
    		final boolean HIS_PATHCOUNT,
    		final boolean HIS_PATHCOUNT_MAX,
    		final boolean HIS_RETURNPOINTS,
    		final boolean HIS_RETURNPOINTS_MAX,
    		final boolean HIS_VOCF,
    		final boolean INCLUDERELATIONS,
    		final boolean INTEGRATIONBRANCHCOVERAGE,
    		final boolean INTEGRATIONDECISIONCOVERAGE,
    		final boolean INTEGRATIONFUNCTIONCOVERAGE,
    		final boolean INTEGRATIONSTATEMENTCOVERAGE,
    		final boolean INTEGRATIONTESTCOVERAGE,
    		final boolean ITC,
    		final boolean LINESADDED,
    		final boolean LINESCHANGED,
    		final boolean LINESDELETED,
    		final boolean LOC,
    		final boolean MAXCYCLOMATICCOMPLEXITY,
    		final boolean PATHCOUNT,
    		final boolean PATHCOUNT_MAX,
    		final boolean POSTANA,
    		final boolean PREPARE,
    		final boolean SEC,
    		final boolean SECURITY,
    		final boolean STC,
    		final boolean SYSTEMBRANCHCOVERAGE,
    		final boolean SYSTEMDECISIONCOVERAGE,
    		final boolean SYSTEMFUNCTIONCOVERAGE,
    		final boolean SYSTEMSTATEMENTCOVERAGE,
    		final boolean SYSTEMTESTCOVERAGE,
    		final boolean TOTALBRANCHCOVERAGE,
    		final boolean TOTALDECISIONCOVERAGE,
    		final boolean TOTALFUNCTIONCOVERAGE,
    		final boolean TOTALSTATEMENTCOVERAGE,
    		final boolean TOTALTESTCOVERAGE,
    		final boolean TTC,
    		final boolean UNITBRANCHCOVERAGE,
    		final boolean UNITDECISIONCOVERAGE,
    		final boolean UNITFUNCTIONCOVERAGE,
    		final boolean UNITSTATEMENTCOVERAGE,
    		final boolean UNITTESTCOVERAGE,
    		final boolean UTC
    ) {
    	this.ABSTRACTINTERPRETATION = ABSTRACTINTERPRETATION;
    	this.ACCUCHANGERATE = ACCUCHANGERATE;
    	this.ACCUFIXRATE = ACCUFIXRATE;
    	this.ACCULINESADDED = ACCULINESADDED;
    	this.ACCULINESCHANGED = ACCULINESCHANGED;
    	this.ACCULINESDELETED = ACCULINESDELETED;
    	this.AI = AI;
    	this.ALL = ALL;
    	this.AVGCYCLOMATICCOMPLEXITY = AVGCYCLOMATICCOMPLEXITY;
    	this.BEGIN = BEGIN;
    	this.BUILDRELATIONS = BUILDRELATIONS;
    	this.CHANGEDFILES = CHANGEDFILES;
    	this.CHANGERATE = CHANGERATE;
    	this.CODINGSTANDARD = CODINGSTANDARD;
    	this.COMPILERWARNING = COMPILERWARNING;
    	this.CS = CS;
    	this.CW = CW;
    	this.CY = CY;
    	this.CYCLOMATICCOMPLEXITY = CYCLOMATICCOMPLEXITY;
    	this.DEADCODE = DEADCODE;
    	this.DUP = DUP;
    	this.DUPLICATEDCODE = DUPLICATEDCODE;
    	this.DUPLICATEDCODECUSTOM = DUPLICATEDCODECUSTOM;
    	this.ELOC = ELOC;
    	this.END = END;
    	this.FANOUT = FANOUT;
    	this.FANOUT_INTEXT = FANOUT_INTEXT;
    	this.FINALIZE = FINALIZE;
    	this.FIXRATE = FIXRATE;
    	this.GLOC = GLOC;
    	this.HIS_AVGCYCLOMATICCOMPLEXITY = HIS_AVGCYCLOMATICCOMPLEXITY;
    	this.HIS_CALLLEVELS = HIS_CALLLEVELS;
    	this.HIS_CALLLEVELS_MAX = HIS_CALLLEVELS_MAX;
    	this.HIS_CODINGSTANDARD = HIS_CODINGSTANDARD;
    	this.HIS_COMMENTDENSITY = HIS_COMMENTDENSITY;
    	this.HIS_FUNCCALLS = HIS_FUNCCALLS;
    	this.HIS_FUNCCALLS_MAX = HIS_FUNCCALLS_MAX;
    	this.HIS_FUNCCYCLE = HIS_FUNCCYCLE;
    	this.HIS_FUNCCYCLE_MAX = HIS_FUNCCYCLE_MAX;
    	this.HIS_FUNCSIZE = HIS_FUNCSIZE;
    	this.HIS_FUNCSIZE_MAX = HIS_FUNCSIZE_MAX;
    	this.HIS_GOTOSTATEMENTS = HIS_GOTOSTATEMENTS;
    	this.HIS_GOTOSTATEMENTS_MAX = HIS_GOTOSTATEMENTS_MAX;
    	this.HIS_MAXCYCLOMATICCOMPLEXITY = HIS_MAXCYCLOMATICCOMPLEXITY;
    	this.HIS_PARAMCOUNT = HIS_PARAMCOUNT;
    	this.HIS_PARAMCOUNT_MAX = HIS_PARAMCOUNT_MAX;
    	this.HIS_PATHCOUNT = HIS_PATHCOUNT;
    	this.HIS_PATHCOUNT_MAX = HIS_PATHCOUNT_MAX;
    	this.HIS_RETURNPOINTS = HIS_RETURNPOINTS;
    	this.HIS_RETURNPOINTS_MAX = HIS_RETURNPOINTS_MAX;
    	this.HIS_VOCF = HIS_VOCF;
    	this.INCLUDERELATIONS = INCLUDERELATIONS;
    	this.INTEGRATIONBRANCHCOVERAGE = INTEGRATIONBRANCHCOVERAGE;
    	this.INTEGRATIONDECISIONCOVERAGE = INTEGRATIONDECISIONCOVERAGE;
    	this.INTEGRATIONFUNCTIONCOVERAGE = INTEGRATIONFUNCTIONCOVERAGE;
    	this.INTEGRATIONSTATEMENTCOVERAGE = INTEGRATIONSTATEMENTCOVERAGE;
    	this.INTEGRATIONTESTCOVERAGE = INTEGRATIONTESTCOVERAGE;
    	this.ITC = ITC;
    	this.LINESADDED = LINESADDED;
    	this.LINESCHANGED = LINESCHANGED;
    	this.LINESDELETED = LINESDELETED;
    	this.LOC = LOC;
    	this.MAXCYCLOMATICCOMPLEXITY = MAXCYCLOMATICCOMPLEXITY;
    	this.PATHCOUNT = PATHCOUNT;
    	this.PATHCOUNT_MAX = PATHCOUNT_MAX;
    	this.POSTANA = POSTANA;
    	this.PREPARE = PREPARE;
    	this.SEC = SEC;
    	this.SECURITY = SECURITY;
    	this.STC = STC;
    	this.SYSTEMBRANCHCOVERAGE = SYSTEMBRANCHCOVERAGE;
    	this.SYSTEMDECISIONCOVERAGE = SYSTEMDECISIONCOVERAGE;
    	this.SYSTEMFUNCTIONCOVERAGE = SYSTEMFUNCTIONCOVERAGE;
    	this.SYSTEMSTATEMENTCOVERAGE = SYSTEMSTATEMENTCOVERAGE;
    	this.SYSTEMTESTCOVERAGE = SYSTEMTESTCOVERAGE;
    	this.TOTALBRANCHCOVERAGE = TOTALBRANCHCOVERAGE;
    	this.TOTALDECISIONCOVERAGE = TOTALDECISIONCOVERAGE;
    	this.TOTALFUNCTIONCOVERAGE = TOTALFUNCTIONCOVERAGE;
    	this.TOTALSTATEMENTCOVERAGE = TOTALSTATEMENTCOVERAGE;
    	this.TOTALTESTCOVERAGE = TOTALTESTCOVERAGE;
    	this.TTC = TTC;
    	this.UNITBRANCHCOVERAGE = UNITBRANCHCOVERAGE;
    	this.UNITDECISIONCOVERAGE = UNITDECISIONCOVERAGE;
    	this.UNITFUNCTIONCOVERAGE = UNITFUNCTIONCOVERAGE;
    	this.UNITSTATEMENTCOVERAGE = UNITSTATEMENTCOVERAGE;
    	this.UNITTESTCOVERAGE = UNITTESTCOVERAGE;
    	this.UTC = UTC;
    }

    public Metrics() {
    	this(false,
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
			   false,
			   false,
			   false,
			   false,
			   false,
			   false,
			   false,
			   false,
			   false
        );
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<Metrics> {
        @NonNull
        @Override
        public String getDisplayName() {
            return "Metrics";
        }
    }

    public ImmutableList<String> getEnabledMetrics() {
        final ImmutableList.Builder<String> out = ImmutableList.builder();
        for (final Field f : this.getClass().getFields()) {
            final boolean enabled;
            try {
                enabled = (Boolean) f.get(this);
            } catch (final Exception e) {
                Throwables.throwIfUnchecked(e);
                throw new RuntimeException(e);
            }
            if (enabled) {
                out.add(f.getName());
            }
        }
        return out.build();
    }
}
