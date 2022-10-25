package com.helloworldweb.helloworld_guestbook.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;

@Service
public class JwtTokenService {

    private final UserDetailsService userDetailsService;
    private static final long TOKEN_VALID_TIME = 1000L * 60 * 60 * 10; //10시간
//    private static final long REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 60 * 24 * 14; //2주
    private String secret;
    private Key key;

    public JwtTokenService(
            UserDetailsService userDetailsService,
            @Value("${jwt.secret}") String secret) {
        this.userDetailsService = userDetailsService;
        this.secret = secret;
    }

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    public boolean verifyToken(String jwtToken){
        try{
            Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwtToken);
            System.out.println("claims = " + claims);
            long valid_time = claims.getBody().getExpiration().getTime() - claims.getBody().getIssuedAt().getTime();
            // refresh_token인지 확인
            return valid_time > TOKEN_VALID_TIME ? false : !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String createToken(String email) {
        Claims claims = Jwts.claims().setSubject(email);

        long now = (new Date()).getTime();
        Date validTime = new Date(now + TOKEN_VALID_TIME);

        return Jwts.builder()
                .setClaims(claims) // 데이터
                .setIssuedAt(new Date(now))   // 토큰 발행 일자
                .setExpiration(validTime) // 만료 기간
                .signWith(key, SignatureAlgorithm.HS256) // 암호화 알고리즘, secret 값
                .compact(); // Token 생성
    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }

    public String getUserEmail(String token)
    {
        return Jwts.parser().setSigningKey(key)
                .parseClaimsJws(token).getBody().getSubject();
    }

    public String getTokenByHeader(HttpServletRequest request) {
        return request.getHeader("Auth");
    }

    public boolean validateTokenWithDate(String token) {
        try {
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
