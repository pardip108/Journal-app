package com.espire.journalApp.controller;

import com.espire.journalApp.dto.UserDTO;
import com.espire.journalApp.entity.User;
import com.espire.journalApp.service.UserService;
import com.espire.journalApp.utils.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/public")
@Tag(name = "Public APIs")
public class PublicController {

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @GetMapping("/health-check")
    public String healthCheck(){
        return "Ok";
    }

    @PostMapping("/signup")
    public void signup(@RequestBody UserDTO user){
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setUsername(user.getUsername());
        newUser.setSentimentAnalysis(user.isSentimentAnalysis());
        userService.saveNewUser(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO user){
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        }catch (Exception e){
            log.error("Exception occured while authentication ", e);
            return new ResponseEntity<>("incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }
}
