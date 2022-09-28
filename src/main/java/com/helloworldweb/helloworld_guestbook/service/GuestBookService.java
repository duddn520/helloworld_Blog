package com.helloworldweb.helloworld_guestbook.service;

import com.helloworldweb.helloworld_guestbook.domain.User;
import com.helloworldweb.helloworld_guestbook.dto.GuestBookCommentDto;
import com.helloworldweb.helloworld_guestbook.dto.GuestBookDto;

public interface GuestBookService {

    /** C
     * GuestBook에 GuestBookComment를 등록하는 함수.
     * @param userId - GuestBookComent를 등록하려는 GuestBook의 주인 ID
     * @param guestBookCommentDto - 등록하려는 GuestBookComment의 정보가 담긴 Dto 객체.
     * @param email - 방명록을 작성한 유저의 Email.
     * @return GuestBook 객체를 Dto화 하여 리턴.
     */
    GuestBookDto addGuestBookComment(Long userId, GuestBookCommentDto guestBookCommentDto, String email);

    /** R
     * GuestBook 객체를 조회하는 함수. GuestBook의 ID를 통해 조회한다.
     * @param userId - GuestBook을 조회할 User의 ID
     * @return GuestBook 객체를 Dto 화 하여 리턴.
     */
    GuestBookDto getGuestBook(Long userId);

    /** U
     * GuestBookComment 객체를 수정하는 함수.
     * @param guestBookCommentDto - 수정할 GuestBookComment의 Dto 형태
     * @param email - 수정 요청자 Email, 작성자와 요청자 비교, 작성자는 수정 가능.
     * @return 수정한 GuestBookComment 객체 Dto 화 하여 리턴.
     */
    GuestBookCommentDto updateGuestBookComment(GuestBookCommentDto guestBookCommentDto, String email);

    /** D
     * GuestBookComment 객체를 삭제하는 함수.
     * @param guestBookCommentId - 삭제할 GuestBookComment 객체의 ID.
     * @param email - 삭제 요청자 Email, 작성자와 요청자 비교, 작성자는 삭제 가능.
     */
    void deleteGuestBookComment(Long guestBookCommentId, String email);


}
