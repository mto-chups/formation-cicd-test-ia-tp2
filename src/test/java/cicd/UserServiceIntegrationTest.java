package cicd;

import static org.junit.jupiter.api.Assertions.*;

import com.devops.cicd.user.Role;
import com.devops.cicd.user.User;
import com.devops.cicd.user.UserService;
import org.junit.jupiter.api.Test;

class UserServiceTest {

    private final UserService service = new UserService(); // adapte si interface/impl différente

    @Test
    void register_with_valid_data_returns_valid_user() {
        User u = service.register(" alice@test.com ", "Abcd123!", Role.USER);

        assertNotNull(u);
        assertEquals("alice@test.com", u.getEmail());
        assertEquals("Abcd123!", u.getPassword());
        assertEquals(Role.USER, u.getRole());
        assertFalse(u.canAccessAdminArea());
    }

    @Test
    void register_admin_role_can_access_admin_area() {
        User u = service.register("admin@test.com", "Abcd123!", Role.ADMIN);
        assertTrue(u.canAccessAdminArea());
    }

    @Test
    void register_propagates_email_validation_exception_exactly() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.register("alice@@test.com", "Abcd123!", Role.USER)
        );
        assertEquals("email must be valid", ex.getMessage());
    }

    @Test
    void register_propagates_password_exception_exactly() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.register("alice@test.com", "abcd1234", Role.USER)
        );
        assertEquals("password must be strong", ex.getMessage());
    }

    @Test
    void register_propagates_role_null_exception_exactly() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.register("alice@test.com", "Abcd123!", null)
        );
        assertEquals("role must not be null", ex.getMessage());
    }
}
