package com.helloworldweb.helloworld_guestbook.service;

import com.helloworldweb.helloworld_guestbook.domain.User;
import com.helloworldweb.helloworld_guestbook.dto.UserDto;

public interface UserService {

    /** C
     * User 회원가입 후 user객체를 반환한다. User객체 등록시 깡통 GuestBook 객체 또한 등록해줄 것.
     * @param userDto -> 유저 정보가 담긴 Dto.
     * @return UserDto -> 유저 객체의 Dto.
     */
    UserDto addUser(UserDto userDto);

    /** U
     * 회원가입 된 User객체의 정보 수정.
     * @param userDto - 수정할 User 정보가 담긴 Dto
     * @return 수정 이후의 User의 Dto
     */
    UserDto updateUser(UserDto userDto);

    /** D
     * 회원가입 된 User 삭제. cascade 고려할것.
     * @param userId - 삭제할 User의 ID
     */
    void deleteUser(Long userId);

}
