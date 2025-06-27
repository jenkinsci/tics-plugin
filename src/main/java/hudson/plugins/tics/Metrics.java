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
    public final boolean ALL;
    public final boolean AVGCYCLOMATICCOMPLEXITY;
    public final boolean BUILDRELATIONS;
    public final boolean CHANGEDFILES;
    public final boolean CHANGERATE;
    public final boolean CODINGSTANDARD;
    public final boolean COMPILERWARNING;
    public final boolean DEADCODE;
    public final boolean DUPLICATEDCODE;
    public final boolean ELOC;
    public final boolean FANOUT;
    public final boolean FINALIZE;
    public final boolean FIXRATE;
    public final boolean GLOC;
    public final boolean INCLUDERELATIONS;
    public final boolean INTEGRATIONTESTCOVERAGE;
    public final boolean LINESADDED;
    public final boolean LINESCHANGED;
    public final boolean LINESDELETED;
    public final boolean LOC;
    public final boolean MAXCYCLOMATICCOMPLEXITY;
    public final boolean PREPARE;
    public final boolean SECURITY;
    public final boolean SYSTEMTESTCOVERAGE;
    public final boolean TOTALTESTCOVERAGE;
    public final boolean UNITTESTCOVERAGE;

    @DataBoundConstructor
    public Metrics(final boolean ABSTRACTINTERPRETATION
            , final boolean ACCUCHANGERATE
            , final boolean ACCUFIXRATE
            , final boolean ACCULINESADDED
            , final boolean ACCULINESCHANGED
            , final boolean ACCULINESDELETED
            , final boolean ALL
            , final boolean AVGCYCLOMATICCOMPLEXITY
            , final boolean BUILDRELATIONS
            , final boolean CHANGEDFILES
            , final boolean CHANGERATE
            , final boolean CODINGSTANDARD
            , final boolean COMPILERWARNING
            , final boolean DEADCODE
            , final boolean DUPLICATEDCODE
            , final boolean ELOC
            , final boolean FANOUT
            , final boolean FINALIZE
            , final boolean FIXRATE
            , final boolean GLOC
            , final boolean INCLUDERELATIONS
            , final boolean INTEGRATIONTESTCOVERAGE
            , final boolean LINESADDED
            , final boolean LINESCHANGED
            , final boolean LINESDELETED
            , final boolean LOC
            , final boolean MAXCYCLOMATICCOMPLEXITY
            , final boolean PREPARE
            , final boolean SECURITY
            , final boolean SYSTEMTESTCOVERAGE
            , final boolean TOTALTESTCOVERAGE
            , final boolean UNITTESTCOVERAGE
    ) {
        this.ABSTRACTINTERPRETATION = ABSTRACTINTERPRETATION;
        this.ACCUCHANGERATE = ACCUCHANGERATE;
        this.ACCUFIXRATE = ACCUFIXRATE;
        this.ACCULINESADDED = ACCULINESADDED;
        this.ACCULINESCHANGED = ACCULINESCHANGED;
        this.ACCULINESDELETED = ACCULINESDELETED;
        this.ALL = ALL;
        this.AVGCYCLOMATICCOMPLEXITY = AVGCYCLOMATICCOMPLEXITY;
        this.BUILDRELATIONS = BUILDRELATIONS;
        this.CHANGEDFILES = CHANGEDFILES;
        this.CHANGERATE = CHANGERATE;
        this.CODINGSTANDARD = CODINGSTANDARD;
        this.COMPILERWARNING = COMPILERWARNING;
        this.DEADCODE = DEADCODE;
        this.DUPLICATEDCODE = DUPLICATEDCODE;
        this.ELOC = ELOC;
        this.FANOUT = FANOUT;
        this.FINALIZE = FINALIZE;
        this.FIXRATE = FIXRATE;
        this.GLOC = GLOC;
        this.INCLUDERELATIONS = INCLUDERELATIONS;
        this.INTEGRATIONTESTCOVERAGE = INTEGRATIONTESTCOVERAGE;
        this.LINESADDED = LINESADDED;
        this.LINESCHANGED = LINESCHANGED;
        this.LINESDELETED = LINESDELETED;
        this.LOC = LOC;
        this.MAXCYCLOMATICCOMPLEXITY = MAXCYCLOMATICCOMPLEXITY;
        this.PREPARE = PREPARE;
        this.SECURITY = SECURITY;
        this.SYSTEMTESTCOVERAGE = SYSTEMTESTCOVERAGE;
        this.TOTALTESTCOVERAGE = TOTALTESTCOVERAGE;
        this.UNITTESTCOVERAGE = UNITTESTCOVERAGE;
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
