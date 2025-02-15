package com.synesthesia.spring_oauth2.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ValidationService {

    @Autowired
    private Validator validator;

    public <T> void validate(T request) {
        Set<ConstraintViolation<T>> validateException = validator.validate(request);
        if (!validateException.isEmpty()) {
            throw new ConstraintViolationException(validateException);
        }
    }

}
