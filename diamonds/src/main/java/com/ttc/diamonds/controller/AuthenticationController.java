package com.ttc.diamonds.controller;

import com.ttc.diamonds.config.JwtTokenUtil;
import com.ttc.diamonds.dto.AuthToken;
import com.ttc.diamonds.model.User;
import com.ttc.diamonds.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<AuthToken> register(@RequestParam String username, @RequestParam String password) throws AuthenticationException {

        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        if (auth.isAuthenticated()) {
            UserDetails user = (UserDetails) auth.getPrincipal();
            final String token = jwtTokenUtil.generateToken(user);
            return new ResponseEntity<>(new AuthToken(token, user.getUsername()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
