package com.helloworldweb.helloworld_guestbook.controller;

import com.helloworldweb.helloworld_guestbook.model.ApiResponse;
import com.helloworldweb.helloworld_guestbook.model.HttpResponseMsg;
import com.helloworldweb.helloworld_guestbook.model.HttpStatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.NoResultException;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ControllerSupport {

    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<ApiResponse> handleClassCastException(){
        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.UNAUTHORIZED,
                HttpResponseMsg.NO_JWT), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalCallerException.class)
    public ResponseEntity<ApiResponse> handleIllegalCallerException(){
        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.UNAUTHORIZED,
                HttpResponseMsg.FORBIDDEN), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse> handleNoSuchElementException(){
        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.BAD_REQUEST,
                HttpResponseMsg.NO_CONTENT), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<ApiResponse> handleNoResultException(){
        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.UNAUTHORIZED,
                HttpResponseMsg.NOT_FOUND_USER), HttpStatus.UNAUTHORIZED);
    }
}
