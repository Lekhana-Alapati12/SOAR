package com.example.SOARSpringBoot;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        CustomUserDetails userDetails=(CustomUserDetails) authentication.getPrincipal();
        String redirectUrl=request.getContextPath();
        if(userDetails.hasRole("Manager"))
        {
            redirectUrl="req-approval";
        }
        else if(userDetails.hasRole("Employee Admin"))
        {
            redirectUrl="emp-management";
        }
        else if(userDetails.hasRole("Infrastructure Admin"))
        {
            redirectUrl="invt-maintenance";
        }
        else if(userDetails.hasRole("Software Developer"))
        {
            redirectUrl="invt-request";
        }
        response.sendRedirect(redirectUrl);
    }
}
