package com.helloworldweb.helloworld_guestbook.service;

import com.helloworldweb.helloworld_guestbook.domain.User;
import com.helloworldweb.helloworld_guestbook.dto.UserDto;

public interface UserService {

    /**
     * User 회원가입 후 user객체를 반환한다. User객체 등록시 깡통 GuestBook 객체 또한 등록해줄 것.
     * @param userDto -> 유저 정보가 담긴 Dto.
     * @return UserDto -> 유저 객체의 Dto.
     */
    UserDto addUser(UserDto userDto);
}
