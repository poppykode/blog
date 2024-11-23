package co.zw.poppykode.blog.controller;

import co.zw.poppykode.blog.payload.JWTAuthResponse;
import co.zw.poppykode.blog.payload.LoginDto;
import co.zw.poppykode.blog.payload.SignUpDto;
import co.zw.poppykode.blog.payload.UserResponse;
import co.zw.poppykode.blog.security.JwtTokenProvider;
import co.zw.poppykode.blog.security.SecurityUser;
import co.zw.poppykode.blog.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;


    @PostMapping("/signin")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDto loginDto){
        Authentication authToken = authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(),
                        loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        SecurityUser userDetails = (SecurityUser) authToken.getPrincipal();
        String token = jwtTokenProvider.generateToken(authToken);
        return new ResponseEntity<>(new JWTAuthResponse(token), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> registerUser(@RequestBody SignUpDto signUpDto){
        return  new ResponseEntity<>(userService.registerUser(signUpDto),HttpStatus.CREATED);
    }
}
