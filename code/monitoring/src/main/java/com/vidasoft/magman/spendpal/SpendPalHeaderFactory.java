package com.vidasoft.magman.spendpal;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;

public class SpendPalHeaderFactory implements ClientHeadersFactory {

    @Inject
    @ConfigProperty(name = "spendpal.api.key")
    String spendpalApiKey;

    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> multivaluedMap, MultivaluedMap<String, String> multivaluedMap1) {
        MultivaluedMap<String, String> result = new MultivaluedHashMap<>();
        result.add("Authorization", spendpalApiKey);
        return result;
    }
}
