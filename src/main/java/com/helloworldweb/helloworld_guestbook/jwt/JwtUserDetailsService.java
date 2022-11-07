package com.helloworldweb.helloworld_guestbook.jwt;

import com.helloworldweb.helloworld_guestbook.domain.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class JwtUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = User.builder().id(Long.valueOf(userId)).build();
        return user;
    }
}
