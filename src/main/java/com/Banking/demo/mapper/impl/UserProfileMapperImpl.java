package com.Banking.demo.mapper.impl;

import com.Banking.demo.entity.User;
import com.Banking.demo.mapper.UserProfileMapper;
import com.Banking.demo.model.authentication.UserProfileResponseModel;
import org.springframework.stereotype.Component;

@Component
public class UserProfileMapperImpl implements UserProfileMapper {
    @Override
    public UserProfileResponseModel toUserProfile(User user) {
        return UserProfileResponseModel
                .builder()
                .name(user.getName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .build();
    }
}
