package com.helloworldweb.helloworld_guestbook.jwt;

import com.helloworldweb.helloworld_guestbook.domain.User;
import com.helloworldweb.helloworld_guestbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class JwtUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        User user = User.builder().email(userEmail).build();
        return user;
    }
}
