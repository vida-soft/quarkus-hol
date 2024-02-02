package com.vidasoft.magman.security;

import com.vidasoft.magman.model.Author;
import com.vidasoft.magman.model.Manager;
import com.vidasoft.magman.model.Subscriber;
import com.vidasoft.magman.model.User;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import jakarta.annotation.PostConstruct;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claims;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

@ApplicationScoped
public class JwtService {

    private static final Map<Class<? extends User>, String> ROLE_MAP = Map.of(
            Author.class, Author.ROLE_NAME,
            Subscriber.class, Subscriber.ROLE_NAME,
            Manager.class, Manager.ROLE_NAME
    );

    private PrivateKey privateKey;

    @Inject
    @ConfigProperty(name = "mp.jwt.verify.privatekey.location")
    String keyLocation;

    @PostConstruct
    public void initializePrivateKey() {
        try {
            privateKey = readPrivateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String generateJWT(User user) {

        long currentTimeInSeconds = System.currentTimeMillis() / 1000;

        JwtClaimsBuilder claimsBuilder = Jwt.claims();
        claimsBuilder.issuer("https://localhost");
        claimsBuilder.upn(user.id + "");
        claimsBuilder.subject(ROLE_MAP.get(user.getClass()));
        claimsBuilder.issuedAt(currentTimeInSeconds);
        claimsBuilder.expiresAt(currentTimeInSeconds + 1800); // 30 minutes
        claimsBuilder.groups(ROLE_MAP.get(user.getClass()));

        claimsBuilder.claim(Claims.auth_time.name(), currentTimeInSeconds);

        return claimsBuilder.jws().sign(privateKey);
    }

    private PrivateKey readPrivateKey() {
        try (InputStream contentIS = getKeyStream(keyLocation)) {
            byte[] tmp = new byte[4096];
            try {
                int length = contentIS.read(tmp);
                return decodePrivateKey(new String(tmp, 0, length));
            } catch (Exception ex) {
                throw new RuntimeException("Could not read private key", ex);
            }
        } catch (Exception e) {
            return null;
        }
    }

    private InputStream getKeyStream(String keyLocation) throws IOException {
        var key = new File(keyLocation);

        return key.exists() ? new FileInputStream(key) :
                this.getClass().getClassLoader().getResourceAsStream(keyLocation);
    }


    private static PrivateKey decodePrivateKey(final String pemEncoded) throws Exception{
        byte[] encodedBytes = toEncodedBytes(pemEncoded);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    private static byte[] toEncodedBytes(final String pemEncoded) {
        final String normalizedPem = removeBeginEnd(pemEncoded);
        return Base64.getDecoder().decode(normalizedPem);
    }

    private static String removeBeginEnd(String pem) {
        pem = pem.replaceAll("-----BEGIN (.*)-----", "");
        pem = pem.replaceAll("-----END (.*)----", "");
        pem = pem.replaceAll("\r\n", "");
        pem = pem.replaceAll("\n", "");
        return pem.trim();
    }

}
