package com.helloworldweb.helloworld_guestbook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helloworldweb.helloworld_guestbook.dto.BlogPostDto;
import com.helloworldweb.helloworld_guestbook.dto.PostSubCommentDto;
import com.helloworldweb.helloworld_guestbook.dto.UserDto;
import com.helloworldweb.helloworld_guestbook.jwt.JwtTokenService;
import com.helloworldweb.helloworld_guestbook.service.BlogPostService;
import com.helloworldweb.helloworld_guestbook.service.PostSubCommentService;
import com.helloworldweb.helloworld_guestbook.service.SyncService;
import com.helloworldweb.helloworld_guestbook.service.UserService;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional
public class PostSubCommentControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    MockMvc mvc;

    @Autowired
    PostSubCommentService postSubCommentService;

    @Autowired
    UserService userService;

    @Autowired
    BlogPostService blogPostService;

    @Autowired
    JwtTokenService jwtTokenService;

    @Autowired
    SyncService syncService;

    @Test
    void createPostSubComment_Success() throws Exception {
        //given
        //user 회원가입 및 blogPost 작성.
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("email@email.com")
                .build();

        userService.addUser(userDto);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDto.toEntity(),"",userDto.toEntity().getAuthorities()));

        BlogPostDto blogPostDto = BlogPostDto.builder()
                .content("content")
                .title("title")
                .build();

        BlogPostDto savedBlogPostDto = blogPostService.addBlogPost(blogPostDto);

        PostSubCommentDto postSubCommentDto = PostSubCommentDto.builder()
                .content("subcomment!!!!!")
                .build();
        String json = new ObjectMapper().writeValueAsString(postSubCommentDto);

        String token = jwtTokenService.createToken(String.valueOf(1L));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/postsubcomment/new")
                .param("blogpost_id",String.valueOf(savedBlogPostDto.getId()))
                .content(json)
                .header("Auth",token)
                .contentType(MediaType.APPLICATION_JSON);

        //when
        mvc.perform(requestBuilder)
        //then
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void createPostSubComment_Fail_NoJWT() throws Exception {
        //given
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("email@email.com")
                .build();

        userService.addUser(userDto);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDto.toEntity(),"",userDto.toEntity().getAuthorities()));

        BlogPostDto blogPostDto = BlogPostDto.builder()
                .content("content")
                .title("title")
                .build();

        BlogPostDto savedBlogPostDto = blogPostService.addBlogPost(blogPostDto);

        PostSubCommentDto postSubCommentDto = PostSubCommentDto.builder()
                .content("subcomment!!!!!")
                .build();
        String json = new ObjectMapper().writeValueAsString(postSubCommentDto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/postsubcomment/new")
                .param("blogpost_id",String.valueOf(savedBlogPostDto.getId()))
                .content(json)
                .contentType(MediaType.APPLICATION_JSON);

        //when
        mvc.perform(requestBuilder)
                //then
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void createPostSubComment_Fail_NotFoundUser() throws Exception {
        //given
        //user 회원가입 및 blogPost 작성.
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("email@email.com")
                .build();

        userService.addUser(userDto);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDto.toEntity(),"",userDto.toEntity().getAuthorities()));

        BlogPostDto blogPostDto = BlogPostDto.builder()
                .content("content")
                .title("title")
                .build();

        BlogPostDto savedBlogPostDto = blogPostService.addBlogPost(blogPostDto);

        PostSubCommentDto postSubCommentDto = PostSubCommentDto.builder()
                .content("subcomment!!!!!")
                .build();
        String json = new ObjectMapper().writeValueAsString(postSubCommentDto);

        String token = jwtTokenService.createToken(String.valueOf(2L));
        //등록되지 않은 유저의 ID를 담은 jwt 요청

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/postsubcomment/new")
                .param("blogpost_id",String.valueOf(savedBlogPostDto.getId()))
                .content(json)
                .header("Auth",token)
                .contentType(MediaType.APPLICATION_JSON);

        //when
        mvc.perform(requestBuilder)
                //then
                .andExpect(status().is4xxClientError())
                .andDo(print());

    }

    @Test
    void addPostSubComment_Success() throws Exception {
        //given
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("email@email.com")
                .build();

        userService.addUser(userDto);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDto.toEntity(),"",userDto.toEntity().getAuthorities()));

        BlogPostDto blogPostDto = BlogPostDto.builder()
                .content("content")
                .title("title")
                .build();

        BlogPostDto savedBlogPostDto = blogPostService.addBlogPost(blogPostDto);

        PostSubCommentDto postSubCommentDto = PostSubCommentDto.builder()
                .content("subcomment!!!!!")
                .build();

        PostSubCommentDto savedSubCommentDto = postSubCommentService.createPostSubComment(savedBlogPostDto.getId(),postSubCommentDto);
        //이미 등록된 댓글이 있는 상태.

        PostSubCommentDto addSubCommentDto = PostSubCommentDto.builder()
                .postCommentId(savedSubCommentDto.getPostCommentId())
                .content("added comment!!!!!")
                .build();
        //추가할댓글

        String json = new ObjectMapper().writeValueAsString(addSubCommentDto);

        String token = jwtTokenService.createToken(String.valueOf(1L));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/postsubcomment/new")
                .param("blogpost_id",String.valueOf(savedBlogPostDto.getId()))
                .content(json)
                .header("Auth",token)
                .contentType(MediaType.APPLICATION_JSON);

        //when
        mvc.perform(requestBuilder)
                //then
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void addPostSubComment_Fail_NoJWT() throws Exception {
        //given
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("email@email.com")
                .build();

        userService.addUser(userDto);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDto.toEntity(),"",userDto.toEntity().getAuthorities()));

        BlogPostDto blogPostDto = BlogPostDto.builder()
                .content("content")
                .title("title")
                .build();

        BlogPostDto savedBlogPostDto = blogPostService.addBlogPost(blogPostDto);

        PostSubCommentDto postSubCommentDto = PostSubCommentDto.builder()
                .content("subcomment!!!!!")
                .build();

        PostSubCommentDto savedSubCommentDto = postSubCommentService.createPostSubComment(savedBlogPostDto.getId(),postSubCommentDto);
        //이미 등록된 댓글이 있는 상태.

        PostSubCommentDto addSubCommentDto = PostSubCommentDto.builder()
                .postCommentId(savedSubCommentDto.getPostCommentId())
                .content("added comment!!!!!")
                .build();
        //추가할댓글

        String json = new ObjectMapper().writeValueAsString(addSubCommentDto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/postsubcomment/new")
                .param("blogpost_id",String.valueOf(savedBlogPostDto.getId()))
                .content(json)
                .contentType(MediaType.APPLICATION_JSON);

        //when
        mvc.perform(requestBuilder)
                //then
                .andExpect(status().is4xxClientError())
                .andDo(print());

    }

    @Test
    void addPostSubComment_Fail_NotFoundUser() throws Exception {
        //given
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("email@email.com")
                .build();

        userService.addUser(userDto);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDto.toEntity(),"",userDto.toEntity().getAuthorities()));

        BlogPostDto blogPostDto = BlogPostDto.builder()
                .content("content")
                .title("title")
                .build();

        BlogPostDto savedBlogPostDto = blogPostService.addBlogPost(blogPostDto);

        PostSubCommentDto postSubCommentDto = PostSubCommentDto.builder()
                .content("subcomment!!!!!")
                .build();

        PostSubCommentDto savedSubCommentDto = postSubCommentService.createPostSubComment(savedBlogPostDto.getId(),postSubCommentDto);
        //이미 등록된 댓글이 있는 상태.

        PostSubCommentDto addSubCommentDto = PostSubCommentDto.builder()
                .postCommentId(savedSubCommentDto.getPostCommentId())
                .content("added comment!!!!!")
                .build();
        //추가할댓글

        String json = new ObjectMapper().writeValueAsString(addSubCommentDto);

        String token = jwtTokenService.createToken(String.valueOf(2L));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/postsubcomment/new")
                .param("blogpost_id",String.valueOf(savedBlogPostDto.getId()))
                .content(json)
                .header("Auth",token)
                .contentType(MediaType.APPLICATION_JSON);

        //when
        mvc.perform(requestBuilder)
                //then
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void getPostSubComment_Success() throws Exception {
        //given
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("email@email.com")
                .build();

        userService.addUser(userDto);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDto.toEntity(),"",userDto.toEntity().getAuthorities()));

        BlogPostDto blogPostDto = BlogPostDto.builder()
                .content("content")
                .title("title")
                .build();

        BlogPostDto savedBlogPostDto = blogPostService.addBlogPost(blogPostDto);

        PostSubCommentDto postSubCommentDto = PostSubCommentDto.builder()
                .content("subcomment!!!!!")
                .build();

        PostSubCommentDto savedSubCommentDto = postSubCommentService.createPostSubComment(savedBlogPostDto.getId(),postSubCommentDto);
        //이미 등록된 댓글이 있는 상태.

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/postsubcomment")
                .param("postsubcomment_id",String.valueOf(savedSubCommentDto.getId()));

        //when
        mvc.perform(requestBuilder)
                //then
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getPostSubComment_Fail_NoContent() throws Exception {
        //given
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("email@email.com")
                .build();

        userService.addUser(userDto);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDto.toEntity(),"",userDto.toEntity().getAuthorities()));

        BlogPostDto blogPostDto = BlogPostDto.builder()
                .content("content")
                .title("title")
                .build();

        BlogPostDto savedBlogPostDto = blogPostService.addBlogPost(blogPostDto);

        PostSubCommentDto postSubCommentDto = PostSubCommentDto.builder()
                .content("subcomment!!!!!")
                .build();

        postSubCommentService.createPostSubComment(savedBlogPostDto.getId(),postSubCommentDto);
        //이미 등록된 댓글이 있는 상태.

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/postsubcomment")
                .param("postsubcomment_id",String.valueOf(999L));
        //쌩뚱맞은파라미터
        //when
        mvc.perform(requestBuilder)
                //then
                .andExpect(status().is4xxClientError())
                .andDo(print());

    }

    @Test
    void getAllPostSubCommentsById_Success() throws Exception {
        //given
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("email@email.com")
                .build();

        UserDto savedUserDto = userService.addUser(userDto);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDto.toEntity(),"",userDto.toEntity().getAuthorities()));

        BlogPostDto blogPostDto = BlogPostDto.builder()
                .content("content")
                .title("title")
                .build();

        BlogPostDto savedBlogPostDto = blogPostService.addBlogPost(blogPostDto);

        PostSubCommentDto postSubCommentDto1 = PostSubCommentDto.builder()
                .content("subcomment1!!!!!")
                .build();

        PostSubCommentDto postSubCommentDto2 = PostSubCommentDto.builder()
                .content("subcomment2!!!!!")
                .build();

        postSubCommentService.createPostSubComment(savedBlogPostDto.getId(),postSubCommentDto1);
        postSubCommentService.createPostSubComment(savedBlogPostDto.getId(),postSubCommentDto2);
        //이미 등록된 댓글이 있는 상태.

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/postsubcomment/user")
                .param("user_id",String.valueOf(savedUserDto.getId()));

        //when
        mvc.perform(requestBuilder)
                //then
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    void getAllPostSubCommentsById_Fail_NotFoundUser() throws Exception {
        //given
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("email@email.com")
                .build();

        UserDto savedUserDto = userService.addUser(userDto);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDto.toEntity(),"",userDto.toEntity().getAuthorities()));

        BlogPostDto blogPostDto = BlogPostDto.builder()
                .content("content")
                .title("title")
                .build();

        BlogPostDto savedBlogPostDto = blogPostService.addBlogPost(blogPostDto);

        PostSubCommentDto postSubCommentDto1 = PostSubCommentDto.builder()
                .content("subcomment1!!!!!")
                .build();

        PostSubCommentDto postSubCommentDto2 = PostSubCommentDto.builder()
                .content("subcomment2!!!!!")
                .build();

        postSubCommentService.createPostSubComment(savedBlogPostDto.getId(),postSubCommentDto1);
        postSubCommentService.createPostSubComment(savedBlogPostDto.getId(),postSubCommentDto2);
        //이미 등록된 댓글이 있는 상태.

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/postsubcomment/user")
                .param("user_id",String.valueOf(999L));
        //쌩뚱맞은 파라미터

        //when
        mvc.perform(requestBuilder)
                //then
                .andExpect(status().is4xxClientError())
                .andDo(print());

    }

    @Test
    void updatePostSubComment_Success() throws Exception {
        //given
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("email@email.com")
                .build();

        userService.addUser(userDto);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDto.toEntity(),"",userDto.toEntity().getAuthorities()));

        BlogPostDto blogPostDto = BlogPostDto.builder()
                .content("content")
                .title("title")
                .build();

        BlogPostDto savedBlogPostDto = blogPostService.addBlogPost(blogPostDto);

        PostSubCommentDto postSubCommentDto = PostSubCommentDto.builder()
                .content("subcomment!!!!!")
                .build();

        PostSubCommentDto savedSubCommentDto = postSubCommentService.createPostSubComment(savedBlogPostDto.getId(),postSubCommentDto);
        //이미 등록된 댓글이 있는 상태.

        PostSubCommentDto updateDto = PostSubCommentDto.builder()
                .id(savedSubCommentDto.getId())
                .postCommentId(savedSubCommentDto.getPostCommentId())
                .userDto(savedSubCommentDto.getUserDto())
                .content("updated!!!!!")
                .build();

        String json = new ObjectMapper().writeValueAsString(updateDto);
        String token = jwtTokenService.createToken(String.valueOf(1L));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/postsubcomment")
                .header("Auth",token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        //when
        mvc.perform(requestBuilder)
                //then
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void updatePostSubComment_Fail_NoJWT() throws Exception {
        //given
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("email@email.com")
                .build();

        userService.addUser(userDto);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDto.toEntity(),"",userDto.toEntity().getAuthorities()));

        BlogPostDto blogPostDto = BlogPostDto.builder()
                .content("content")
                .title("title")
                .build();

        BlogPostDto savedBlogPostDto = blogPostService.addBlogPost(blogPostDto);

        PostSubCommentDto postSubCommentDto = PostSubCommentDto.builder()
                .content("subcomment!!!!!")
                .build();

        PostSubCommentDto savedSubCommentDto = postSubCommentService.createPostSubComment(savedBlogPostDto.getId(),postSubCommentDto);
        //이미 등록된 댓글이 있는 상태.

        PostSubCommentDto updateDto = PostSubCommentDto.builder()
                .id(savedSubCommentDto.getId())
                .postCommentId(savedSubCommentDto.getPostCommentId())
                .userDto(savedSubCommentDto.getUserDto())
                .content("updated!!!!!")
                .build();

        String json = new ObjectMapper().writeValueAsString(updateDto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/postsubcomment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        //when
        mvc.perform(requestBuilder)
                //then
                .andExpect(status().is4xxClientError())
                .andDo(print());

    }

    @Test
    void updatePostSubComment_Fail_NoContent() throws Exception {
        //given
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("email@email.com")
                .build();

        userService.addUser(userDto);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDto.toEntity(),"",userDto.toEntity().getAuthorities()));

        BlogPostDto blogPostDto = BlogPostDto.builder()
                .content("content")
                .title("title")
                .build();

        BlogPostDto savedBlogPostDto = blogPostService.addBlogPost(blogPostDto);

        PostSubCommentDto postSubCommentDto = PostSubCommentDto.builder()
                .content("subcomment!!!!!")
                .build();

        PostSubCommentDto savedSubCommentDto = postSubCommentService.createPostSubComment(savedBlogPostDto.getId(),postSubCommentDto);

        PostSubCommentDto updateDto = PostSubCommentDto.builder()
                .id(999L)
                .postCommentId(savedSubCommentDto.getPostCommentId())
                .userDto(savedSubCommentDto.getUserDto())
                .content("updated!!!!!")
                .build();
        //쌩뚱맞은 PostSubCommentId
        String json = new ObjectMapper().writeValueAsString(updateDto);
        String token = jwtTokenService.createToken(String.valueOf(1L));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/postsubcomment")
                .header("Auth",token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        //when
        mvc.perform(requestBuilder)
                //then
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void updatePostSubComment_Fail_IllegalCaller() throws Exception {
        //given
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("email@email.com")
                .build();

        userService.addUser(userDto);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDto.toEntity(),"",userDto.toEntity().getAuthorities()));

        BlogPostDto blogPostDto = BlogPostDto.builder()
                .content("content")
                .title("title")
                .build();

        BlogPostDto savedBlogPostDto = blogPostService.addBlogPost(blogPostDto);

        PostSubCommentDto postSubCommentDto = PostSubCommentDto.builder()
                .content("subcomment!!!!!")
                .build();

        PostSubCommentDto savedSubCommentDto = postSubCommentService.createPostSubComment(savedBlogPostDto.getId(),postSubCommentDto);
        //이미 등록된 댓글이 있는 상태.

        PostSubCommentDto updateDto = PostSubCommentDto.builder()
                .id(savedSubCommentDto.getId())
                .postCommentId(savedSubCommentDto.getPostCommentId())
                .userDto(savedSubCommentDto.getUserDto())
                .content("updated!!!!!")
                .build();

        String json = new ObjectMapper().writeValueAsString(updateDto);
        String token = jwtTokenService.createToken(String.valueOf(2L));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/postsubcomment")
                .header("Auth",token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        //when
        mvc.perform(requestBuilder)
                //then
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void deletePostSubComment_Success() throws Exception {
        //given
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("email@email.com")
                .build();

        userService.addUser(userDto);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDto.toEntity(),"",userDto.toEntity().getAuthorities()));

        BlogPostDto blogPostDto = BlogPostDto.builder()
                .content("content")
                .title("title")
                .build();

        BlogPostDto savedBlogPostDto = blogPostService.addBlogPost(blogPostDto);

        PostSubCommentDto postSubCommentDto = PostSubCommentDto.builder()
                .content("subcomment!!!!!")
                .build();

        PostSubCommentDto savedSubCommentDto = postSubCommentService.createPostSubComment(savedBlogPostDto.getId(),postSubCommentDto);
        //이미 등록된 댓글이 있는 상태.

        String token = jwtTokenService.createToken(String.valueOf(1L));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/postsubcomment")
                .param("postsubcomment_id",String.valueOf(savedSubCommentDto.getId()))
                .header("Auth",token);

        //when
        mvc.perform(requestBuilder)
                //then
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    void deletePostSubComment_Fail_NoJWT() throws Exception {
        //given
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("email@email.com")
                .build();

        userService.addUser(userDto);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDto.toEntity(),"",userDto.toEntity().getAuthorities()));

        BlogPostDto blogPostDto = BlogPostDto.builder()
                .content("content")
                .title("title")
                .build();

        BlogPostDto savedBlogPostDto = blogPostService.addBlogPost(blogPostDto);

        PostSubCommentDto postSubCommentDto = PostSubCommentDto.builder()
                .content("subcomment!!!!!")
                .build();

        PostSubCommentDto savedSubCommentDto = postSubCommentService.createPostSubComment(savedBlogPostDto.getId(),postSubCommentDto);
        //이미 등록된 댓글이 있는 상태.

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/postsubcomment")
                .param("postsubcomment_id",String.valueOf(savedSubCommentDto.getId()));

        //when
        mvc.perform(requestBuilder)
                //then
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void deletePostSubComment_Fail_NoContent() throws Exception {
        //given
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("email@email.com")
                .build();

        userService.addUser(userDto);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDto.toEntity(),"",userDto.toEntity().getAuthorities()));

        BlogPostDto blogPostDto = BlogPostDto.builder()
                .content("content")
                .title("title")
                .build();

        BlogPostDto savedBlogPostDto = blogPostService.addBlogPost(blogPostDto);

        PostSubCommentDto postSubCommentDto = PostSubCommentDto.builder()
                .content("subcomment!!!!!")
                .build();

        PostSubCommentDto savedSubCommentDto = postSubCommentService.createPostSubComment(savedBlogPostDto.getId(),postSubCommentDto);
        //이미 등록된 댓글이 있는 상태.
        String token = jwtTokenService.createToken(String.valueOf(1L));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/postsubcomment")
                .param("postsubcomment_id",String.valueOf(999L))
                .header("Auth",token);

        //when
        mvc.perform(requestBuilder)
                //then
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void deletePostSubComment_Fail_IllegalCaller() throws Exception {
        //given
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("email@email.com")
                .build();

        userService.addUser(userDto);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDto.toEntity(),"",userDto.toEntity().getAuthorities()));

        BlogPostDto blogPostDto = BlogPostDto.builder()
                .content("content")
                .title("title")
                .build();

        BlogPostDto savedBlogPostDto = blogPostService.addBlogPost(blogPostDto);

        PostSubCommentDto postSubCommentDto = PostSubCommentDto.builder()
                .content("subcomment!!!!!")
                .build();

        PostSubCommentDto savedSubCommentDto = postSubCommentService.createPostSubComment(savedBlogPostDto.getId(),postSubCommentDto);
        //이미 등록된 댓글이 있는 상태.

        String token = jwtTokenService.createToken(String.valueOf(2L));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/postsubcomment")
                .param("postsubcomment_id",String.valueOf(savedSubCommentDto.getId()))
                .header("Auth",token);

        //when
        mvc.perform(requestBuilder)
                //then
                .andExpect(status().is4xxClientError())
                .andDo(print());

    }
}
