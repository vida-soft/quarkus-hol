package com.vidasoft.magman.interceptors;

import com.vidasoft.magman.model.PublishedContent;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import java.time.LocalDateTime;

@Interceptor
@ModifiesContent
@Priority(Interceptor.Priority.APPLICATION)
public class ModifiesContentInterceptor {

    @AroundInvoke
    public Object contentModified(InvocationContext invocationContext) throws Exception {
        Object[] arguments = invocationContext.getParameters();
        for (var argument : arguments) {
            if (argument instanceof PublishedContent) {
                var content = (PublishedContent) argument;
                content.lastModified = LocalDateTime.now();
            }
        }

        return invocationContext.proceed();
    }
}
