package com.twenty4.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseObject<T> {

    private Integer status;

    private String code;

    private T data;

    public void prepareHttpStatus(HttpStatus httpStatus){
        status = httpStatus.value();
        code = httpStatus.getReasonPhrase();
    }

}
