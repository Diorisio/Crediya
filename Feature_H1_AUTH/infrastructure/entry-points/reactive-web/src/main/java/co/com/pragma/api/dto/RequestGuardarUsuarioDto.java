package co.com.pragma.api.dto;



import jakarta.validation.constraints.*;

import java.math.BigInteger;



public record RequestGuardarUsuarioDto(

        @NotNull
        @NotBlank
         String nombre,
        @NotNull
        @NotBlank
         String apellido,
        @NotNull
        @NotBlank
        @Email
         String correoElectronico ,

         @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "fechaNacimiento debe tener formato YYYY-MM-DD")
         String fechaNacimiento,

         String direccion,

         String telefono,
        @NotNull
        @DecimalMin(value = "0", message = "El salario no puede ser menor que 0")
        @DecimalMax(value = "15000000", message = "El salario no puede ser mayor que 15.000.000")
         BigInteger salarioBase
) {
}
