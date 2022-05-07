package com.twenty4.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseObject {

    private String timestamp;

    private Integer status;

    private String error;

    private String message;

    private String path;
}
