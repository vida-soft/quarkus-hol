package com.vidasoft.magman.interceptors;

import com.vidasoft.magman.model.PublishedContent;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
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
