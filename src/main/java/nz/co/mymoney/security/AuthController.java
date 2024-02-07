package nz.co.mymoney.security;

import nz.co.mymoney.security.User;
import nz.co.mymoney.security.UserRepository;
import nz.co.mymoney.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static java.util.Objects.nonNull;

@RestController
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/login")
    public ResponseEntity login(Principal principal) {

        User user = userRepository.findUserByEmail(principal.getName());
        if (nonNull(user)) {
            String token = jwtUtil.createToken(user);
            return new ResponseEntity(token, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }
}