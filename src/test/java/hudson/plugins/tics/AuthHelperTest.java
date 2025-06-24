package hudson.plugins.tics;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AuthHelperTest {

    private class DecodeTokenTestCase {
        public String authToken;
        public Pair<String, String> expectedResult;
        public DecodeTokenTestCase(final String token, final Pair<String, String> credential) {
            this.authToken = token;
            this.expectedResult = credential;
        }
    }


    private List<DecodeTokenTestCase> getDecodeTokenTestCases() {
        final List<DecodeTokenTestCase> testCases = new ArrayList<>();

        testCases.add(new DecodeTokenTestCase("Zm9vOmJhOmFy", Pair.of("foo", "ba:ar")));
        testCases.add(new DecodeTokenTestCase("YWRtaW46YWRtaW4xOmFkbWluMg==", Pair.of("admin", "admin1:admin2")));
        testCases.add(new DecodeTokenTestCase("dGVzdGVyOnRlc3QxOnRlc3QyOnRlc3QzOmFkZjczODQ5Mzg3NA==", Pair.of("tester", "test1:test2:test3:adf738493874")));
        testCases.add(new DecodeTokenTestCase("MGI5M2UwOGUtYzJiMC00NDdkLTlkYTktMWMyMTY4MmNmYTk2Ons3fi1OLUkhfCh9JXVQZw", Pair.of("0b93e08e-c2b0-447d-9da9-1c21682cfa96", "{7~-N-I!|(}%uPg")));

        return testCases;
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testDecodeTokenToUsernamePassword() {
        // Test cases where no exception is thrown
        for (final DecodeTokenTestCase testCase : getDecodeTokenTestCases()) {
            final Pair<String, String> credentialsPair = AuthHelper.decodeTokenToUsernamePassword(testCase.authToken);

            assertEquals(testCase.expectedResult, credentialsPair);
        }

        // Test case where an exception is thrown
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Malformed authentication token. Please make sure you are using a valid token from the TICS Viewer.");
        AuthHelper.decodeTokenToUsernamePassword("123456");
    }
}
