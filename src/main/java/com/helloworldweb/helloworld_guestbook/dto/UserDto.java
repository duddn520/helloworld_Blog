package com.helloworldweb.helloworld_guestbook.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserDto {
    private Long id;
    private String email;
    private String profileUrl;
    private String nickName;

}
