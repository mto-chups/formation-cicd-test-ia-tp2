package cicd;

import static org.junit.jupiter.api.Assertions.*;

import com.devops.cicd.user.Role;
import com.devops.cicd.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class UserTest {

    // Helpers
    private static final String STRONG_PWD = "Abcd123!"; // len=8, Maj/Min/Chiffre/Special
    private static final String STRONG_PWD_WITH_SPACES = "Abcd 123!"; // password non trim
    private static final String VALID_EMAIL = "alice@test.com";

    @Nested
    @DisplayName("Cas nominaux")
    class NominalCases {

        @Test
        @DisplayName("Création User avec email valide + password fort + role non null -> OK")
        void shouldCreateUserWhenAllValid() {
            User user = new User(VALID_EMAIL, STRONG_PWD, Role.USER);

            assertNotNull(user);
            assertEquals(VALID_EMAIL, user.getEmail());
            assertEquals(STRONG_PWD, user.getPassword());
            assertEquals(Role.USER, user.getRole());
        }

        @Test
        @DisplayName("Normalisation: email trim() avant stockage; password inchangé; role inchangé")
        void shouldNormalizeEmailOnly() {
            User user = new User("  alice@test.com  ", STRONG_PWD, Role.ADMIN);

            assertEquals("alice@test.com", user.getEmail(), "email must be trimmed before storage");
            assertEquals(STRONG_PWD, user.getPassword(), "password must not be modified");
            assertEquals(Role.ADMIN, user.getRole(), "role must be stored as-is");
        }

        @Test
        @DisplayName("Admin access: ADMIN -> true")
        void canAccessAdminArea_admin_true() {
            User user = new User(VALID_EMAIL, STRONG_PWD, Role.ADMIN);
            assertTrue(user.canAccessAdminArea());
        }

        @Test
        @DisplayName("Admin access: USER -> false")
        void canAccessAdminArea_user_false() {
            User user = new User(VALID_EMAIL, STRONG_PWD, Role.USER);
            assertFalse(user.canAccessAdminArea());
        }
    }

    @Nested
    @DisplayName("Cas limites")
    class EdgeCases {

        @Test
        @DisplayName("Email avec espaces autour: \" alice@test.com \" -> stocké \"alice@test.com\"")
        void emailWithSpacesIsTrimmed() {
            User user = new User(" alice@test.com ", STRONG_PWD, Role.USER);
            assertEquals("alice@test.com", user.getEmail());
        }

        @Test
        @DisplayName("Email minimalement valide: \"a@b.c\" -> OK")
        void minimalValidEmail_ok() {
            User user = new User("a@b.c", STRONG_PWD, Role.USER);
            assertEquals("a@b.c", user.getEmail());
        }

        @Test
        @DisplayName("Email avec plusieurs . après @ : \"a@b.c.d\" -> valide")
        void emailMultipleDotsAfterAt_ok() {
            User user = new User("a@b.c.d", STRONG_PWD, Role.USER);
            assertEquals("a@b.c.d", user.getEmail());
        }

        @Test
        @DisplayName("Password exactement conforme (len=8, maj/min/chiffre/special) -> OK")
        void passwordExactlyStrong_ok() {
            User user = new User(VALID_EMAIL, "Abcd123!", Role.USER);
            assertEquals("Abcd123!", user.getPassword());
        }

        @Test
        @DisplayName("Password contenant des espaces: non trim; si accepté par la policy -> inchangé")
        void passwordWithSpaces_isNotTrimmed_ifAcceptedByPolicy() {
            String pwd = STRONG_PWD_WITH_SPACES;

            try {
                User user = new User(VALID_EMAIL, pwd, Role.USER);
                assertEquals(pwd, user.getPassword(), "password must not be trimmed/modified");
            } catch (IllegalArgumentException ex) {
                assertEquals("password must be strong", ex.getMessage());
            }
        }

    }

    @Nested
    @DisplayName("Cas d'erreur: validation email")
    class EmailValidationErrors {

        @ParameterizedTest(name = "email invalide attendu: \"{0}\"")
        @ValueSource(strings = {"", "   ", "alice", "alice@", "@test.com", "alice@test", "alice@@test.com"})
        void invalidEmails_throw(String email) {
            System.out.println("Testing email: [" + email + "]");
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> new User(email, STRONG_PWD, Role.USER)
            );
            assertEquals("email must be valid", ex.getMessage());
        }

        @Test
        @DisplayName("Email null -> IllegalArgumentException(\"email must be valid\")")
        void emailNull_throw() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> new User(null, STRONG_PWD, Role.USER)
            );
            assertEquals("email must be valid", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("Cas d'erreur: validation password")
    class PasswordValidationErrors {

        @Test
        @DisplayName("Password null -> IllegalArgumentException(\"password must be strong\")")
        void passwordNull_throw() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> new User(VALID_EMAIL, null, Role.USER)
            );
            assertEquals("password must be strong", ex.getMessage());
        }

        @ParameterizedTest(name = "password vide/blanc -> IllegalArgumentException(\"password must be strong\"): \"{0}\"")
        @ValueSource(strings = {"", "   "})
        void passwordBlank_throw(String password) {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> new User(VALID_EMAIL, password, Role.USER)
            );
            assertEquals("password must be strong", ex.getMessage());
        }

        @Test
        @DisplayName("Password non fort -> IllegalArgumentException(\"password must be strong\")")
        void passwordNotStrong_throw() {
            // volontairement faible: pas de maj, pas de special
            String weak = "abcd1234";
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> new User(VALID_EMAIL, weak, Role.USER)
            );
            assertEquals("password must be strong", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("Cas d'erreur: validation role")
    class RoleValidationErrors {

        @Test
        @DisplayName("Role null -> IllegalArgumentException(\"role must not be null\")")
        void roleNull_throw() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> new User(VALID_EMAIL, STRONG_PWD, null)
            );
            assertEquals("role must not be null", ex.getMessage());
        }
    }
}
