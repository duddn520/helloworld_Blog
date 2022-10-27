package com.helloworldweb.helloworld_guestbook.service;

import com.helloworldweb.helloworld_guestbook.domain.User;
import com.helloworldweb.helloworld_guestbook.dto.GuestBookCommentDto;
import com.helloworldweb.helloworld_guestbook.dto.GuestBookDto;

public interface GuestBookService {

    /** C
     * GuestBook에 GuestBookComment를 등록하는 함수.
     * @param userId - 방명록의 주인 user ID
     * @param guestBookCommentDto - 등록하려는 GuestBookComment의 정보가 담긴 Dto 객체.
     * @return GuestBook 객체를 Dto화 하여 리턴.
     */
    GuestBookDto addGuestBookComment(Long userId, GuestBookCommentDto guestBookCommentDto);

    /** R
     * GuestBook 객체를 조회하는 함수. GuestBook의 ID를 통해 조회한다.
     * @param userId - GuestBook을 조회할 User의 ID
     * @return GuestBook 객체를 Dto 화 하여 리턴.
     */
    GuestBookDto getGuestBook(Long userId);

    /** U
     * GuestBookComment 객체를 수정하는 함수.
     * @param guestBookCommentDto - 수정할 GuestBookComment의 Dto 형태
     * @return 수정한 GuestBookComment 객체 Dto 화 하여 리턴.
     */
    GuestBookCommentDto updateGuestBookComment(GuestBookCommentDto guestBookCommentDto);

    /** D
     * GuestBookComment 객체를 삭제하는 함수.
     * @param guestBookCommentId - 삭제할 GuestBookComment 객체의 ID.
     */
    void deleteGuestBookComment(Long guestBookCommentId);


}
