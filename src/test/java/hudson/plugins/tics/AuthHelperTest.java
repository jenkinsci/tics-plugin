package hudson.plugins.tics;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthHelperTest {

    static Stream<Arguments> parameters() {
        return Stream.of(
                Arguments.of("Zm9vOmJhOmFy", Pair.of("foo", "ba:ar")),
                Arguments.of("YWRtaW46YWRtaW4xOmFkbWluMg==", Pair.of("admin", "admin1:admin2")),
                Arguments.of("dGVzdGVyOnRlc3QxOnRlc3QyOnRlc3QzOmFkZjczODQ5Mzg3NA==", Pair.of("tester", "test1:test2:test3:adf738493874")),
                Arguments.of("MGI5M2UwOGUtYzJiMC00NDdkLTlkYTktMWMyMTY4MmNmYTk2Ons3fi1OLUkhfCh9JXVQZw", Pair.of("0b93e08e-c2b0-447d-9da9-1c21682cfa96", "{7~-N-I!|(}%uPg")));
    }

    @ParameterizedTest
    @MethodSource("parameters")
    void testDecodeTokenToUsernamePassword(final String authToken, final Pair<String, String> expectedResult) {
        // Test cases where no exception is thrown
        final Pair<String, String> credentialsPair = AuthHelper.decodeTokenToUsernamePassword(authToken);
        assertEquals(expectedResult, credentialsPair);
    }

    @Test
    void testDecodeTokenToUsernamePasswordThrowsException() {
        // Test case where an exception is thrown
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> AuthHelper.decodeTokenToUsernamePassword("123456")
        );
        assertEquals("Malformed authentication token. Please make sure you are using a valid token from the TICS Viewer.", exception.getMessage());
    }
}
