package hudson.plugins.tics;

import hudson.model.Run;

import java.util.List;
import java.util.Objects;

public class TicsPublisherProjectAction extends AbstractTicsPublisherAction {
    public final Run<?, ?> run;
    public final String ticsPath;

    public TicsPublisherProjectAction(final Run<?, ?> run, final String ticsPath) {
        this.run = run;
        this.ticsPath = ticsPath;
    }

    @Override
    public String getIconFileName() {
        // We return null to indicate that their should not be a link in the sidebar on the left.
        // The TICS results are already in the floating box.
        return null;
        //return "/plugin/tics/tics24x24.gif";
    }


    /**
     * Gets the most recent {@link TicsPublisherBuildAction} object.
     */
    public TicsPublisherBuildAction getLastBuild() {
        for (Run<?, ?> b = run.getParent().getLastBuild(); b != null; b = b.getPreviousBuild()) {
            final List<TicsPublisherBuildAction> actions = b.getActions(TicsPublisherBuildAction.class);
            for (final TicsPublisherBuildAction action : actions) {
                if (action == null) {
                    continue;
                }
                if (Objects.equals(ticsPath, action.ticsPath) // #30349: differentiate between multiple publish steps within a single pipeline invocation
                        || this.ticsPath == null // happens for builds at the time that TicsPublisherProjectAction did not yet store ticsPath
                ) {
                    return action;
                }
            }
        }
        return null;
    }
}
