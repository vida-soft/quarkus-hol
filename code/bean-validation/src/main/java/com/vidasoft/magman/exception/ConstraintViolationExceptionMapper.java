package com.vidasoft.magman.exception;

import com.vidasoft.magman.validator.ViolationMessage;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.LinkedList;
import java.util.List;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(buildResponseContent(e))
                .build();
    }

    private List<ViolationMessage> buildResponseContent(ConstraintViolationException violationEx) {
        var result = new LinkedList<ViolationMessage>();

        for (ConstraintViolation<?> violation : violationEx.getConstraintViolations()) {
            var property = violation.getPropertyPath().toString();
            var message = violation.getMessage();
            result.add(new ViolationMessage(property, message));
        }

        return result;
    }
}


