package com.lecture.carrental.controller;

import com.lecture.carrental.domain.User;
import com.lecture.carrental.dto.UserDTO;
import com.lecture.carrental.security.jwt.JwtUtils;
import com.lecture.carrental.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
@RequestMapping()
public class UserController {

    public UserService userService;
    public AuthenticationManager authenticationManager;
    public JwtUtils jwtUtils;


    @GetMapping("/user")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserById(HttpServletRequest request){
        Long id = (Long) request.getAttribute("id");
        UserDTO user = userService.findById(id);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PostMapping("/register")
    public ResponseEntity<Map<String, Boolean>> registerUser(@Valid @RequestBody User user) {
        userService.register(user);

        Map<String, Boolean> map = new HashMap<>();
        map.put("User registered successfully!", true);

        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> authenticateUser(@RequestBody Map<String, Object> userMap){
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");

        userService.login(email, password);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PutMapping("/user")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> updateUser(HttpServletRequest request,
                                                           @Valid @RequestBody UserDTO userDTO) {

        Long id = (Long) request.getAttribute("id");
        userService.updateUser(id, userDTO);

        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}
