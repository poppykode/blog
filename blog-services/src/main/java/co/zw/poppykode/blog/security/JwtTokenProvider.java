package co.zw.poppykode.blog.security;

import co.zw.poppykode.blog.exception.BlogAPIException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Date;

import static co.zw.poppykode.blog.utils.AppConstants.ISSUER;


@Component
public class JwtTokenProvider {
    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app.jwt-expiration-milliseconds}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication){
             SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
             securityUser.getAuthorities();
             securityUser.getUsername();
             String username = authentication.getName();
             Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
             Date currentDate = new Date();
             Date expireDate =new Date(currentDate.getTime() + jwtExpirationInMs);
           String token = Jwts.builder()
                   .setSubject(username)
                   .claim("authorities",securityUser.getAuthorities())
                   .setIssuer(ISSUER)
                   .setIssuedAt(new Date())
                   .setExpiration(expireDate)
                   .signWith(key())
                   .compact();
            return token;
}

    public String getUsernameFromJWT(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public Boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .requireIssuer(ISSUER)
                    .build()
                    .parse(token);
            return true;
        } catch (SecurityException ex) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Invalid Jwt signature");
        } catch (MalformedJwtException ex) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Invalid Jwt token");
        } catch (ExpiredJwtException ex) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Expired Jwt token");
        } catch (UnsupportedJwtException ex) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Unsupported Jwt token");
        } catch (IllegalArgumentException ex) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Jwt claims string is empty");
        } catch(JwtException ex){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Something went wrong " + ex.getMessage());
        }
    }
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }


}
