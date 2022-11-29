package com.helloworldweb.helloworld_guestbook.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenService jwtTokenService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // CORS 정책
        ( (HttpServletResponse) response ).setHeader("Access-Control-Allow-Origin","http://localhost:3000");
        ( (HttpServletResponse) response ).addHeader("Access-Control-Allow-Headers","token,Auth,Refresh,x-requested-with, content-type, content-length");
        ( (HttpServletResponse) response ).setHeader("Access-Control-Allow-Credentials","true");

        Cookie[] cookies = ((HttpServletRequest) request).getCookies();
        String token = null;
        if (cookies != null) {
            HashMap<String,String> cookieMap = new HashMap<>();
            for (Cookie c : cookies) {
                String key = c.getName();
                String value = c.getValue();
                cookieMap.put(key, value);
            }
            token = cookieMap.get("Auth");
        }

//        String token = jwtTokenService.getTokenByHeader((HttpServletRequest) request);
        if (token != null && jwtTokenService.validateTokenWithDate(token)) {   // token 검증
            Authentication auth = jwtTokenService.getAuthentication(token);    // 인증 객체 생성
            SecurityContextHolder.getContext().setAuthentication(auth); // SecurityContextHolder에 인증 객체 저장
        }
        chain.doFilter(request, response);
    }
}
