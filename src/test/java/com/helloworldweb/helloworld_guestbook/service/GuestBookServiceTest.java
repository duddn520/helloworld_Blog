package com.helloworldweb.helloworld_guestbook.service;

import com.helloworldweb.helloworld_guestbook.domain.GuestBook;
import com.helloworldweb.helloworld_guestbook.domain.GuestBookComment;
import com.helloworldweb.helloworld_guestbook.domain.User;
import com.helloworldweb.helloworld_guestbook.dto.GuestBookCommentDto;
import com.helloworldweb.helloworld_guestbook.repository.GuestBookCommentRepository;
import com.helloworldweb.helloworld_guestbook.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class GuestBookServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    GuestBookCommentRepository guestBookCommentRepository;

    @InjectMocks
    GuestBookServiceImpl guestBookService;

    private static final GuestBook testGuestBook1 = GuestBook.builder()
            .id(3L)
            .guestBookComments(new ArrayList<>())
            .build();
    private static final GuestBook testGuestBook2 = GuestBook.builder()
            .id(4L)
            .guestBookComments(new ArrayList<>())
            .build();
    private static final String testUser1Email = "123@email.com";
    private static final String testUser2Email = "456@email.com";
    private static final User testUser1 = User.builder()
            .id(1L)
            .email(testUser1Email)
            .guestBook(testGuestBook1)
            .build();
    private static final User testUser2 = User.builder()
            .id(2L)
            .email(testUser2Email)
            .guestBook(testGuestBook2)
            .build();

    private static final GuestBookCommentDto testGuestBookComment1Dto = GuestBookCommentDto.builder()
            .id(5L)
            .content("content1")
            .reply("reply1")
            .userId(2L)
            .guestBookId(3L)
            .build();

    private static final GuestBookCommentDto testGuestBookComment2Dto = GuestBookCommentDto.builder()
            .id(6L)
            .content("content2")
            .reply("reply2")
            .build();

    @Test
    void 방명록작성_성공(){
        //given
        //testGuestBookComment2 Dto(연관관계 등록 전의 Dto 주어진 경우)
        //방명록 주인 => testUser1, 작성자 => testUser2
        //user 등록 시 GuestBook은 매핑되어있다고 가정.
        testGuestBook1.updateUser(testUser1);
        testGuestBook2.updateUser(testUser2);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(testUser1));
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(testUser2));

        //when
        guestBookService.addGuestBookComment(1L,testGuestBookComment2Dto,testUser2Email);

        //then
        //작성자 (testuser2, testGuestBook2의 주인) 가 올바르게 매핑되었는지 확인
        assertEquals(testGuestBook2.getUser().getId(),testUser1.getGuestBook().getGuestBookComments().get(0).getUser().getId());

        //user <-> GuestBook 간의 연관관계 확인
        assertEquals(testUser1.getId(),testUser1.getGuestBook().getUser().getId());
        assertEquals(testUser2.getId(),testUser1.getGuestBook().getGuestBookComments().get(0).getUser().getId());

        //내용물 확인 ( 2번 유저의 작성 게시물의 content와, 1번 유저 방명록에 적힌 content 비교)
        assertEquals(testUser1.getGuestBook().getGuestBookComments().get(0).getContent(),testUser2.getGuestBookComments().get(0).getContent());
    }

    @Test
    void 방명록작성_잘못된_방명록주인ID(){
        //given
        when(userRepository.findById(any(Long.class))).thenThrow(new NoSuchElementException("해당 유저가 존재하지 않습니다."));

        //when
        //then
        assertThrows(NoSuchElementException.class,()->guestBookService.addGuestBookComment(1L,testGuestBookComment2Dto,testUser2Email));
    }

    @Test
    void 방명록작성_잘못된_방명록작성자ID(){
        //given
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(testUser1));
        when(userRepository.findByEmail(any(String.class))).thenThrow(new NoSuchElementException("해당 유저가 존재하지 않습니다."));

        //when
        //then
        assertThrows(NoSuchElementException.class,()->guestBookService.addGuestBookComment(1L,testGuestBookComment2Dto,testUser2Email));

    }

    @Test
    void 방명록수정_성공(){
        //given
        //testUser2 가 이미 작성한 testUser1의 방명록, testUser2가 수정시.
        testGuestBook1.updateUser(testUser1);
        GuestBookComment existGuestBookComment = GuestBookComment.builder()
                .id(5L)
                .content("content")
                .reply("reply")
                .user(testUser2)
                .build();
        existGuestBookComment.updateGuestBook(testGuestBook1);
        testUser2.getGuestBookComments().add(existGuestBookComment);

        when(guestBookCommentRepository.findGuestBookCommentWithUserById(any(Long.class))).thenReturn(Optional.of(existGuestBookComment));
        //when
        GuestBookCommentDto guestBookCommentDto = guestBookService.updateGuestBookComment(testGuestBookComment1Dto,testUser2Email);

        //then
        //내용물 확인
        assertEquals(guestBookCommentDto.getContent(),"content1");
        assertEquals(guestBookCommentDto.getReply(),"reply1");

        //작성자 변경여부 확인
        assertEquals(guestBookCommentDto.getUserId(),testUser2.getId());

    }

    @Test
    void 방명록수정_잘못된_수정자Email(){

        //given
        //when
        //then
        assertThrows(NoSuchElementException.class,()->guestBookService.updateGuestBookComment(testGuestBookComment1Dto,testUser2Email));

    }

    @Test
    void 방명록수정_잘못된_GuestBookCommentID(){

        //given
        when(guestBookCommentRepository.findGuestBookCommentWithUserById(any(Long.class))).thenThrow(new NoSuchElementException("해당 방명록이 존재하지 않습니다."));

        //when
        //then
        assertThrows(NoSuchElementException.class,()->guestBookService.updateGuestBookComment(testGuestBookComment1Dto,testUser2Email));
    }

    @Test
    void 방명록수정_수정자_작성자불일치(){

        //given
        testGuestBook1.updateUser(testUser1);
        GuestBookComment existGuestBookComment = GuestBookComment.builder()
                .id(5L)
                .content("content")
                .reply("reply")
                .user(testUser2)
                .build();
        existGuestBookComment.updateGuestBook(testGuestBook1);
        testUser2.getGuestBookComments().add(existGuestBookComment);
        //when (testUser2가 작성한 방명록을 testUser1이 수정 시도.)
        //then
        assertThrows(NoSuchElementException.class,()->guestBookService.updateGuestBookComment(testGuestBookComment1Dto,testUser1Email));

    }

    @Test
    void 방명록삭제_잘못된_방명록ID(){
        //given
        when(guestBookCommentRepository.findGuestBookCommentWithUserById(any(Long.class))).thenThrow(new NoSuchElementException("해당 방명록이 존재하지 않습니다."));
        //when
        //then
        assertThrows(NoSuchElementException.class,()->guestBookService.deleteGuestBookComment(5L,testUser1Email));

    }

    @Test
    void 방명록삭제_잘못된_요청자Email(){
        //given
        testGuestBook1.updateUser(testUser1);
        GuestBookComment existGuestBookComment = GuestBookComment.builder()
                .id(5L)
                .content("content")
                .reply("reply")
                .user(testUser2)
                .build();
        existGuestBookComment.updateGuestBook(testGuestBook1);
        testUser2.getGuestBookComments().add(existGuestBookComment);
        when(guestBookCommentRepository.findGuestBookCommentWithUserById(any(Long.class))).thenReturn(Optional.of(existGuestBookComment));

        //when
        //then
        assertThrows(IllegalCallerException.class,()->guestBookService.deleteGuestBookComment(5L,testUser1Email));

    }
    @Test
    void 방명록삭제_작성자_요청자비일치(){

        //given
        //testuser1의 방명록, 거기에 글 쓴 testuser2
        testGuestBook1.updateUser(testUser1);
        GuestBookComment existGuestBookComment = GuestBookComment.builder()
                .id(5L)
                .content("content")
                .reply("reply")
                .user(testUser2)
                .build();
        existGuestBookComment.updateGuestBook(testGuestBook1);
        testUser2.getGuestBookComments().add(existGuestBookComment);

        when(guestBookCommentRepository.findGuestBookCommentWithUserById(any(Long.class))).thenReturn(Optional.of(existGuestBookComment));
        //when
        //then
        assertThrows(IllegalCallerException.class,()->guestBookService.deleteGuestBookComment(5L,testUser1Email));
    }


}
