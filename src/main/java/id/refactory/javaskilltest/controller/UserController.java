package id.refactory.javaskilltest.controller;

import id.refactory.javaskilltest.entity.UserData;
import id.refactory.javaskilltest.repository.UserRepository;
import id.refactory.javaskilltest.util.TokenUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("api/user")
public class UserController {

    private final UserRepository userRepository;

    private final TokenUtils tokenUtils;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, TokenUtils tokenUtils, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenUtils = tokenUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value = "/login", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    ResponseEntity<?> login(@RequestBody UserRequest userRequest) {

        if (userRequest == null) {
            String message = "Username and Password can't empty";

            HashMap<String, Object> badResponse = new HashMap<>();
            badResponse.put("message", message);
            return new ResponseEntity<>(badResponse, HttpStatus.BAD_REQUEST);
        }

        UserData userData = userRepository.getUserByUsername(userRequest.getUsername());

        Boolean isPasswordMatch = passwordEncoder.matches(userRequest.getPassword(), userData.getPassword());

        if (userData == null || !isPasswordMatch) {
            String message = "Username and Password not found";

            HashMap<String, Object> badResponse = new HashMap<>();
            badResponse.put("message", message);
            return new ResponseEntity<>(badResponse, HttpStatus.NOT_FOUND);
        }

        try {
            return getTokenResponse(userRequest.getUsername(), userRequest.getPassword());
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/register", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    ResponseEntity<?> register(@RequestBody UserRequest userRequest) {
        if (userRequest == null) {
            String message = "Username and Password can't empty";

            HashMap<String, Object> badResponse = new HashMap<>();
            badResponse.put("message", message);
            return new ResponseEntity<>(badResponse, HttpStatus.BAD_REQUEST);
        }

        String passwordEncode = passwordEncoder.encode(userRequest.getPassword());
        UserData userData = new UserData(
                0L,
                userRequest.getUsername(),
                passwordEncode,
                System.currentTimeMillis()
        );

        try {
            userRepository.save(userData);

            return getTokenResponse(userRequest.getUsername(), userRequest.getPassword());
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private @NonNull ResponseEntity<?> getTokenResponse(String username, String password) {
        User user = new User(username, password, new ArrayList<>());

        String token = tokenUtils.generateToken(user);

        HashMap<String, Object> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response);
    }
}
