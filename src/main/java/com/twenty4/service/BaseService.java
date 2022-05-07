package com.twenty4.service;

import com.twenty4.exception.ErrorCode;
import com.twenty4.exception.ThrowException;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

@Log4j2
public class BaseService<T> {

    protected void isPresent(Optional object){
        if(!object.isPresent()){
            ThrowException.throwBadRequestApiException(ErrorCode.REQUESTED_DATA_NOT_FOUND, null);
        }
    }

    protected void isPresent(Optional object, List<String> parameters){
        if(!object.isPresent()){
            ThrowException.throwBadRequestApiException(ErrorCode.REQUESTED_DATA_NOT_FOUND, parameters);
        }
    }

    protected final String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}
