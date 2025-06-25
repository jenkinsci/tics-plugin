package hudson.plugins.tics;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.Util;
import hudson.model.AbstractProject;
import hudson.model.Item;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.plugins.tics.MeasureApiCall.MeasureApiCallException;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import jenkins.tasks.SimpleBuildStep;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.tuple.Pair;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest2;
import org.kohsuke.stapler.verb.POST;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.regex.Pattern;

public class TicsPublisher extends Recorder implements SimpleBuildStep {
    static final String LOGGING_PREFIX = "[TICS Publisher] ";
    private final String ticsPath;
    private final String viewerUrl;
    private final String credentialsId;
    private final boolean checkQualityGate;
    private final boolean failIfQualityGateFails;

    /**
     * Constructor arguments are injected by Jenkins, using settings stored through config.jelly.
     */
    @DataBoundConstructor
    public TicsPublisher(final String viewerUrl
            , final String ticsPath
            , final String credentialsId
            , final boolean checkQualityGate
            , final boolean failIfQualityGateFails
    ) { // Note: variable names must match names in TicsPublisher/config.jelly
        this.viewerUrl = viewerUrl;
        this.ticsPath = ticsPath;
        this.credentialsId = credentialsId;
        this.checkQualityGate = checkQualityGate;
        this.failIfQualityGateFails = failIfQualityGateFails;
    }

    /**
     * Referenced in <code>config.jelly</code>.
     */
    public String getTicsPath() {
        return ticsPath;
    }

    /**
     * Referenced in <code>config.jelly</code>.
     */
    public String getViewerUrl() {
        return viewerUrl;
    }

    /**
     * Referenced in <code>config.jelly</code>.
     */
    public String getCredentialsId() {
        return credentialsId;
    }

    public boolean hasGlobalViewerUrl() {
        return !Strings.isNullOrEmpty(getDescriptor().getViewerUrl());
    }

    public String getGlobalViewerUrl() {
        return getDescriptor().getViewerUrl();
    }

    /**
     * Referenced in <code>config.jelly</code>.
     */
    public boolean getCheckQualityGate() {
        return checkQualityGate;
    }

    /**
     * Referenced in <code>config.jelly</code>.
     */
    public boolean getFailIfQualityGateFails() {
        return failIfQualityGateFails;
    }

    @Override
    public void perform(@NonNull final Run<?, ?> run, @NonNull final FilePath workspace, @NonNull final EnvVars envvars, @NonNull final Launcher launcher, @NonNull final TaskListener listener) throws IOException, RuntimeException, InterruptedException {
        final Optional<Pair<String, String>> usernameAndPassword = AuthHelper.lookupUsernameAndPasswordFromCredentialsId(run.getParent(), credentialsId, run.getEnvironment(listener));
        final String ticsPath1 = Util.replaceMacro(Preconditions.checkNotNull(Strings.emptyToNull(this.ticsPath), "Path not specified"), run.getEnvironment(listener));

        final String measureApiUrl;
        final String qualityGateUrl;
        final String tiobeWebBaseUrl;
        try {
            measureApiUrl = getResolvedTiobewebBaseUrl() + "/api/public/v1/Measure";
            qualityGateUrl = getResolvedTiobewebBaseUrl() + "/api/public/v1/QualityGateStatus";
            tiobeWebBaseUrl = getResolvedTiobewebBaseUrl();
        } catch (final InvalidTicsViewerUrl ex) {
            ex.printStackTrace(listener.getLogger());
            throw new IllegalArgumentException(LOGGING_PREFIX + "Invalid TICS Viewer URL", ex);
        }

        final MeasureApiCall measureApiCall = new MeasureApiCall(listener.getLogger(), measureApiUrl, usernameAndPassword);
        final MetricData tqiData = getTqiMetricData(listener.getLogger(), ticsPath1, measureApiCall);

        QualityGateData gateData;
        if (checkQualityGate) {
            final QualityGateApiCall qgApiCall = new QualityGateApiCall(qualityGateUrl, ticsPath1, usernameAndPassword, listener);
            gateData = retrieveQualityGateData(qgApiCall, listener, tiobeWebBaseUrl);

            if (!gateData.passed && this.failIfQualityGateFails) {
                run.setResult(Result.FAILURE);
            }
        } else {
            gateData = null;
        }

        run.addAction(new TicsPublisherBuildAction(run, ticsPath1, tqiData, gateData, tiobeWebBaseUrl));
        run.setResult(Result.SUCCESS); // note that: "has no effect when the result is already set and worse than the proposed result"
    }

