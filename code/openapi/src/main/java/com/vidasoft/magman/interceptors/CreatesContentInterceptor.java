package com.vidasoft.magman.interceptors;

import com.vidasoft.magman.model.PublishedContent;
import jakarta.annotation.Priority;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import java.time.LocalDateTime;

@Interceptor
@CreatesContent
@Priority(Interceptor.Priority.APPLICATION)
public class CreatesContentInterceptor {

    @AroundInvoke
    public Object contentCreated(InvocationContext invocationContext) throws Exception {
        Object returnedObject = invocationContext.proceed();
        if (returnedObject instanceof PublishedContent) {
            var content = (PublishedContent) returnedObject;
            content.publishDate = LocalDateTime.now();
            content.lastModified = LocalDateTime.now();
        }

        return returnedObject;
    }

}
