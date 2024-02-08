package nz.co.mymoney.security;

import nz.co.mymoney.security.jwt.JwtUtil;
import nz.co.mymoney.user.User;
import nz.co.mymoney.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Objects.nonNull;

@RestController
public class AuthController {
    private final UserRepository userRepository;
    private final  PasswordEncoder passwordEncoder;
    private final  JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest) {
        User user = userRepository.findUserByEmail(loginRequest.getEmail());
        if (nonNull(user)) {
            String password = loginRequest.getPassword();
            String passwordFromDB = user.getPassword();
            if (passwordEncoder.matches(password, passwordFromDB)) {
                String token = jwtUtil.createToken(user);
                return new ResponseEntity(token, HttpStatus.OK);
            }
        }
        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }
}