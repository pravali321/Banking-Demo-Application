package com.Banking.demo.mapper;

import com.Banking.demo.entity.User;
import com.Banking.demo.model.authentication.RegisterRequestModel;

public interface UserMapper {
    User toUser(RegisterRequestModel request);
}
