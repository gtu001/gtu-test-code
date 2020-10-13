package gtu.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.programing.roger.springngBlog.exception.PostNotFoundException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

@Service
public class JwtProvider {

    private KeyStore keyStore;

    @PostConstruct
    public void init() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            //String path = Thread.currentThread().getContextClassLoader().getResource("").getPath(); 
//            InputStream resourceAsStream = getClass().getResourceAsStream("/springblog.jks");
            InputStream resourceAsStream = getClass().getResourceAsStream("/keystore.jks");
//            keyStore.load(resourceAsStream, "secret".toCharArray());
            keyStore.load(resourceAsStream, "123456".toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new PostNotFoundException("Exception occured while loading keystore");
        }

    }

    public String generateToken(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        String jwtt = Jwts.builder()
                .setSubject(principal.getUsername())
                .signWith(getPrivateKey())
                .compact();
        System.out.println("jwtt: " + jwtt);
        return jwtt;
    }

    private PrivateKey getPrivateKey() {
        try {
//            return (PrivateKey) keyStore.getKey("springblog", "secret".toCharArray());
            return (PrivateKey) keyStore.getKey("keyalias", "123456".toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new PostNotFoundException("Exception occured while retrieving public key from keystore");
        }
    }

    public boolean validateToken(String jwt) {
        Jwts.parser().setSigningKey(getPublickey()).parseClaimsJws(jwt);
        return true;
    }

    private PublicKey getPublickey() {
        try {
//            return keyStore.getCertificate("springblog").getPublicKey();
            return keyStore.getCertificate("keyalias").getPublicKey();
        } catch (KeyStoreException e) {
            throw new PostNotFoundException("Exception occured while retrieving public key from keystore");
        }
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(getPublickey())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}