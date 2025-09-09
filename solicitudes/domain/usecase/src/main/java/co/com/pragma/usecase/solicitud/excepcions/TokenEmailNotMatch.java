package co.com.pragma.usecase.solicitud.excepcions;


public class TokenEmailNotMatch extends RuntimeException{
        public TokenEmailNotMatch() {
            super("El correo del token no coincide con el de la solicitud");
        }
    }

