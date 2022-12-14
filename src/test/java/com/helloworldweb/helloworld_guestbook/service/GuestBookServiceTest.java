package com.helloworldweb.helloworld_guestbook.service;

import com.helloworldweb.helloworld_guestbook.domain.GuestBook;
import com.helloworldweb.helloworld_guestbook.domain.GuestBookComment;
import com.helloworldweb.helloworld_guestbook.domain.User;
import com.helloworldweb.helloworld_guestbook.dto.GuestBookCommentDto;
import com.helloworldweb.helloworld_guestbook.repository.GuestBookCommentRepository;
import com.helloworldweb.helloworld_guestbook.repository.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

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
            .build();

    private static final GuestBookCommentDto testGuestBookComment2Dto = GuestBookCommentDto.builder()
            .id(6L)
            .content("content2")
            .reply("reply2")
            .build();

    @BeforeEach
    void ContextHolder??????(){
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(testUser2,"",testUser2.getAuthorities()));
    }

    @Test
    void ???????????????_??????(){
        //given
        //testGuestBookComment2 Dto(???????????? ?????? ?????? Dto ????????? ??????)
        //????????? ?????? => testUser1, ????????? => testUser2
        //user ?????? ??? GuestBook??? ????????????????????? ??????.
        testGuestBook1.updateUser(testUser1);
        testGuestBook2.updateUser(testUser2);
        when(userRepository.findUserWithGuestBookWithGuestBookCommentsbyId(any(Long.class))).thenReturn(Optional.of(testUser1));
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(testUser2));

        //when
        guestBookService.addGuestBookComment(1L,testGuestBookComment2Dto);

        //then
        //????????? (testuser2, testGuestBook2??? ??????) ??? ???????????? ?????????????????? ??????
        assertEquals(testGuestBook2.getUser().getId(),testUser1.getGuestBook().getGuestBookComments().get(0).getUser().getId());

        //user <-> GuestBook ?????? ???????????? ??????
        assertEquals(testUser1.getId(),testUser1.getGuestBook().getUser().getId());
        assertEquals(testUser2.getId(),testUser1.getGuestBook().getGuestBookComments().get(0).getUser().getId());

        //????????? ?????? ( 2??? ????????? ?????? ???????????? content???, 1??? ?????? ???????????? ?????? content ??????)
        assertEquals(testUser1.getGuestBook().getGuestBookComments().get(0).getContent(),testUser2.getGuestBookComments().get(0).getContent());
    }

    @Test
    void ???????????????_?????????_???????????????ID(){
        //given
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(testUser2));
        when(userRepository.findUserWithGuestBookWithGuestBookCommentsbyId(any(Long.class))).thenThrow(new NoSuchElementException("?????? ????????? ???????????? ????????????."));

        //when
        //then
        assertThrows(NoSuchElementException.class,()->guestBookService.addGuestBookComment(999L,testGuestBookComment2Dto));
    }

    @Test
    void ???????????????_?????????_??????????????????ID(){
        //given
        when(userRepository.findById(any(Long.class))).thenThrow(new NoSuchElementException("?????? ????????? ???????????? ????????????."));

        //when
        //then
        assertThrows(NoSuchElementException.class,()->guestBookService.addGuestBookComment(1L,testGuestBookComment2Dto));

    }

    @Test
    void ???????????????_??????(){
        //given
        //testUser2 ??? ?????? ????????? testUser1??? ?????????, testUser2??? ?????????.
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
        GuestBookCommentDto guestBookCommentDto = guestBookService.updateGuestBookComment(testGuestBookComment1Dto);

        //then
        //????????? ??????
        assertEquals(guestBookCommentDto.getContent(),"content1");
        assertEquals(guestBookCommentDto.getReply(),"reply1");

        //????????? ???????????? ??????
        assertEquals(guestBookCommentDto.getUserDto().getId(),testUser2.getId());

    }

    @Test
    void ???????????????_?????????_?????????Email(){

        //given
        //when
        //then
        assertThrows(NoSuchElementException.class,()->guestBookService.updateGuestBookComment(testGuestBookComment1Dto));

    }

    @Test
    void ???????????????_?????????_GuestBookCommentID(){

        //given
        when(guestBookCommentRepository.findGuestBookCommentWithUserById(any(Long.class))).thenThrow(new NoSuchElementException("?????? ???????????? ???????????? ????????????."));

        //when
        //then
        assertThrows(NoSuchElementException.class,()->guestBookService.updateGuestBookComment(testGuestBookComment1Dto));
    }

    @Test
    void ???????????????_?????????_??????????????????(){

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
        //when (testUser2??? ????????? ???????????? testUser1??? ?????? ??????.)
        //then
        assertThrows(NoSuchElementException.class,()->guestBookService.updateGuestBookComment(testGuestBookComment1Dto));

    }

    @Test
    void ???????????????_?????????_?????????ID(){
        //given
        when(guestBookCommentRepository.findGuestBookCommentWithUserById(any(Long.class))).thenThrow(new NoSuchElementException("?????? ???????????? ???????????? ????????????."));
        //when
        //then
        assertThrows(NoSuchElementException.class,()->guestBookService.deleteGuestBookComment(5L));

    }
    @Test
    void ???????????????_?????????_??????????????????(){

        //given
        //testuser2??? ?????????, ????????? ??? ??? testuser1
        testGuestBook2.updateUser(testUser2);
        GuestBookComment existGuestBookComment = GuestBookComment.builder()
                .id(5L)
                .content("content")
                .reply("reply")
                .user(testUser1)
                .build();
        existGuestBookComment.updateGuestBook(testGuestBook2);
        testUser1.getGuestBookComments().add(existGuestBookComment);

        when(guestBookCommentRepository.findGuestBookCommentWithUserById(any(Long.class))).thenReturn(Optional.of(existGuestBookComment));
        //when
        //then
        assertThrows(IllegalCallerException.class,()->guestBookService.deleteGuestBookComment(5L));
    }


}
