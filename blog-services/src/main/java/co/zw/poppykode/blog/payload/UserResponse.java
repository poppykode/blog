package co.zw.poppykode.blog.payload;

import co.zw.poppykode.blog.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String username;
    private String email;
    private Set<Role> roles;
}
