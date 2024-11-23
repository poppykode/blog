package co.zw.poppykode.blog.payload;

import co.zw.poppykode.blog.entity.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Set;

@Data
public class SignUpDto {
    private Long id;
    private String name;
    @NotEmpty
    private String username;
    private String email;
    @NotEmpty
    private String password;
    @NotEmpty(message = "User should have atleast one role")
    private Set<Role> roles;
}
