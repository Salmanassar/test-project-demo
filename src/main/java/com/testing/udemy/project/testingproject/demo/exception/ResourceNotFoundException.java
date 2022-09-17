package com.testing.udemy.project.testingproject.demo.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String massage) {
        super(massage);
    }

    public ResourceNotFoundException(String massage, Throwable throwable){
        super(massage,throwable);
    }
}
