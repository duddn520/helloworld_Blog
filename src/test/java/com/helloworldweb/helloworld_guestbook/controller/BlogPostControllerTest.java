package com.helloworldweb.helloworld_guestbook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helloworldweb.helloworld_guestbook.domain.User;
import com.helloworldweb.helloworld_guestbook.dto.BlogPostDto;
import com.helloworldweb.helloworld_guestbook.dto.UserDto;
import com.helloworldweb.helloworld_guestbook.jwt.JwtTokenService;
import com.helloworldweb.helloworld_guestbook.service.BlogPostService;
import com.helloworldweb.helloworld_guestbook.service.GuestBookService;
import com.helloworldweb.helloworld_guestbook.service.PostSubCommentService;
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
        //given
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
        //when
        mvc.perform(requestBuilder)
        //then
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void registerBlogPost_Fail_WithoutJWT() throws Exception {
        //given
        UserDto userDto = UserDto.builder()
                .email("email@email.com")
                .build();

        userService.addUser(userDto);

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
        //when
        mvc.perform(requestBuilder)
        //then
                .andExpect(status().is4xxClientError())
                .andDo(print());

    }

    //kafka 오류시 처리방향(addUser)
    @Test
    void registerBlogPost_Fail_NotExistingUser() throws Exception {
        //given
        UserDto userDto = UserDto.builder()
                .email("email@email.com")
                .build();

        userService.addUser(userDto);

        String token = jwtTokenService.createToken("123@email.com");

        System.out.println("########################");

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
        //when
        mvc.perform(requestBuilder)
        //then
                .andExpect(status().is4xxClientError())
                .andDo(print());

    }

    @Test
    void getAllBlogPostsByUserId_Success() throws Exception {
        //given
        UserDto userDto = UserDto.builder()
                .email("email@email.com")
                .build();

        UserDto savedUser = userService.addUser(userDto);

        String token = jwtTokenService.createToken("email@email.com");

        BlogPostDto blogPostDto1 = BlogPostDto.builder()
                .content("newcontent1!!!!!!")
                .title("title123123123123")
                .build();

        String json1 = new ObjectMapper().writeValueAsString(blogPostDto1);

        RequestBuilder requestBuilder1 = MockMvcRequestBuilders
                .post("/api/blogpost")
                .header("Auth",token)
                .content(json1)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(requestBuilder1);

        BlogPostDto blogPostDto2 = BlogPostDto.builder()
                .content("newcontent1!!!!!!")
                .title("title123123123123")
                .build();

        String json2 = new ObjectMapper().writeValueAsString(blogPostDto2);

        RequestBuilder requestBuilder2 = MockMvcRequestBuilders
                .post("/api/blogpost")
                .header("Auth",token)
                .content(json2)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(requestBuilder2);

        RequestBuilder requestBuilder3 = MockMvcRequestBuilders
                .get("/api/blogpost/all")
                .param("user_id",String.valueOf(savedUser.getId()))
                .header("Auth",token);
        //when
        mvc.perform(requestBuilder3)
        //then
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    //해당 유저가 작성한 BlogPost가 아직 없는 경우, 빈 ArrayList를 반환한다.
    void getAllBogPostsByUserID_Success_NoBlogPostUser() throws Exception{
        //given
        UserDto userDto = UserDto.builder()
                .email("email@email.com")
                .build();

        UserDto savedUser = userService.addUser(userDto);

        String token = jwtTokenService.createToken("email@email.com");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/blogpost/all")
                .param("user_id",String.valueOf(savedUser.getId()))
                .header("Auth",token);
        //when
        mvc.perform(requestBuilder)
                //then
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getBlogPostById_Success() throws Exception{
        //given
        UserDto userDto = UserDto.builder()
                .email("email@email.com")
                .build();

        String token = jwtTokenService.createToken("email@email.com");

        User user = userDto.toEntity();
        userService.addUser(userDto);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDto.toEntity(),"",user.getAuthorities()));

        BlogPostDto blogPostDto = BlogPostDto.builder()
                .content("content1")
                .title("title1").build();

        BlogPostDto savedDto  = blogPostService.addBlogPost(blogPostDto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/blogpost")
                .param("blogpost_id",String.valueOf(savedDto.getId()))
                .header("Auth",token);
        //when
        mvc.perform(requestBuilder)
                //then
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    //해당 BlogPostId를 갖는 BlogPost가 존재하지 않는 경우.
    void getBlogPostsById_Fail_NoContent() throws Exception{
        //given
        UserDto userDto = UserDto.builder()
                .email("email@email.com")
                .build();

        String token = jwtTokenService.createToken("email@email.com");

        User user = userDto.toEntity();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDto.toEntity(),"",user.getAuthorities()));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/blogpost")
                .header("Auth",token)
                .param("blogpost_id",String.valueOf(999L));
        //when
        mvc.perform(requestBuilder)
        //then
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void updateBlogPost_Success() throws Exception{
        //given
        UserDto userDto = UserDto.builder()
                .email("email@email.com")
                .build();

        String token = jwtTokenService.createToken("email@email.com");

        User user = userDto.toEntity();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDto.toEntity(),"",user.getAuthorities()));

        userService.addUser(userDto);

        BlogPostDto blogPostDto = BlogPostDto.builder()
                .content("content1")
                .title("title1").build();

        BlogPostDto savedDto  = blogPostService.addBlogPost(blogPostDto);

        BlogPostDto updateDto = BlogPostDto.builder()
                .id(savedDto.getId())
                .content("updatedContent")
                .title(savedDto.getTitle())
                .searchCount(savedDto.getSearchCount())
                .views(savedDto.getViews())
                .userDto(savedDto.getUserDto())
                .tags(savedDto.getTags())
                .build();

        String json = new ObjectMapper().writeValueAsString(updateDto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/blogpost")
                .header("Auth",token)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON);

        //when
        mvc.perform(requestBuilder)
        //then
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    //게시글 작성자가 아닌 다른 유저의 수정 요청시. 401UnAuthorized
    //회원가입, 포스트 작성은 123@email.com, 수정 요청은 email@email.com
    void updateBlogPost_Fail_IllegalCaller() throws Exception{
        //given
        UserDto userDto = UserDto.builder()
                .email("123@email.com")
                .build();

        String token = jwtTokenService.createToken("email@email.com");

        User user = userDto.toEntity();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDto.toEntity(),"",user.getAuthorities()));

        userService.addUser(userDto);

        BlogPostDto blogPostDto = BlogPostDto.builder()
                .content("content1")
                .title("title1").build();

        BlogPostDto savedDto  = blogPostService.addBlogPost(blogPostDto);

        BlogPostDto updateDto = BlogPostDto.builder()
                .id(savedDto.getId())
                .content("updatedContent")
                .title(savedDto.getTitle())
                .searchCount(savedDto.getSearchCount())
                .views(savedDto.getViews())
                .userDto(savedDto.getUserDto())
                .tags(savedDto.getTags())
                .build();

        String json = new ObjectMapper().writeValueAsString(updateDto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/blogpost")
                .header("Auth",token)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON);

        //when
        mvc.perform(requestBuilder)
                //then
                .andExpect(status().is4xxClientError())
                .andDo(print());

    }

    @Test
    void updateBlogPost_Fail_NoContent() throws Exception {
        //given
        UserDto userDto = UserDto.builder()
                .email("email@email.com")
                .build();

        userService.addUser(userDto);

        String token = jwtTokenService.createToken("email@email.com");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/blogpost")
                .param("blogpost_id",String.valueOf(999L))
                .header("Auth",token);

        //when
        mvc.perform(requestBuilder)
        //then
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void deleteBlogPost_Success() throws Exception {
        //given
        UserDto userDto = UserDto.builder()
                .email("email@email.com")
                .build();

        String token = jwtTokenService.createToken("email@email.com");

        User user = userDto.toEntity();
        userService.addUser(userDto);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDto.toEntity(),"",user.getAuthorities()));

        BlogPostDto blogPostDto = BlogPostDto.builder()
                .content("content1")
                .title("title1").build();

        BlogPostDto savedDto  = blogPostService.addBlogPost(blogPostDto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/blogpost")
                .param("blogpost_id",String.valueOf(savedDto.getId()))
                .header("Auth",token);

        //when
        mvc.perform(requestBuilder)
        //then
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    //123@email.com의 게시글, email@email.com 이 지우려 할때 발생.
    void deleteBlogPost_Fail_IllegalCaller() throws Exception {
        //given
        UserDto userDto = UserDto.builder()
                .email("123@email.com")
                .build();

        User user = userDto.toEntity();
        userService.addUser(userDto);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDto.toEntity(),"",user.getAuthorities()));

        BlogPostDto blogPostDto = BlogPostDto.builder()
                .content("content1")
                .title("title1").build();

        BlogPostDto savedDto  = blogPostService.addBlogPost(blogPostDto);

        String token = jwtTokenService.createToken("email@email.com");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/blogpost")
                .param("blogpost_id",String.valueOf(savedDto.getId()))
                .header("Auth",token);

        //when
        mvc.perform(requestBuilder)
        //then
                .andExpect(status().is4xxClientError())
                .andDo(print());

    }

    @Test
    //존재하지 않는 게시물에 대한 삭제요청.
    void deleteBlogPost_Fail_NoContent() throws Exception {
        //given
        UserDto userDto = UserDto.builder()
                .email("123@email.com")
                .build();

        userService.addUser(userDto);

        String token = jwtTokenService.createToken("email@email.com");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/blogpost")
                .param("blogpost_id",String.valueOf(999L))
                .header("Auth",token);

        //when
        mvc.perform(requestBuilder)
        //then
                .andExpect(status().is4xxClientError())
                .andDo(print());


    }

}
