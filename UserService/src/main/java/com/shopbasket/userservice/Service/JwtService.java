package com.shopbasket.userservice.Service;

import com.shopbasket.userservice.Entities.Customer;
import com.shopbasket.userservice.Entities.Employee;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "cbb0da25d527915bb05845811497ca3fd1172521dafc9cf4b660f2c497f3439e";
    public String extractUsername(String token) {
        return extractClaim(token,Claims::getSubject);
    }
    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public String generateToken(Employee employee){
        return generateToken(new HashMap<>(),employee);
    }
    public String generateToken(Map<String, Object> extraClaims, Employee employee){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .claim("id",employee.getId())
                .claim("username", employee.getUsername())
                .claim("role", employee.getRole())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String extractRole(String token) {
        String role = extractClaim(token, claims -> claims.get("role", String.class));
        return role;
    }

    public String generateTokenForCustomer(Customer customer) {
        return generateTokenForCustomer(new HashMap<>(),customer);
    }
    private  String generateTokenForCustomer(Map<String, Object> extraClaims, Customer customer) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .claim("id",customer.getId())
                .claim("username", customer.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