    private MetricData getTqiMetricData(final PrintStream logger, final String ticsPath1, final MeasureApiCall apiCall) {
        final TqiPublisherResultBuilder builder = new TqiPublisherResultBuilder(
                logger,
                apiCall,
                ticsPath1
        );
        try {
            return builder.run();
        } catch (final Exception e) {
            logger.println(LOGGING_PREFIX + Throwables.getStackTraceAsString(e));
            return MetricData.error(ticsPath1, "There was an error while retrieving metric data. See the build log for more information.");
        }
    }

    @SuppressFBWarnings("NP_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD")
    private QualityGateData retrieveQualityGateData(
            final QualityGateApiCall apiCall,
            final TaskListener listener,
            final String tiobeWebBaseUrl
    ) {
        try {
            final QualityGateData gateData = apiCall.retrieveQualityGateData();

            if (gateData.apiResponse != null) {
                final boolean passed = gateData.apiResponse.passed;
                final String encodedQualityGateViewerUrl = tiobeWebBaseUrl + "/" + gateData.apiResponse.url.replace("(", "%28").replace(")", "%29");

                listener.getLogger().println(LOGGING_PREFIX + " Quality Gate " + (passed ? "passed" : "failed")
                        + ". Please check the following url for more information: " + encodedQualityGateViewerUrl);
            }

            return gateData;
        } catch (final Exception e) {
            listener.getLogger().println(LOGGING_PREFIX + Throwables.getStackTraceAsString(e));
            return QualityGateData.error("There was an error while retrieving the quality gate status. See the build log for more information.");
        }
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    /**
     * Descriptor is an object that has metadata about a Describable object, and
     * also serves as a factory (in a way this relationship is similar to
     * Object/Class relationship. A Descriptor/Describable combination is used
     * throughout in Hudson to implement a configuration/extensibility
     * mechanism.
     */
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {
        public String globalViewerUrl;

        public DescriptorImpl() {
            load();
        }

        @NonNull
        @Override
        public String getDisplayName() {
            return "Publish TICS results";
        }

        @Override
        public boolean configure(final StaplerRequest2 request, final JSONObject json) throws FormException {
            // to persist global configuration information set that to properties and call save().
            this.globalViewerUrl = json.getString("globalViewerUrl");
            save();
            return true; // indicate that everything is good so far
        }

        /**
         * Helper method to check whether URL points to a TICS Viewer, in which case it returns Optional.absent().
         */
        private static Optional<FormValidation> checkViewerUrlForErrorsCommon(final String url) {
            final Pattern urlPattern = Pattern.compile("[^:/]+://[^/]+/[^/]+/[^/]+/?");
            final String urlErrorExample = "http(s)://hostname/tiobeweb/section/";

            Optional<FormValidation> validation = ValidationHelper.checkViewerUrlIsEmpty(url);
            if (validation.isPresent()) {
                return validation;
            }

            validation = ValidationHelper.checkViewerUrlPattern(url, urlPattern, urlErrorExample);
            if (validation.isPresent()) {
                return validation;
            }

            validation = ValidationHelper.checkViewerBaseUrlAccessibility(url);
            return validation;
        }

        private FormValidation checkViewerUrlForErrorsOrWarnings(final String value) {
            if (Strings.isNullOrEmpty(value)) {
                return FormValidation.ok();
            }
            Optional<FormValidation> validation = checkViewerUrlForErrorsCommon(value);
            if (validation.isPresent()) {
                return validation.get();
            }
            validation = ValidationHelper.checkViewerUrlForWarningsCommon(value);
            return validation.orElseGet(FormValidation::ok);
        }


        /**
         * Form validation of viewerUrl.
         * Uses Jenkinks naming convention "doCheckXXXXX" to bind to entry field.
         * Further reading: https://wiki.jenkins-ci.org/display/JENKINS/Form+Validation
         **/
        @POST
        public FormValidation doCheckViewerUrl(@AncestorInPath final AbstractProject<?, ?> project, @AncestorInPath final TaskListener listener, @QueryParameter final String value) throws IOException, InterruptedException {
            if (project == null) { // no context
                return FormValidation.ok();
            }
            project.checkPermission(Item.CONFIGURE);

            final EnvVars envvars = project.getEnvironment(null, listener);
            if (Strings.isNullOrEmpty(value)) {
                final String globalViewerUrl2 = Util.replaceMacro(Strings.nullToEmpty(this.globalViewerUrl), envvars);
                // Check whether correctly defined at global level
                if (Strings.isNullOrEmpty(globalViewerUrl2)) {
                    return FormValidation.error("Field is required");
                } else {
                    final Optional<FormValidation> error = checkViewerUrlForErrorsCommon(globalViewerUrl2);
                    if (error.isPresent()) {
                        return FormValidation.errorWithMarkup("Global setting (" + globalViewerUrl2 + ") is invalid: " + error.get().getMessage());
                    } else {
                        return FormValidation.okWithMarkup("Using global setting: " + globalViewerUrl2);
                    }
                }
            }
            return checkViewerUrlForErrorsOrWarnings(Util.replaceMacro(value, envvars));
        }

        /**
         * Form validation of globalViewerUrl.
         */
        @POST
        public FormValidation doCheckGlobalViewerUrl(@AncestorInPath final Item item, @QueryParameter final String value) {
            if (item == null) { // no context
                return FormValidation.ok();
            }
            item.checkPermission(Item.CONFIGURE);
            return checkViewerUrlForErrorsOrWarnings(value);
        }

        @POST
        public FormValidation doCheckTicsPath(@AncestorInPath final AbstractProject<?, ?> project, @AncestorInPath final TaskListener listener, @QueryParameter final String value, @QueryParameter final String viewerUrl, @QueryParameter final String credentialsId) throws IOException, InterruptedException {
            if (project == null) { // no context
                return FormValidation.ok();
            }
            project.checkPermission(Item.CONFIGURE);

            if (Strings.isNullOrEmpty(value)) {
                return FormValidation.error("Field is required");
            }
            if (!value.matches("^[^:/]+://[^/]+/.+$")) {
                return FormValidation.errorWithMarkup("Path should start with <code>hierarchy://project/branch</code>, where <code>hierarchy</code> is either <code>HIE</code> or <code>ORG</code>.");
            }

            final String resolvedViewerUrl = Optional.ofNullable(Strings.emptyToNull(viewerUrl)).orElse(Strings.nullToEmpty(globalViewerUrl));
            if (checkViewerUrlForErrorsCommon(resolvedViewerUrl).isPresent()) {
                // if an error is present for the URL, do not validate any further
                return FormValidation.ok();
            }

            try {
                if (!Strings.isNullOrEmpty(credentialsId)) {
                    final EnvVars envvars = project.getEnvironment(null, listener);
                    final String measureApiUrl = ValidationHelper.getMeasureApiUrl(ValidationHelper.getTiobewebBaseUrlFromGivenUrl(Util.replaceMacro(resolvedViewerUrl, envvars)));
                    final PrintStream dummyLogger = new PrintStream(new ByteArrayOutputStream(), false, StandardCharsets.UTF_8);

                    final Optional<Pair<String, String>> usernameAndPassword = AuthHelper.lookupUsernameAndPasswordFromCredentialsId(project, credentialsId, envvars);
                    final MeasureApiCall apiCall = new MeasureApiCall(dummyLogger, measureApiUrl, usernameAndPassword);
                    apiCall.execute(MeasureApiCall.RESPONSE_DOUBLE_TYPETOKEN, Util.replaceMacro(value, envvars), "none");
                }

                return FormValidation.ok();
            } catch (final IllegalArgumentException | InvalidTicsViewerUrl | MeasureApiCallException e) {
                return FormValidation.errorWithMarkup(e.getMessage());
            }
        }

        @Override
        public boolean isApplicable(final Class<? extends AbstractProject> jobType) {
            return true;
        }

        /**
         * Referenced in <code>global.jelly</code>.
         */
        public String getViewerUrl() {
            return globalViewerUrl;
        }

        /**
         * Called by Jenkins to fill credentials dropdown list
         */
        public ListBoxModel doFillCredentialsIdItems(@AncestorInPath final Item context, @QueryParameter final String credentialsId) {
            return AuthHelper.fillCredentialsDropdown(context, credentialsId);
        }
    }

    static class InvalidTicsViewerUrl extends Exception {
        public InvalidTicsViewerUrl(final String msg) {
            super(msg);
        }
    }

    /**
     * Returns the tiobeweb URL based on the configured viewer url, tries project setting first, then global setting.
     * Example: "http://192.168.1.88:42506/tiobeweb/default"
     *
     * @throws InvalidTicsViewerUrl
     */
    public String getResolvedTiobewebBaseUrl() throws InvalidTicsViewerUrl {
        Optional<String> optUrl = Optional.ofNullable(Strings.emptyToNull(getViewerUrl()));
        if (optUrl.isEmpty()) {
            optUrl = Optional.ofNullable(Strings.emptyToNull(getDescriptor().getViewerUrl()));
        }
        if (optUrl.isEmpty()) {
            throw new InvalidTicsViewerUrl("TICS Viewer URL was not configured at project level or globally.");
        }
        return ValidationHelper.getTiobewebBaseUrlFromGivenUrl(optUrl.get());
    }


}
