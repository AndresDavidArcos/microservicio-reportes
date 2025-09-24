package co.com.pragma.consumer.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequestDTO {
    private String correo;
    private String password;
}
