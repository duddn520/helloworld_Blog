package com.helloworldweb.helloworld_guestbook.service;

import com.helloworldweb.helloworld_guestbook.domain.BlogPost;
import com.helloworldweb.helloworld_guestbook.domain.GuestBook;
import com.helloworldweb.helloworld_guestbook.domain.GuestBookComment;
import com.helloworldweb.helloworld_guestbook.domain.User;
import com.helloworldweb.helloworld_guestbook.dto.BlogPostDto;
import com.helloworldweb.helloworld_guestbook.dto.GuestBookCommentDto;
import com.helloworldweb.helloworld_guestbook.dto.GuestBookDto;
import com.helloworldweb.helloworld_guestbook.dto.UserDto;
import com.helloworldweb.helloworld_guestbook.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class jpatest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GuestBookService guestBookService;

    @Autowired
    BlogPostService blogPostService;

    @BeforeEach
    void 회원가입(){
        User user = User. builder()
                .email("email@email.com")
                .nickName("nickname")
                .profileUrl("profileimage")
                .build();

        User caller = User. builder()
                .email("123@email.com")
                .nickName("nickname")
                .profileUrl("profileimage")
                .build();

        userService.addUser(new UserDto(user));
        userService.addUser(new UserDto(caller));

        BlogPostDto blogPostdto = BlogPostDto.builder()
                .title("title")
                .content("content")
                .tags("tags")
                .views(2L)
                .searchCount(2L).build();

        blogPostService.addBlogPost(blogPostdto,"email@email.com");
        GuestBookCommentDto guestBookCommentDto = GuestBookCommentDto.builder()
                        .content("contnent123123123")
                        .build();
        guestBookService.addGuestBookComment(1L,guestBookCommentDto,"123@email.com");
    }

    @Test
    void update쿼리확인용(){
        //given
        System.out.println("###############################");
        String callerEmail = "123@email.com";
        String userEmail = "email@email.com";
        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new NoSuchElementException("해당 유저가 없습니다."));
        User callerEntity = userRepository.findByEmail(callerEmail).orElseThrow(()-> new NoSuchElementException("해당 유저가 없습니다."));


        System.out.println("###############################");
        BlogPostDto dto = blogPostService.getAllBlogPosts(userEmail).get(0);
        // view +1
        BlogPostDto blogPostDto = BlogPostDto.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .tags(dto.getTags())
                .userId(dto.getUserId())
                .views(dto.getViews()+1L)
                .searchCount(dto.getSearchCount()).build();

        blogPostService.updateBlogPost(blogPostDto, userEmail);
        //blogPostService.updateBlogPost의 Transcational commit 시점에서 updateQuery 전송됨.
        System.out.println("################################");
    }

    @Test
    void GuestBook조회시_쿼리확인(){
        System.out.println("######################################################");
        GuestBookDto guestBookDto = guestBookService.getGuestBook(1L);
        assertThat(guestBookDto.getGuestBookCommentDtos()).isInstanceOf(ArrayList.class);
        assertThat(guestBookDto.getGuestBookCommentDtos().isEmpty()).isEqualTo(false);
        assertThat(guestBookDto.getGuestBookCommentDtos().size()).isEqualTo(1);
        assertThat(guestBookDto.getGuestBookCommentDtos().get(0).getUserId()).isEqualTo(3L);
        assertThat(guestBookDto.getGuestBookCommentDtos().get(0).getContent()).isEqualTo("contnent123123123");
    }

//
//    @Test
//    void JPQL테스트(){
//    }
}
