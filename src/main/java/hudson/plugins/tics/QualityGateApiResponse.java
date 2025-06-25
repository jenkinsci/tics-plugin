package hudson.plugins.tics;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.ArrayList;
import java.util.List;

/**
 * Response model for 'api/public/v1/QualityGateStatus'.
 */
@SuppressFBWarnings({"UWF_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD", "UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD"})
public class QualityGateApiResponse {
    public static class Condition {
        public boolean passed;
        public boolean error;
        public String message;
    }

    public static class Gate {
        public boolean passed;
        public String name;
        public List<Condition> conditions = new ArrayList<>();
    }

    public boolean passed;
    public String message;
    public String url;
    public List<Gate> gates = new ArrayList<>();

}
