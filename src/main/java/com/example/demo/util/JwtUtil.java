package com.example.demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    private String secret="spring";

    Date dateAt=new Date(System.currentTimeMillis());
    /*One Hour*/
    Date dateExpired=new Date(System.currentTimeMillis()+1000*60*60);

    public Claims extractAllClaim(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
        final Claims claims=extractAllClaim(token);
        return claimsResolver.apply(claims);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String createToken(Map<String,Object> claims,String subject){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(dateAt)
                .setExpiration(dateExpired)
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();
    }

    public String generateToken(String username){
        Map<String, Object> claims=new HashMap<>();
        return createToken(claims,username);
    }

    public Boolean validateToken(String token, UserDetails userDetails){
        final String username=extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));

    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());    
    }

}
