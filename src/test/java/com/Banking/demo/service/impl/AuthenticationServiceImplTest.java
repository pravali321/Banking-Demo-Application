package com.Banking.demo.service.impl;

import com.Banking.demo.entity.Role;
import com.Banking.demo.entity.User;
import com.Banking.demo.mapper.UserMapper;
import com.Banking.demo.model.authentication.AuthenticationResponseModel;
import com.Banking.demo.model.authentication.LoginRequestModel;
import com.Banking.demo.model.authentication.RegisterRequestModel;
import com.Banking.demo.repository.UserRepository;
import com.Banking.demo.security.JwtService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private User user;
    private RegisterRequestModel registerRequest;
    private LoginRequestModel loginRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        authenticationService = new AuthenticationServiceImpl(userRepository, jwtService, userMapper, authenticationManager);

        registerRequest = RegisterRequestModel
                .builder()
                .name("Pravalika Narasinggoji")
                .email("pravalika@gmail.com")
                .phone("01552422396")
                .password("123456")
                .build();

        loginRequest = LoginRequestModel
                .builder()
                .email("pravalika@gmail.com")
                .password("123456")
                .build();

        user = User.
                builder()
                .id(1L)
                .name("Pravalika Narasinggoji")
                .email("pravalika@gmail.com")
                .phone("01552422396")
                .password("123456")
                .role(Role.USER)
                .build();
    }

    @Test
    public void registerShouldRegisterUserSuccessfully() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByPhone(registerRequest.getPhone())).thenReturn(false);
        when(userMapper.toUser(registerRequest)).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("token");
        when(userRepository.save(user)).thenReturn(user);

        AuthenticationResponseModel response = authenticationService.register(registerRequest);

        assertThat(response.getToken()).isNotEmpty();
        assertThat(response.getToken()).isEqualTo("token");

        verify(userRepository, times(1)).existsByEmail(registerRequest.getEmail());
        verify(userRepository, times(1)).existsByPhone(registerRequest.getPhone());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void registerShouldThrowEntityExistsExceptionWhenEmailExists() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);


        assertThatThrownBy(() -> authenticationService.register(registerRequest))
                .isInstanceOf(EntityExistsException.class);

        verify(userRepository, times(1)).existsByEmail(registerRequest.getEmail());
        verify(userRepository, times(0)).existsByPhone(registerRequest.getPhone());
        verify(userRepository, times(0)).save(user);
    }

    @Test
    public void registerShouldThrowEntityExistsExceptionWhenEmailNotExistsButPhoneExists() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByPhone(registerRequest.getPhone())).thenReturn(true);


        assertThatThrownBy(() -> authenticationService.register(registerRequest))
                .isInstanceOf(EntityExistsException.class);

        verify(userRepository, times(1)).existsByEmail(registerRequest.getEmail());
        verify(userRepository, times(1)).existsByPhone(registerRequest.getPhone());
        verify(userRepository, times(0)).save(user);
    }

    @Test
    public void loginShouldLoginUserSuccessfully() {
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("token");

        AuthenticationResponseModel response = authenticationService.login(loginRequest);

        assertThat(response.getToken()).isNotEmpty();
        assertThat(response.getToken()).isEqualTo("token");

        verify(userRepository, times(1)).findByEmail(loginRequest.getEmail());
    }

    @Test
    public void loginShouldThrowEntityNotFoundExceptionWhenUserDoesNotExist() {
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authenticationService.login(loginRequest))
                .isInstanceOf(EntityNotFoundException.class);

        verify(userRepository, times(1)).findByEmail(loginRequest.getEmail());
    }

    @Test
    public void loginShouldThrowAuthenticationExceptionWhenBadCredentials() {
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        )).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> authenticationService.login(loginRequest))
                .isInstanceOf(RuntimeException.class);
    }
}