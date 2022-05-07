package com.twenty4.security.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        Map<String, Object> errorObject = new HashMap<String, Object>();
        int errorCode = 401;
        errorObject.put("message", "Access Denied");
        errorObject.put("error", HttpStatus.UNAUTHORIZED);
        errorObject.put("code", errorCode);
        errorObject.put("timestamp", new Timestamp(new Date().getTime()));
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorCode);
        response.getWriter().write(objectMapper.writeValueAsString(errorObject));
    }

}
