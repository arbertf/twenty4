package com.twenty4.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class ThrowException {

    public static void throwBadRequestApiException(ErrorCode errorCode, List<String> parameters){
        if(parameters!=null && !parameters.isEmpty())
            throw new BadRequestApiException(errorCode, HttpStatus.BAD_REQUEST, parameters);

        throw new BadRequestApiException(errorCode, HttpStatus.BAD_REQUEST);
    }

    public static void throwNotFoundApiException(ErrorCode errorCode){
        throw new NotFoundApiException(errorCode, HttpStatus.NOT_FOUND);
    }

    public static void throwConflictException(ErrorCode errorCode){
        throw new ConflictException(errorCode, HttpStatus.CONFLICT);
    }

    public static void throwInternalServerError(ErrorCode errorCode, Throwable e){
        throw new InternalException(errorCode.getReasonPhrase(), e);
    }
}
