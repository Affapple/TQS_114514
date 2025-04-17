package ies.carbox.api.RestAPI.dtos;

import java.util.List;

import jakarta.persistence.Tuple;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDto {
    private String email;
    private String username;
    private String password;
    private int phone;
    private List<List<String>> carsList;
    
}