package com.antilamer.thingTracker.config.security;

import com.antilamer.thingTracker.dto.UserDTO;
import com.antilamer.thingTracker.service.AuthenticationBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthenticationBO authenticationBO;

    @Autowired
    public CustomAuthenticationSuccessHandler(
            AuthenticationBO authenticationBO) {
        this.authenticationBO = authenticationBO;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        UserDTO user = authenticationBO.getLoggedUserDTO();
        SecurityUtils.sendResponse(response, HttpServletResponse.SC_OK, user);
    }
}