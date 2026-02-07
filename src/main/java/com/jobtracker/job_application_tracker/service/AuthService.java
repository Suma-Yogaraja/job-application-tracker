package com.jobtracker.job_application_tracker.service;

import com.jobtracker.job_application_tracker.dto.AuthResponse;
import com.jobtracker.job_application_tracker.dto.LoginRequest;
import com.jobtracker.job_application_tracker.dto.RegisterRequest;
import com.jobtracker.job_application_tracker.model.User;
import com.jobtracker.job_application_tracker.repository.UserRepository;
import com.jobtracker.job_application_tracker.security.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest registerRequest){

        if(userRepository.findByEmail(registerRequest.getEmail()).isPresent()){
            throw new RuntimeException("email already taken");
        }
        User newUser=new User();
        newUser.setEmail((registerRequest.getEmail()));
        String encodedPassword=passwordEncoder.encode(registerRequest.getPassword());
        newUser.setPasswordHash(encodedPassword);
        userRepository.save((newUser));
        //generate token
        String token=jwtService.generateToken(newUser.getEmail());

        // 5. Wrap it in the Response DTO and return it
        return new AuthResponse(token);
    }
    public AuthResponse login(LoginRequest loginRequest){
        User user=userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()-> new UsernameNotFoundException("user not found"));

        if(!passwordEncoder.matches(loginRequest.getPassword(),user.getPasswordHash()))
            throw new RuntimeException("Invalid password");
        String token= jwtService.generateToken(user.getEmail());
        return new AuthResponse(token);
    }
}
