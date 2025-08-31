package co.com.pragma.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigInteger;


public record RequestSolicitud(
        @NotNull
        @NotBlank
        Double monto,
        @NotNull
        @NotBlank
        String plazo,
        @NotNull
        @NotBlank
        @Email
        String email,
        BigInteger id_estado,

        BigInteger id_tipo_prestamo

) {
}
