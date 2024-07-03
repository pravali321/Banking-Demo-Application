package com.Banking.demo.mapper;

import com.Banking.demo.entity.User;
import com.Banking.demo.model.authentication.UserProfileResponseModel;

public interface UserProfileMapper {
    UserProfileResponseModel toUserProfile(User user);
}
