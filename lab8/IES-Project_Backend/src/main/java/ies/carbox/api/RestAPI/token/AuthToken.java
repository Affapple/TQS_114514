package ies.carbox.api.RestAPI.token;

import ies.carbox.api.RestAPI.entity.Role;
import lombok.*;

@Getter
@Setter
public class AuthToken {
    private String token;
    private long expiresIn;
    private String role;
}
