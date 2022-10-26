package com.helloworldweb.helloworld_guestbook.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenService jwtTokenService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = jwtTokenService.getTokenByHeader((HttpServletRequest) request);
        if (token != null && jwtTokenService.validateTokenWithDate(token)) {   // token 검증
            // TODO: 2022/10/25 요청 온 JWT의 email에 해당하는 유저가 DB에 존재하지 않는 경우(kafka 오류로 인해 동기화 안된 경우 혹은 그냥 JWT 오류)에 대한 처리 필요.
            Authentication auth = jwtTokenService.getAuthentication(token);    // 인증 객체 생성
            SecurityContextHolder.getContext().setAuthentication(auth); // SecurityContextHolder에 인증 객체 저장
        }
        chain.doFilter(request, response);
    }
}
