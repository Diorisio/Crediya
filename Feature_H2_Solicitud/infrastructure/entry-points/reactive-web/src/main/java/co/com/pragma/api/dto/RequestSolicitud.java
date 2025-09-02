package co.com.pragma.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigInteger;

public record RequestSolicitud(
        @NotNull
        Double monto,
        @NotNull
        Integer  plazo,
        @NotNull
        @NotBlank
        @Email
        String correoElectronico,

        BigInteger id_estado,

        BigInteger idTipoPrestamo,

        BigInteger identificacion
) {
}
