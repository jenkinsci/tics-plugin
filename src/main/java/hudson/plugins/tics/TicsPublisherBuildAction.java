package hudson.plugins.tics;

import com.google.common.collect.ImmutableMap;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.Descriptor;
import hudson.model.ProminentProjectAction;
import hudson.model.Run;
import hudson.plugins.tics.TicsPublisher.InvalidTicsViewerUrl;
import hudson.tasks.Publisher;
import hudson.util.DescribableList;
import jenkins.tasks.SimpleBuildStep;
import org.apache.http.client.utils.URIBuilder;
import org.joda.time.DateTime;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Note: the fields of TicsPublisherBuildAction are serialized in Jenkins' build.xml files,
 * including the build field!
 * DO NOT CHANGE THESE FIELDS names!
 *
 * @author dreniers
 */
public class TicsPublisherBuildAction extends AbstractTicsPublisherAction implements ProminentProjectAction, SimpleBuildStep.LastBuildAction {
    /**
     * Build will be stored as a reference in build.xml as: '&lt;build class="build" reference="../../.."/&gt;'
     * I tried adding the publisher as a field here, but that just stores the fields, not the reference.
     **/
    private final Run<?, ?> run;
    public final MetricData tqiData;
    public final QualityGateData gateData;
    public final String ticsPath;
    private final String tiobeWebBaseUrl;

    private final List<TicsPublisherProjectAction> projectActions;

    public TicsPublisherBuildAction(
            final Run<?, ?> run,
            final String ticsPath,
            final MetricData tqiData,
            final QualityGateData QualityGateData,
            final String tiobeWebBaseUrl
    ) {
        this.run = run;
        this.ticsPath = ticsPath;
        this.tqiData = tqiData;
        this.gateData = QualityGateData;
        final List<TicsPublisherProjectAction> actions = new ArrayList<>();
        actions.add(new TicsPublisherProjectAction(run, ticsPath));
        this.projectActions = actions;
        this.tiobeWebBaseUrl = tiobeWebBaseUrl;
    }

    @Override
    public String getIconFileName() {
        // We return null to indicate that their should not be a link in the sidebar on the left.
        // The TICS results are already in the summary.
        return null;
    }

    /**
     * From: http://grepcode.com/file_/repo1.maven.org/maven2/org.hudsonci.plugins/artifactory/2.1.3-h-2/org/jfrog/hudson/action/ActionableHelper.java/?v=source
     *
     * @return The project publisher of the given type. Null if not found.
     */
    public static <T extends Publisher> T getPublisher(final Run<?, ?> run, final Class<T> type) {
        // Pipeline runs are not an instance of AbstractProject

        if (run.getParent() instanceof AbstractProject<?, ?> project) {
            final DescribableList<Publisher, Descriptor<Publisher>> publishersList = project.getPublishersList();
            for (final Publisher publisher : publishersList) {
                if (type.isInstance(publisher)) {
                    return type.cast(publisher);
                }
            }
        }
        return null;
    }


    public final String getOpenInViewerUrl() {
        if (this.tqiData == null) {
            return null;
        }
        final String dashboardFilePath = "/TqiDashboard.html";
        final String fragment = "axes=" + tqiData.ticsPath;
        return openInViewerUrl(dashboardFilePath, fragment);
    }

    public final String openInViewerUrl(final String link, final String fragment) {
        if (this.run == null) {
            return "";
        }

        String baseUrl;
        final TicsPublisher publisher = getPublisher(this.run, TicsPublisher.class);
        if (publisher == null) {
            baseUrl = this.tiobeWebBaseUrl;
        } else {
            try {
                baseUrl = publisher.getResolvedTiobewebBaseUrl();
            } catch (final InvalidTicsViewerUrl e) {
                baseUrl = this.tiobeWebBaseUrl;
            }
        }

        final URI uri;
        try {
            uri = new URIBuilder(baseUrl + link)
                    .setFragment(fragment)
                    .build();
        } catch (final URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return uri.toString();
    }

    public String getLetterForegroundColor(final String letter) {
        final ImmutableMap<String, String> colors = ImmutableMap.<String, String>builder()
                .put("A", "white")
                .put("B", "white")
                .put("C", "black")
                .put("D", "black")
                .put("E", "white")
                .put("F", "white")
                .build();
        return colors.getOrDefault(letter, "black");
    }

    public String getLetterBackgroundColor(final String letter) {
        final ImmutableMap<String, String> colors = ImmutableMap.<String, String>builder()
                .put("A", "#006400")
                .put("B", "#64AE00")
                .put("C", "#FFFF00")
                .put("D", "#FF950E")
                .put("E", "#FF420E")
                .put("F", "#BE0000")
                .build();
        return colors.getOrDefault(letter, "#CCC");
    }

    public String formatDate(final String date) {
        try {
            return new DateTime(date).toString("YYYY-MM-dd HH:mm:ss");
        } catch (final IllegalArgumentException ex) {
            return "-";
        }
    }

    public long countConditions(final QualityGateApiResponse.Gate gate, final boolean passed) {
        return gate.conditions.stream().filter(c -> passed == c.passed).count();
    }

    @Override
    public Collection<? extends Action> getProjectActions() {
        return this.projectActions;
    }


    public final String getViewerQualityGateDetails() {
        if (gateData == null || gateData.apiResponse == null || gateData.apiResponse.url == null) {
            return "";
        }
        final String escapedQualityGateViewerUrl = "/" + this.gateData.apiResponse.url;
        return this.openInViewerUrl(escapedQualityGateViewerUrl, "");
    }

}
