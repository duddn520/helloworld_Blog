package com.helloworldweb.helloworld_guestbook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helloworldweb.helloworld_guestbook.dto.GuestBookCommentDto;
import com.helloworldweb.helloworld_guestbook.dto.GuestBookDto;
import com.helloworldweb.helloworld_guestbook.dto.UserDto;
import com.helloworldweb.helloworld_guestbook.jwt.JwtTokenService;
import com.helloworldweb.helloworld_guestbook.service.GuestBookService;
import com.helloworldweb.helloworld_guestbook.service.SyncService;
import com.helloworldweb.helloworld_guestbook.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional
public class GuestBookControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    MockMvc mvc;

    @Autowired
    GuestBookService guestBookService;

    @Autowired
    UserService userService;

    @Autowired
    JwtTokenService jwtTokenService;

    @Autowired
    SyncService syncService;


    @Test
    void registerGuestBookComment_Success() throws Exception {
        //given
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("email@email.com")
                .build();

        UserDto callerDto = UserDto.builder()
                .id(2L)
                .email("caller@email.com")
                .build();

        UserDto savedDto = userService.addUser(userDto);
        userService.addUser(callerDto);

        String token = jwtTokenService.createToken(String.valueOf(2L));

        GuestBookCommentDto guestBookCommentDto = GuestBookCommentDto.builder()
                .content("content")
                .build();

        String json = new ObjectMapper().writeValueAsString(guestBookCommentDto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/guestbook")
                .param("user_id",String.valueOf(savedDto.getId()))
                .cookie(new Cookie("Auth",token))
                .content(json)
                .contentType(MediaType.APPLICATION_JSON);

        //when
        mvc.perform(requestBuilder)
        //then
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    void registerGuestBookComment_Fail_NoJWT() throws Exception{
        //given
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("email@email.com")
                .build();

        UserDto callerDto = UserDto.builder()
                .id(2L)
                .email("caller@email.com")
                .build();

        UserDto savedDto = userService.addUser(userDto);
        userService.addUser(callerDto);

        GuestBookCommentDto guestBookCommentDto = GuestBookCommentDto.builder()
                .content("content")
                .build();

        String json = new ObjectMapper().writeValueAsString(guestBookCommentDto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/guestbook")
                .param("user_id",String.valueOf(savedDto.getId()))
                .content(json)
                .contentType(MediaType.APPLICATION_JSON);

        //when
        mvc.perform(requestBuilder)
        //then
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

//    @Test
//    void registerGuestBookComment_Fail_NotExistingUserId() throws Exception{
//        //given
//        UserDto callerDto = UserDto.builder()
//                .id(1L)
//                .email("caller@email.com")
//                .build();
//
//        userService.addUser(callerDto);
//
//        String token = jwtTokenService.createToken(String.valueOf(1L));
//
//        GuestBookCommentDto guestBookCommentDto = GuestBookCommentDto.builder()
//                .content("content")
//                .build();
//
//        String json = new ObjectMapper().writeValueAsString(guestBookCommentDto);
//
//        RequestBuilder requestBuilder = MockMvcRequestBuilders
//                .post("/api/guestbook")
//                .param("user_id",String.valueOf(999L))
//                .header("Auth",token)
//                .content(json)
//                .contentType(MediaType.APPLICATION_JSON);
//
//        //when
//        mvc.perform(requestBuilder)
//        //then
//                .andExpect(status().is4xxClientError())
//                .andDo(print());
//    }

    @Test
    void getGuestBook_Success() throws Exception {
        //given
        UserDto user1Dto = UserDto.builder()
                .id(1L)
                .email("email@email.com")
                .build();

        UserDto user2Dto = UserDto.builder()
                .id(2L)
                .email("123@email.com")
                .build();

        UserDto savedDto = userService.addUser(user1Dto);
        userService.addUser(user2Dto);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user2Dto.toEntity(),"",user2Dto.toEntity().getAuthorities()));

        GuestBookCommentDto guestBookCommentDto = GuestBookCommentDto.builder()
                        .content("content1")
                                .build();

        guestBookService.addGuestBookComment(savedDto.getId(),guestBookCommentDto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/guestbook")
                .param("user_id",String.valueOf(savedDto.getId()));

        //when
        mvc.perform(requestBuilder)
        //then
                .andExpect(status().isOk())
                .andDo(print());

    }

//    @Test
//    void getGuestBook_Fail_NotExistingUser() throws Exception {
//        //given
//        UserDto user1Dto = UserDto.builder()
//                .id(1L)
//                .email("email@email.com")
//                .build();
//
//        UserDto user2Dto = UserDto.builder()
//                .id(2L)
//                .email("123@email.com")
//                .build();
//
//        UserDto savedDto = userService.addUser(user1Dto);
//        userService.addUser(user2Dto);
//        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user2Dto.toEntity(),"",user2Dto.toEntity().getAuthorities()));
//
//        GuestBookCommentDto guestBookCommentDto = GuestBookCommentDto.builder()
//                .content("content1")
//                .build();
//
//        guestBookService.addGuestBookComment(savedDto.getId(),guestBookCommentDto);
//
//        RequestBuilder requestBuilder = MockMvcRequestBuilders
//                .get("/api/guestbook")
//                .param("user_id",String.valueOf(999L));
//
//        //when
//        mvc.perform(requestBuilder)
//        //then
//                .andExpect(status().is4xxClientError())
//                .andDo(print());
//    }

    @Test
    void updateGuestBookComment_Success() throws Exception {
        //given
        UserDto user1Dto = UserDto.builder()
                .id(1L)
                .email("email@email.com")
                .build();

        UserDto user2Dto = UserDto.builder()
                .id(2L)
                .email("123@email.com")
                .build();

        UserDto savedDto = userService.addUser(user1Dto);
        userService.addUser(user2Dto);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user2Dto.toEntity(),"",user2Dto.toEntity().getAuthorities()));

        GuestBookCommentDto guestBookCommentDto = GuestBookCommentDto.builder()
                .content("content1")
                .build();

        GuestBookDto guestBookDto = guestBookService.addGuestBookComment(savedDto.getId(),guestBookCommentDto);

        GuestBookCommentDto savedCommentDto = guestBookDto.getGuestBookCommentDtos().get(0);

        String token = jwtTokenService.createToken(String.valueOf(2L));

        GuestBookCommentDto updateCommentDto = GuestBookCommentDto.builder()
                .id(savedCommentDto.getId())
                .content("updated")
                .reply(savedCommentDto.getReply())
                .userDto(savedCommentDto.getUserDto())
                .build();

        String json = new ObjectMapper().writeValueAsString(updateCommentDto);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/guestbook")
                .cookie(new Cookie("Auth",token))
                .content(json)
                .contentType(MediaType.APPLICATION_JSON);

        //when
        mvc.perform(requestBuilder)
        //then
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void updateGuestBookComment_Fail_NoJWT() throws Exception {
        //given
        UserDto user1Dto = UserDto.builder()
                .id(1L)
                .email("email@email.com")
                .build();

        UserDto user2Dto = UserDto.builder()
                .id(2L)
                .email("123@email.com")
                .build();

        UserDto savedDto = userService.addUser(user1Dto);
        userService.addUser(user2Dto);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user2Dto.toEntity(),"",user2Dto.toEntity().getAuthorities()));

        GuestBookCommentDto guestBookCommentDto = GuestBookCommentDto.builder()
                .content("content1")
                .build();

        GuestBookDto guestBookDto = guestBookService.addGuestBookComment(savedDto.getId(),guestBookCommentDto);

        GuestBookCommentDto savedCommentDto = guestBookDto.getGuestBookCommentDtos().get(0);

        GuestBookCommentDto updateCommentDto = GuestBookCommentDto.builder()
                .id(savedCommentDto.getId())
                .content("updated")
                .reply(savedCommentDto.getReply())
                .userDto(savedCommentDto.getUserDto())
                .build();

        String json = new ObjectMapper().writeValueAsString(updateCommentDto);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/guestbook")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON);

        //when
        mvc.perform(requestBuilder)
        //then
                .andExpect(status().is4xxClientError())
                .andDo(print());

    }

    @Test
    void updateGuestbookComment_Fail_IllegalCaller() throws Exception {
        //given
        UserDto user1Dto = UserDto.builder()
                .id(1L)
                .email("email@email.com")
                .build();

        UserDto user2Dto = UserDto.builder()
                .id(2L)
                .email("123@email.com")
                .build();

        UserDto savedDto = userService.addUser(user1Dto);
        userService.addUser(user2Dto);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user2Dto.toEntity(),"",user2Dto.toEntity().getAuthorities()));

        GuestBookCommentDto guestBookCommentDto = GuestBookCommentDto.builder()
                .content("content1")
                .build();

        GuestBookDto guestBookDto = guestBookService.addGuestBookComment(savedDto.getId(),guestBookCommentDto);

        GuestBookCommentDto savedCommentDto = guestBookDto.getGuestBookCommentDtos().get(0);

        String token = jwtTokenService.createToken(String.valueOf(1L));

        GuestBookCommentDto updateCommentDto = GuestBookCommentDto.builder()
                .id(savedCommentDto.getId())
                .content("updated")
                .reply(savedCommentDto.getReply())
                .userDto(savedCommentDto.getUserDto())
                .build();

        String json = new ObjectMapper().writeValueAsString(updateCommentDto);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/guestbook")
                .cookie(new Cookie("Auth",token))
                .content(json)
                .contentType(MediaType.APPLICATION_JSON);

        //when
        mvc.perform(requestBuilder)
        //then
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void deleteGuestBookComment_Success() throws Exception {
        //given
        UserDto user1Dto = UserDto.builder()
                .id(1L)
                .email("email@email.com")
                .build();

        UserDto user2Dto = UserDto.builder()
                .id(2L)
                .email("123@email.com")
                .build();

        UserDto savedDto = userService.addUser(user1Dto);
        userService.addUser(user2Dto);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user2Dto.toEntity(),"",user2Dto.toEntity().getAuthorities()));

        GuestBookCommentDto guestBookCommentDto = GuestBookCommentDto.builder()
                .content("content1")
                .build();

        GuestBookDto guestBookDto = guestBookService.addGuestBookComment(savedDto.getId(),guestBookCommentDto);

        GuestBookCommentDto savedCommentDto = guestBookDto.getGuestBookCommentDtos().get(0);

        String token = jwtTokenService.createToken(String.valueOf(2L));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/guestbook")
                .param("guestbook_comment_id",String.valueOf(savedCommentDto.getId()))
                .cookie(new Cookie("Auth",token));

        //when
        mvc.perform(requestBuilder)
        //then
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    void deleteGuestBookComment_Fail_NoJWT() throws Exception {
        //given
        UserDto user1Dto = UserDto.builder()
                .id(1L)
                .email("email@email.com")
                .build();

        UserDto user2Dto = UserDto.builder()
                .id(2L)
                .email("123@email.com")
                .build();

        UserDto savedDto = userService.addUser(user1Dto);
        userService.addUser(user2Dto);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user2Dto.toEntity(),"",user2Dto.toEntity().getAuthorities()));

        GuestBookCommentDto guestBookCommentDto = GuestBookCommentDto.builder()
                .content("content1")
                .build();

        GuestBookDto guestBookDto = guestBookService.addGuestBookComment(savedDto.getId(),guestBookCommentDto);

        GuestBookCommentDto savedCommentDto = guestBookDto.getGuestBookCommentDtos().get(0);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/guestbook")
                .param("guestbook_comment_id",String.valueOf(savedCommentDto.getId()));

        //when
        mvc.perform(requestBuilder)
                //then
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void deleteGuestBookComment_Fail_IllegalCaller() throws Exception {
        //given
        UserDto user1Dto = UserDto.builder()
                .id(1L)
                .email("email@email.com")
                .build();

        UserDto user2Dto = UserDto.builder()
                .id(2L)
                .email("123@email.com")
                .build();

        UserDto savedDto = userService.addUser(user1Dto);
        userService.addUser(user2Dto);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user2Dto.toEntity(),"",user2Dto.toEntity().getAuthorities()));

        GuestBookCommentDto guestBookCommentDto = GuestBookCommentDto.builder()
                .content("content1")
                .build();

        GuestBookDto guestBookDto = guestBookService.addGuestBookComment(savedDto.getId(),guestBookCommentDto);

        GuestBookCommentDto savedCommentDto = guestBookDto.getGuestBookCommentDtos().get(0);

        String token = jwtTokenService.createToken(String.valueOf(1L));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/guestbook")
                .param("guestbook_comment_id",String.valueOf(savedCommentDto.getId()))
                .cookie(new Cookie("Auth",token));

        //when
        mvc.perform(requestBuilder)
                //then
                .andExpect(status().is4xxClientError())
                .andDo(print());

    }


}
