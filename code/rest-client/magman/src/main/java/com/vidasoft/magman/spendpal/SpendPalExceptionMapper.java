package com.vidasoft.magman.spendpal;

import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

import javax.ws.rs.core.Response;

public class SpendPalExceptionMapper implements ResponseExceptionMapper<SpendPalException> {

    @Override
    public SpendPalException toThrowable(Response response) {
        return new SpendPalException(response.getStatus(), response.getEntity());
    }
}
