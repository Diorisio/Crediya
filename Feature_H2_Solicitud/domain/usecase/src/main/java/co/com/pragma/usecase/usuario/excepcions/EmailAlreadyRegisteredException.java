package co.com.pragma.usecase.usuario.excepcions;

public class EmailAlreadyRegisteredException extends RuntimeException{

    private final String email;

    public EmailAlreadyRegisteredException(String email) {
        super("El correo electrónico ya está registrado: " + email);
        this.email = email;
    }
}
