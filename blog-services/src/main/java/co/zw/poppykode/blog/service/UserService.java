package co.zw.poppykode.blog.service;

import co.zw.poppykode.blog.entity.User;
import co.zw.poppykode.blog.payload.SignUpDto;
import co.zw.poppykode.blog.payload.UserResponse;
import co.zw.poppykode.blog.security.SecurityUser;

public interface UserService {
    User getUserByUsername(String username);
    UserResponse registerUser(SignUpDto signUpDto);
    Boolean emailAlreadyExists(String email);
    Boolean usernameAlreadyExists(String username);

}
