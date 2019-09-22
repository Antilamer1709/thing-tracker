package com.antilamer.thingTracker.service;

import com.antilamer.thingTracker.dto.HostDTO;
import com.antilamer.thingTracker.dto.JwtAuthenticationResponseDTO;
import com.antilamer.thingTracker.dto.RegistrationDTO;
import com.antilamer.thingTracker.dto.UserDTO;
import com.antilamer.thingTracker.enums.UserRole;
import com.antilamer.thingTracker.exception.ApplicationException;
import com.antilamer.thingTracker.exception.UnauthorizedException;
import com.antilamer.thingTracker.exception.ValidationException;
import com.antilamer.thingTracker.model.UserEntity;

import java.util.Collection;

public interface AuthenticationBO {

    UserEntity getLoggedUser();

    UserDTO getLoggedUserDTO();

    void registerUser(RegistrationDTO registrationDTO) throws ValidationException;

    void addUserRoles(UserEntity user, UserRole... userRoles);

    void checkUserAccess(UserEntity user) throws UnauthorizedException;

    void checkAdminAccess(UserEntity user) throws UnauthorizedException;

    void checkUserAccess(Collection<UserEntity> users) throws UnauthorizedException;

    JwtAuthenticationResponseDTO login(UserDTO userDTO);

    String getGitProperties();

    HostDTO getHostInfo() throws ApplicationException;
}