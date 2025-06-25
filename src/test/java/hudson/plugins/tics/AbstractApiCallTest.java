package hudson.plugins.tics;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class AbstractApiCallTest {

    private static class NoProxyTestCase {
        public ImmutableList<Pattern> noProxyPatterns;
        public String targetUrl;
        public boolean expectedResult;

        public NoProxyTestCase(final List<Pattern> noProxyPatterns, final String targetUrl, final boolean expectedResult) {
            this.noProxyPatterns = ImmutableList.copyOf(noProxyPatterns);
            this.targetUrl = targetUrl;
            this.expectedResult = expectedResult;
        }
    }

    private List<Pattern> getPatterns(final List<String> regexes) {
        return regexes.stream().map(Pattern::compile).toList();
    }

    private List<NoProxyTestCase> getNoProxyTestCases() {
        final List<NoProxyTestCase> testCases = new ArrayList<>();

        // Test cases where target url is using an IP address
        // no-proxy is not set
        testCases.add(new NoProxyTestCase(getPatterns(List.of()),
                "http://192.168.1.204:42506/tiobeweb/TICS", false));
        // all urls are exempted
        testCases.add(new NoProxyTestCase(getPatterns(List.of(".*")),
                "http://192.168.1.204:42506/tiobeweb/TICS", true));
        // no-proxy pattern matches
        testCases.add(new NoProxyTestCase(getPatterns(List.of("192\\.168\\.1\\.204")),
                "http://192.168.1.204:42506/tiobeweb/TICS", true));
        // no-proxy pattern matches
        testCases.add(new NoProxyTestCase(getPatterns(List.of("192\\..*")),
                "http://192.168.1.204:42506/tiobeweb/TICS", true));
        // no-proxy pattern does not match
        testCases.add(new NoProxyTestCase(getPatterns(List.of("\\.tiobe\\.com")),
                "http://192.168.1.204:42506/tiobeweb/TICS", false));


        //Test cases where target url is using a domain name
        // no-proxy is not set
        testCases.add(new NoProxyTestCase(getPatterns(List.of()),
                "https://testlab.tiobe.com/tiobeweb/testlab", false));
        // all urls are exempted
        testCases.add(new NoProxyTestCase(getPatterns(List.of(".*")),
                "https://testlab.tiobe.com/tiobeweb/testlab", true));
        // no-proxy pattern matches
        testCases.add(new NoProxyTestCase(getPatterns(List.of("testlab\\.tiobe\\.com")),
                "https://testlab.tiobe.com/tiobeweb/testlab", true));
        // no-proxy pattern matches
        testCases.add(new NoProxyTestCase(getPatterns(List.of(".*\\.tiobe\\.com")),
                "https://testlab.tiobe.com/tiobeweb/testlab", true));
        // no-proxy pattern matches
        testCases.add(new NoProxyTestCase(getPatterns(List.of("\\.tiobe\\.com")),
                "https://testlab.tiobe.com/tiobeweb/testlab", true));
        // no-proxy pattern matches
        testCases.add(new NoProxyTestCase(getPatterns(
                Arrays.asList("testlab\\.tiobe\\.com", ".*\\.tiobe\\.com", "\\.tiobe\\.com")),
                "https://testlab.tiobe.com/tiobeweb/testlab", true));
        // no-proxy pattern does not match
        testCases.add(new NoProxyTestCase(getPatterns(List.of("192\\..*")),
                "https://testlab.tiobe.com/tiobeweb/testlab", false));

        // Test cases where target url is an internal address (localhost or 127.*). The internal addresses are exempted from proxy by default.
        // no-proxy is not set
        testCases.add(new NoProxyTestCase(getPatterns(List.of()),
                "https://localhost:42506/tiobeweb/testlab", true));
        testCases.add(new NoProxyTestCase(getPatterns(List.of()),
                "https://127.0.0.1:42506/tiobeweb/testlab", true));
        testCases.add(new NoProxyTestCase(getPatterns(List.of()),
                "https://127.1.2.1:42506/tiobeweb/testlab", true));
        // all urls are exempted
        testCases.add(new NoProxyTestCase(getPatterns(List.of(".*")),
                "https://localhost:42506/tiobeweb/testlab", true));
        // no-proxy pattern matches
        testCases.add(new NoProxyTestCase(getPatterns(Arrays.asList("localhost", "127.0.0.1")),
                "https://localhost:42506/tiobeweb/testlab", true));
        // no-proxy pattern does not match
        testCases.add(new NoProxyTestCase(getPatterns(List.of("testlab\\.tiobe\\.com")),
                "https://localhost:42506/tiobeweb/testlab", true));

        return testCases;
    }

    @Test
    public void testIsProxyExempted() {
        final MeasureApiCall apiCall = new MeasureApiCall(null, "http://192.168.1.204:42506/tiobeweb/TICS/api/public/v1/Measure", Optional.empty());
        for (final NoProxyTestCase testCase : getNoProxyTestCases()) {
            final boolean isProxyExempted = apiCall.isProxyExempted(testCase.targetUrl, testCase.noProxyPatterns);
            assertEquals(testCase.expectedResult, isProxyExempted);
        }
    }

}
