package com.helloworldweb.helloworld_guestbook.service;

import com.helloworldweb.helloworld_guestbook.domain.BlogPost;
import com.helloworldweb.helloworld_guestbook.domain.User;
import com.helloworldweb.helloworld_guestbook.dto.BlogPostDto;
import com.helloworldweb.helloworld_guestbook.repository.BlogPostRepository;
import com.helloworldweb.helloworld_guestbook.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BlogPostServiceTest {

    @Mock
    BlogPostRepository blogPostRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    BlogPostServiceImpl blogPostService;

    @Test
    void 게시물작성(){

        //given
        String email = "email@email.com";
        BlogPost blogPost = BlogPost.builder()
                .content("content")
                .title("title")
                .tags("tags")
                .searchCount(1L)
                .views(1L)
                .build();

        BlogPostDto blogPostDto = BlogPostDto.builder()
                .title(blogPost.getTitle())
                .content(blogPost.getContent())
                .tags(blogPost.getTags())
                .searchCount(blogPost.getSearchCount())
                .views(blogPost.getViews()).build();

        User user = User. builder()
                .id(2L)
                .email("email@email.com")
                .nickName("nickname")
                .profileUrl("profileimage")
                .build();

        when(blogPostRepository.save(any(BlogPost.class))).then(AdditionalAnswers.returnsFirstArg());
        //Optional 객체를 반환하므로, Optional.of 사용하여 타입 지정.
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));

        //when
        BlogPostDto retBlogPostDto = blogPostService.addBlogPost(blogPostDto, email);

        //then
        /**
         * 내용물 확인
         */
        assertThat(retBlogPostDto.getContent()).isEqualTo(blogPostDto.getContent());
        assertThat(retBlogPostDto.getTitle()).isEqualTo(blogPostDto.getTitle());
        assertThat(retBlogPostDto.getViews()).isEqualTo(blogPostDto.getViews());
        assertThat(retBlogPostDto.getSearchCount()).isEqualTo(blogPostDto.getSearchCount());

        /**
         * 연관 확인
         */
        assertThat(retBlogPostDto.getUserId()).isEqualTo(user.getId());
    }


}
