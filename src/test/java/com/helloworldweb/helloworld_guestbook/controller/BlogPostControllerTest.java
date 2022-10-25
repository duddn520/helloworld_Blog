package com.helloworldweb.helloworld_guestbook.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.helloworldweb.helloworld_guestbook.domain.GuestBook;
import com.helloworldweb.helloworld_guestbook.domain.User;
import com.helloworldweb.helloworld_guestbook.dto.BlogPostDto;
import com.helloworldweb.helloworld_guestbook.dto.GuestBookCommentDto;
import com.helloworldweb.helloworld_guestbook.dto.PostSubCommentDto;
import com.helloworldweb.helloworld_guestbook.dto.UserDto;
import com.helloworldweb.helloworld_guestbook.jwt.JwtTokenService;
import com.helloworldweb.helloworld_guestbook.service.BlogPostService;
import com.helloworldweb.helloworld_guestbook.service.GuestBookService;
import com.helloworldweb.helloworld_guestbook.service.PostSubCommentService;
import com.helloworldweb.helloworld_guestbook.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;
import java.net.http.HttpHeaders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class BlogPostControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    MockMvc mvc;

    @Autowired
    UserService userService;

    @Autowired
    BlogPostService blogPostService;

    @Autowired
    GuestBookService guestBookService;

    @Autowired
    PostSubCommentService postSubCommentService;

    @Autowired
    JwtTokenService jwtTokenService;

    @Test
    void registerBlogPost_Success() throws Exception {
        UserDto userDto = UserDto.builder()
                .email("email@email.com")
                .build();

        userService.addUser(userDto);

        String token = jwtTokenService.createToken("email@email.com");

        System.out.println("############################################");
        BlogPostDto blogPostDto = BlogPostDto.builder()
                .content("newcontent1!!!!!!")
                .title("title123123123123")
                .build();

        String json = new ObjectMapper().writeValueAsString(blogPostDto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/blogpost")
                .header("Auth",token)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void registerBlogPost_Fail_WithoutJWT() throws Exception {
        UserDto userDto = UserDto.builder()
                .email("email@email.com")
                .build();

        userService.addUser(userDto);

        String token = jwtTokenService.createToken("email@email.com");

        System.out.println("########################");

        BlogPostDto blogPostDto = BlogPostDto.builder()
                .content("newcontent1!!!!!!")
                .title("title123123123123")
                .build();

        String json = new ObjectMapper().writeValueAsString(blogPostDto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/blogpost")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(requestBuilder)
                .andExpect(status().is4xxClientError())
                .andDo(print());

    }
}
