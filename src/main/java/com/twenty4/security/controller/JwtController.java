package com.twenty4.security.controller;

import com.twenty4.common.ErrorResponseObject;
import com.twenty4.entity.User;
import com.twenty4.repository.UserRepository;
import com.twenty4.security.component.JwtTokenComponent;
import com.twenty4.security.model.ApplicationUser;
import com.twenty4.security.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("${base.url}")
public class JwtController {

    @Autowired
    private JwtTokenComponent jwtTokenComponent;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "/auth")
    public ResponseEntity<?> auth(@RequestBody ApplicationUser applicationUser) {
        Optional<User> user = userRepository.findByEmail(applicationUser.getEmail());
        if (user.isPresent()) {
            if (!passwordMatches(applicationUser.getPassword(), user.get().getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new ErrorResponseObject(String.valueOf(Instant.now().toEpochMilli()), HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(), "Wrong email or password!", null));
            }
            return ResponseEntity.ok(new Token(jwtTokenComponent.generateToken(user.get())));
        }else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    private Boolean passwordMatches(String rawPassword, String encodedPassword) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(rawPassword, encodedPassword)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
