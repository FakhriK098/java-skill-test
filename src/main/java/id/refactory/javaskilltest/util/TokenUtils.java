package id.refactory.javaskilltest.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class TokenUtils {
    private static final long serialVersionUID = -1122334455667788L;

    private static final long jwtExpired = 5 * 60 * 60 * 1000;

    @Value("${jwt.secret}")
    private String secret;


    public String getTokenUser(String token) {
        return getToken(token, Claims::getSubject);
    }

    public Date getExpiredDateToken(String token) {
        return getToken(token, Claims::getExpiration);
    }

    public @NonNull Boolean isExpiredToken(String token) {
        final Date expired = getExpiredDateToken(token);
        return expired.before(new Date());
    }

    public <T> T getToken(String token, @NonNull Function<Claims, T> claimsTFunction) {
        final Claims claims = getAllToken(token);
        return claimsTFunction.apply(claims);
    }

    public Claims getAllToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJwt(token).getBody();
    }

    public String generateToken(@NonNull UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpired))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}
