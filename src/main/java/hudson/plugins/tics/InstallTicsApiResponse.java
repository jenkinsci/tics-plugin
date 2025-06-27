package hudson.plugins.tics;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class InstallTicsApiResponse {
    @SuppressFBWarnings({"UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD", "UWF_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD"})
    public static class Links {
        public String setPropPath;
        public String queryArtifact;
        public String uploadArtifact;
        /**
         * Link to GET API that will return a script that can be used to install TICS.
         * For this link to be returned you have to provide parameters:
         * <ul>
         * <li>configuration
         * <li>platform
         * <li>URL
         * </ul>
         **/
        public String installTics;
    }

    public final Links links = new Links();
}

