package co.zw.poppykode.blog.service.impl;

import co.zw.poppykode.blog.entity.Role;
import co.zw.poppykode.blog.entity.User;
import co.zw.poppykode.blog.exception.BlogAPIException;
import co.zw.poppykode.blog.payload.SignUpDto;
import co.zw.poppykode.blog.payload.UserResponse;
import co.zw.poppykode.blog.repository.RoleRepository;
import co.zw.poppykode.blog.repository.UserRepository;
import co.zw.poppykode.blog.security.SecurityUser;
import co.zw.poppykode.blog.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static co.zw.poppykode.blog.utils.AppConstants.ROLE_PREFIX;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserByUsername(username);
        return new SecurityUser(user);
    }

    @Override
    public User getUserByUsername(String username) {
        return  userRepository.findByUsername(username).
                orElseThrow(()-> new UsernameNotFoundException("User with username "+username+" was not found"));
    }

    @Override
    public UserResponse registerUser(SignUpDto signUpDto) {
        if (emailAlreadyExists(signUpDto.getEmail())){
            throw  new BlogAPIException(HttpStatus.BAD_REQUEST,"Email "+ signUpDto.getEmail()+" address already Exists");
        };
        if (usernameAlreadyExists(signUpDto.getUsername())){
            throw  new BlogAPIException(HttpStatus.BAD_REQUEST,"Username "+ signUpDto.getUsername()+" address already Exists");
        };
        User user = mapToUser(signUpDto);
        if(!signUpDto.getRoles().isEmpty()){
            signUpDto.getRoles().forEach(role -> {
                System.out.println(ROLE_PREFIX+role.getName());
                Role dbRole = roleRepository.findByName(ROLE_PREFIX+role.getName())
                        .orElseThrow(() -> new BlogAPIException(HttpStatus.NOT_FOUND, "Role " + role.getName() + " does not exist in the System"));
                user.addRoleToUser(dbRole);
            });
        }
        userRepository.save(user);
        return mapToUserResponse(user);
    }


    private UserResponse mapToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setRoles(user.getRoles());
        return  userResponse;
    }

    private User mapToUser(SignUpDto signUpDto) {
        User user = new User();
        user.setName(signUpDto.getName());
        user.setUsername(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        return user;
    }
    private SignUpDto mapToSignUpDto(User user) {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setId(user.getId());
        signUpDto.setName(user.getName());
        signUpDto.setUsername(user.getUsername());
        signUpDto.setEmail(user.getEmail());
        signUpDto.setRoles(user.getRoles());
        return signUpDto;
    }

    @Override
    public Boolean emailAlreadyExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Boolean usernameAlreadyExists(String username) {
        return userRepository.existsByUsername(username);
    }
}
