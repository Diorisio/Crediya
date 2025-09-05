package co.com.pragma.model.jwt.gateways;

public interface PasswordEncoderRepository {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
