package hudson.plugins.tics;

import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials;
import com.google.common.base.Strings;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import jenkins.model.Jenkins;
import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import java.io.IOException;
import java.util.List;

@SuppressFBWarnings("PA_PUBLIC_PRIMITIVE_ATTRIBUTE")
public class TicsPipelinePublish extends Recorder implements SimpleBuildStep {

    private static final String PUBLISH_TICS_RESULTS = "publishTicsResults";
    public final String viewerUrl;
    public String projectName;
    public String branchName;
    public String ticsProjectPath;
    public String userName;
    public String userId;
    public boolean checkQualityGate;
    public boolean failIfQualityGateFails;
    public String credentialsId;

    @DataBoundConstructor
    public TicsPipelinePublish(
            final String viewerUrl
    ) {
        this.viewerUrl = viewerUrl;
    }

    @Override
    public void perform(@NonNull final Run<?, ?> run, @NonNull final FilePath workspace, @NonNull final EnvVars envvars, @NonNull final Launcher launcher, @NonNull final TaskListener listener) throws InterruptedException, IOException {
        if (Strings.isNullOrEmpty(viewerUrl)) {
            throw new IllegalArgumentException("The pipeline method '" + PUBLISH_TICS_RESULTS + "' was used without specifying the 'viewerUrl'. " +
                    "For instance a 'viewerUrl' looks like: 'http://www.company.com:42506/tiobeweb/TICS'.\n");
        }
        if (Strings.isNullOrEmpty(projectName) || Strings.isNullOrEmpty(branchName)) {
            if (Strings.isNullOrEmpty(ticsProjectPath)) {
                throw new IllegalArgumentException("The pipeline method '" + PUBLISH_TICS_RESULTS + "' was used without specifying the 'projectName' or the 'branchName'.");
            }
        }

        final String creds = getCredentials();
        final TicsPublisher tp = new TicsPublisher(viewerUrl, getTicsProjectPath(), creds, this.checkQualityGate, this.failIfQualityGateFails);
        tp.perform(run, workspace, envvars, launcher, listener);
    }

    private String getTicsProjectPath() {
        if (Strings.isNullOrEmpty(ticsProjectPath)) {
            return String.join("/", "HIE:/", projectName, branchName);
        } else {
            return this.ticsProjectPath;
        }
    }

    private String getCredentials() {

        if (!Strings.isNullOrEmpty(this.credentialsId)) {
            return credentialsId;
        }

        if (Strings.isNullOrEmpty(this.userName) && Strings.isNullOrEmpty(this.userId)) {
            return "";
        }
        if (!Strings.isNullOrEmpty(this.userName) && !Strings.isNullOrEmpty(this.userId)) {
            throw new IllegalArgumentException("Please specify either one of the the 'userName' or 'userId' and not both.");
        }
        if (!Strings.isNullOrEmpty(this.userId)) {
            return findUserCredentials("userId", this.userId, StandardUsernamePasswordCredentials::getId);
        } else {
            return findUserCredentials("userName", this.userName, StandardUsernamePasswordCredentials::getUsername);
        }
    }

    private String findUserCredentials(final String targetName, final String targetValue, final java.util.function.Function<StandardUsernamePasswordCredentials, String> func) {
        final List<StandardUsernamePasswordCredentials> credentialsList = CredentialsProvider.
                lookupCredentialsInItemGroup(
                        StandardUsernamePasswordCredentials.class,
                        Jenkins.getInstanceOrNull(),
                        null,
                        null);
        final String exceptionMessage = "No credentials found for " + targetName + ":'" + targetValue + "'" + " in Jenkins credentials store.";
        final StandardUsernamePasswordCredentials findUserInCredentialsList = credentialsList.stream()
                .filter(c -> func.apply(c).equals(targetValue))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(exceptionMessage));
        return findUserInCredentialsList.getId();
    }

    @DataBoundSetter
    public void setProjectName(final String value) {
        this.projectName = value;
    }

    @DataBoundSetter
    public void setBranchName(final String value) {
        this.branchName = value;
    }

    @DataBoundSetter
    public void setTicsProjectPath(final String value) {
        this.ticsProjectPath = value;
    }

    @DataBoundSetter
    public void setUserName(final String value) {
        this.userName = value;
    }

    @DataBoundSetter
    public void setUserId(final String value) {
        this.userId = value;
    }

    @DataBoundSetter
    public void setCheckQualityGate(final boolean value) {
        this.checkQualityGate = value;
    }

    @DataBoundSetter
    public void setFailIfQualityGateFails(final boolean value) {
        this.failIfQualityGateFails = value;
    }

    @DataBoundSetter
    public void setCredentialsId(final String value) {
        this.credentialsId = value;
    }

    @Symbol(PUBLISH_TICS_RESULTS)
    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        @NonNull
        @Override
        public String getDisplayName() {
            return "";
        }

        @Override
        public boolean isApplicable(final Class<? extends AbstractProject> jobType) {
            return true;
        }
    }
}
