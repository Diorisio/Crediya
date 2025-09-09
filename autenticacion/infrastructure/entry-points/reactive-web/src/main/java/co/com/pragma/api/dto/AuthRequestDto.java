package co.com.pragma.api.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthRequestDto(

        @NotNull
        @NotBlank
        @Email
        String email,

        @NotNull
        String password
) {
}
