package com.Banking.demo.service;

import com.Banking.demo.model.authentication.AuthenticationResponseModel;
import com.Banking.demo.model.authentication.LoginRequestModel;
import com.Banking.demo.model.authentication.RegisterRequestModel;

public interface AuthenticationService {
    public AuthenticationResponseModel register(RegisterRequestModel request);

    public AuthenticationResponseModel login(LoginRequestModel request);
}
