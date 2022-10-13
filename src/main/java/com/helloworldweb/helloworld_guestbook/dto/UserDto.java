package com.helloworldweb.helloworld_guestbook.dto;

import com.helloworldweb.helloworld_guestbook.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserDto {
    private Long id;
    private String email;
    private String profileUrl;
    private String nickName;

    public UserDto(User user)
    {
        this.id = user.getId();
        this.email = user.getEmail();
        this.profileUrl = user.getProfileUrl();
        this.nickName = user.getNickName();
    }

    public User toEntity(){
        return User.builder()
                .email(this.email)
                .nickName(this.nickName)
                .profileUrl(this.profileUrl)
                .build();
    }

}
