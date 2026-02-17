package org.example.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private  final PasswordService passwordService;

    public AuthController(UserService userService, JwtService jwtService, PasswordService passwordService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordService = passwordService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CustomUser request) {

        CustomUser user = userService.findUser(request.getUsername());

        if (user != null && passwordService.matches(request.getPassword(), user.getPassword())) { // request.getPassword().equals(user.getPassword())
            String token = jwtService.generateToken(user.getUsername());
            return ResponseEntity.ok(token);
        }

        return ResponseEntity.status(401).body("LOGIN_FAILD");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CustomUser request) {

        if (userService.findUser(request.getUsername()) != null) {
            return ResponseEntity.status(400).body("USER_EXISTS");
        }

        CustomUser newUser = new CustomUser();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordService.hash(request.getPassword()));

        userService.addUser(newUser);

        return ResponseEntity.ok("REGISTER_OK");
    }
}
