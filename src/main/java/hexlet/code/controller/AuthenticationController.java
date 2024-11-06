package hexlet.code.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hexlet.code.dto.AuthRequest;
import hexlet.code.util.JWTUtils;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/login")
public class AuthenticationController {
    @Autowired
    private final JWTUtils jwtUtils;
    @Autowired
    private final AuthenticationManager authenticationManager;

    @PostMapping(path = "")
    public String create(@RequestBody AuthRequest authRequest) {
        var authentication = new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(), authRequest.getPassword());

        authenticationManager.authenticate(authentication);

        var token = jwtUtils.generateToken(authRequest.getUsername());
        return token;
    }
}

