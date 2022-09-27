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

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    void 게시물등록_유저조회실패(){
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

        //유저조회시 실패 가정
        when(userRepository.findByEmail(any(String.class))).thenThrow(NoSuchElementException.class);
        //when
        //then
        /**
         * 유저 조회 실패시 NoSuchElementException Throw 하는지 Test
         */
        assertThrows(NoSuchElementException.class,()->blogPostService.addBlogPost(blogPostDto,email));

    }

    @Test
    void 게시물수정(){

        //given
        BlogPost blogPost = BlogPost.builder()
                .id(1L)
                .title("title1")
                .content("content1")
                .tags("tags")
                .searchCount(1L)
                .views(1L).build();

        User user = User. builder()
                .id(2L)
                .email("email@email.com")
                .nickName("nickname")
                .profileUrl("profileimage")
                .build();

        blogPost.updateUser(user);
        BlogPostDto blogPostDto = new BlogPostDto(blogPost);


        when(blogPostRepository.findById(any(Long.class))).thenReturn(Optional.of(blogPost));
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));
        when(blogPostRepository.save(any(BlogPost.class))).then(AdditionalAnswers.returnsFirstArg());

        //when
        BlogPostDto retBlogPostDto = blogPostService.updateBlogPost(blogPostDto,user.getEmail());

        //then
        /**
         * Id 변경이 일어나면 안됨, 내용물의 변경만 일어날 수 있도록 확인.
         */
        assertThat(retBlogPostDto.getId()).isEqualTo(blogPost.getId());
        assertThat(retBlogPostDto.getTitle()).isEqualTo(blogPostDto.getTitle());
        assertThat(retBlogPostDto.getTags()).isEqualTo(blogPostDto.getTags());
        assertThat(retBlogPostDto.getSearchCount()).isEqualTo(blogPostDto.getSearchCount());
        assertThat(retBlogPostDto.getViews()).isEqualTo(blogPostDto.getViews());
    }

    @Test
    void 게시물수정_수정할_게시물_조회실패(){

        String email = "123@email.com";
        //given
        BlogPostDto blogPostDto = BlogPostDto.builder()
                .id(1L)
                .title("title2")
                .content("content2")
                .tags("tags2")
                .searchCount(2L)
                .views(2L).build();

        //when
        /**
         * NoSuchElementException을 Throw 하는지 Test.
         */
        assertThrows(NoSuchElementException.class,()->blogPostService.updateBlogPost(blogPostDto,email));
    }

    @Test
    void 게시물수정_게시물작성자_요청자_비일치(){

        //given
        User user = User. builder()
                .id(2L)
                .email("email@email.com")
                .nickName("nickname")
                .profileUrl("profileimage")
                .build();

        User caller = User. builder()
                .id(1L)
                .email("123@email.com")
                .nickName("nickname")
                .profileUrl("profileimage")
                .build();


        String callerEmail = "123@email.com";

        BlogPostDto blogPostDto = BlogPostDto.builder()
                .id(1L)
                .title("title2")
                .userId(user.getId())
                .content("content2")
                .tags("tags2")
                .searchCount(2L)
                .views(2L).build();

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(caller));
        //when
        //then
        /**
         * 게시글 작성자와 호출자가 다른경우, IllegalCallerException 발생 Test
         */
        assertThrows(IllegalCallerException.class,()->blogPostService.updateBlogPost(blogPostDto,callerEmail));



    }

    @Test
    void 게시물삭제_삭제할_게시물_조회실패(){

        String email = "1123@email.com";
        //given
        when(blogPostRepository.findById(any(Long.class))).thenThrow(NoSuchElementException.class);
        //when
        //then
        assertThrows(NoSuchElementException.class,()->blogPostService.deleteBlogPost(1L, email));
    }

    @Test
    void 게시물삭제_게시물작성자_요청자_비일치(){
        //given
        User user = User. builder()
                .id(2L)
                .email("email@email.com")
                .nickName("nickname")
                .profileUrl("profileimage")
                .build();

        User caller = User. builder()
                .id(1L)
                .email("123@email.com")
                .nickName("nickname")
                .profileUrl("profileimage")
                .build();


        String callerEmail = "123@email.com";

        BlogPost blogPost = BlogPost.builder()
                .id(1L)
                .title("title")
                .content("content")
                .tags("tags")
                .views(2L)
                .searchCount(2L).build();

        blogPost.updateUser(user);
        BlogPostDto blogPostDto = new BlogPostDto(blogPost);
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(caller));
        when(blogPostRepository.findById(any(Long.class))).thenReturn(Optional.of(blogPost));
        //when
        //then
        /**
         * 게시글 작성자와 호출자가 다른경우, IllegalCallerException 발생 Test
         */
        assertThrows(IllegalCallerException.class,()->blogPostService.deleteBlogPost(blogPostDto.getId(),callerEmail));

    }



}
