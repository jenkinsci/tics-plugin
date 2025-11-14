package hudson.plugins.tics;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AbstractApiCallTest {

    private static ImmutableList<Pattern> getPatterns(final List<String> regexes) {
        return regexes.stream().map(Pattern::compile).collect(ImmutableList.toImmutableList());
    }

    static Stream<Arguments> parameters() {
        return Stream.of(
                // Test cases where target url is using an IP address
                // no-proxy is not set
                Arguments.of(getPatterns(List.of()),
                        "http://192.168.1.204:42506/tiobeweb/TICS", false),
                // all urls are exempted
                Arguments.of(getPatterns(List.of(".*")),
                        "http://192.168.1.204:42506/tiobeweb/TICS", true),
                // no-proxy pattern matches
                Arguments.of(getPatterns(List.of("192\\.168\\.1\\.204")),
                        "http://192.168.1.204:42506/tiobeweb/TICS", true),
                // no-proxy pattern matches
                Arguments.of(getPatterns(List.of("192\\..*")),
                        "http://192.168.1.204:42506/tiobeweb/TICS", true),
                // no-proxy pattern does not match
                Arguments.of(getPatterns(List.of("\\.tiobe\\.com")),
                        "http://192.168.1.204:42506/tiobeweb/TICS", false),

                //Test cases where target url is using a domain name
                // no-proxy is not set
                Arguments.of(getPatterns(List.of()),
                        "https://testlab.tiobe.com/tiobeweb/testlab", false),
                // all urls are exempted
                Arguments.of(getPatterns(List.of(".*")),
                        "https://testlab.tiobe.com/tiobeweb/testlab", true),
                // no-proxy pattern matches
                Arguments.of(getPatterns(List.of("testlab\\.tiobe\\.com")),
                        "https://testlab.tiobe.com/tiobeweb/testlab", true),
                // no-proxy pattern matches
                Arguments.of(getPatterns(List.of(".*\\.tiobe\\.com")),
                        "https://testlab.tiobe.com/tiobeweb/testlab", true),
                // no-proxy pattern matches
                Arguments.of(getPatterns(List.of("\\.tiobe\\.com")),
                        "https://testlab.tiobe.com/tiobeweb/testlab", true),
                // no-proxy pattern matches
                Arguments.of(getPatterns(
                                Arrays.asList("testlab\\.tiobe\\.com", ".*\\.tiobe\\.com", "\\.tiobe\\.com")),
                        "https://testlab.tiobe.com/tiobeweb/testlab", true),
                // no-proxy pattern does not match
                Arguments.of(getPatterns(List.of("192\\..*")),
                        "https://testlab.tiobe.com/tiobeweb/testlab", false),

                // Test cases where target url is an internal address (localhost or 127.*). The internal addresses are exempted from proxy by default.
                // no-proxy is not set
                Arguments.of(getPatterns(List.of()),
                        "https://localhost:42506/tiobeweb/testlab", true),
                Arguments.of(getPatterns(List.of()),
                        "https://127.0.0.1:42506/tiobeweb/testlab", true),
                Arguments.of(getPatterns(List.of()),
                        "https://127.1.2.1:42506/tiobeweb/testlab", true),
                // all urls are exempted
                Arguments.of(getPatterns(List.of(".*")),
                        "https://localhost:42506/tiobeweb/testlab", true),
                // no-proxy pattern matches
                Arguments.of(getPatterns(Arrays.asList("localhost", "127.0.0.1")),
                        "https://localhost:42506/tiobeweb/testlab", true),
                // no-proxy pattern does not match
                Arguments.of(getPatterns(List.of("testlab\\.tiobe\\.com")),
                        "https://localhost:42506/tiobeweb/testlab", true));
    }

    @ParameterizedTest
    @MethodSource("parameters")
    void testIsProxyExempted(final ImmutableList<Pattern> noProxyPatterns, final String targetUrl, final boolean expectedResult) {
        final MeasureApiCall apiCall = new MeasureApiCall(null, "http://192.168.1.204:42506/tiobeweb/TICS/api/public/v1/Measure", Optional.empty());
        final boolean isProxyExempted = apiCall.isProxyExempted(targetUrl, noProxyPatterns);
        assertEquals(expectedResult, isProxyExempted);
    }

}
