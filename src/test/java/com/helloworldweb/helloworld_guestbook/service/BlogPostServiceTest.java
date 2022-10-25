package com.helloworldweb.helloworld_guestbook.service;

import com.helloworldweb.helloworld_guestbook.domain.BlogPost;
import com.helloworldweb.helloworld_guestbook.domain.User;
import com.helloworldweb.helloworld_guestbook.dto.BlogPostDto;
import com.helloworldweb.helloworld_guestbook.repository.BlogPostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
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

    @InjectMocks
    BlogPostServiceImpl blogPostService;

    @BeforeEach
    void contextHolder등록(){
        User user = User. builder()
                .id(2L)
                .email("email@email.com")
                .nickName("nickname")
                .profileUrl("profileimage")
                .build();

        Authentication auth = new UsernamePasswordAuthenticationToken(user,"",user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void 게시물작성(){

        //given
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

        //when
        BlogPostDto retBlogPostDto = blogPostService.addBlogPost(blogPostDto);

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
    void 게시물수정_성공(){

        //given
        BlogPost blogPost = BlogPost.builder()
                .id(1L)
                .title("title1")
                .content("content1")
                .tags("tags")
                .searchCount(1L)
                .views(1L).build();

        BlogPostDto blogPostDto = BlogPostDto.builder()
                .id(1L)
                .title("title2")
                .content("content2")
                .tags("tags2")
                .searchCount(3L)
                .views(3L)
                .userId(2L)
                .build();

        User user = User. builder()
                .id(2L)
                .email("email@email.com")
                .nickName("nickname")
                .profileUrl("profileimage")
                .build();

        blogPost.updateUser(user);

        when(blogPostRepository.findBlogPostWithUserById(any(Long.class))).thenReturn(Optional.of(blogPost));
        when(blogPostRepository.save(any(BlogPost.class))).then(AdditionalAnswers.returnsFirstArg());

        //when
        BlogPostDto retBlogPostDto = blogPostService.updateBlogPost(blogPostDto);

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
        assertThrows(NoSuchElementException.class,()->blogPostService.updateBlogPost(blogPostDto));
    }

    @Test
    void 게시물수정_게시물작성자_요청자_비일치(){

        //given
        User user = User. builder()
                .id(3L)
                .email("123@email.com")
                .nickName("nickname")
                .profileUrl("profileimage")
                .build();

        BlogPostDto blogPostDto = BlogPostDto.builder()
                .id(1L)
                .title("title2")
                .userId(user.getId())
                .content("content2")
                .tags("tags2")
                .searchCount(2L)
                .views(2L).build();

        BlogPost blogPost = blogPostDto.toEntity();
        blogPost.updateUser(user);

        when(blogPostRepository.findBlogPostWithUserById(any(Long.class))).thenReturn(Optional.of(blogPost));
        //when
        //then
        /**
         * 게시글 작성자와 호출자가 다른경우, IllegalCallerException 발생 Test
         */
        assertThrows(IllegalCallerException.class,()->blogPostService.updateBlogPost(blogPostDto));



    }

    @Test
    void 게시물삭제_삭제할_게시물_조회실패(){

        //given
        when(blogPostRepository.findBlogPostWithUserById(any(Long.class))).thenThrow(NoSuchElementException.class);
        //when
        //then
        assertThrows(NoSuchElementException.class,()->blogPostService.deleteBlogPost(1L));
    }

    @Test
    void 게시물삭제_게시물작성자_요청자_비일치(){
        //given
        User user = User. builder()
                .id(1L)
                .email("12345@email.com")
                .nickName("nickname")
                .profileUrl("profileimage")
                .build();

        BlogPost blogPost = BlogPost.builder()
                .id(1L)
                .title("title")
                .content("content")
                .tags("tags")
                .views(2L)
                .searchCount(2L).build();

        blogPost.updateUser(user);
        BlogPostDto blogPostDto = new BlogPostDto(blogPost);
        when(blogPostRepository.findBlogPostWithUserById(any(Long.class))).thenReturn(Optional.of(blogPost));
        //when
        //then
        /**
         * 게시글 작성자와 호출자가 다른경우, IllegalCallerException 발생 Test
         */
        assertThrows(IllegalCallerException.class,()->blogPostService.deleteBlogPost(blogPostDto.getId()));

    }

    @Test
    void 게시물전체조회_성공(){

        //given
        User user = User. builder()
                .id(1L)
                .email("email@email.com")
                .nickName("nickname")
                .profileUrl("profileimage")
                .build();

        BlogPost blogPost1 = BlogPost.builder()
                .id(2L)
                .title("title1")
                .content("content")
                .tags("tags")
                .views(2L)
                .searchCount(2L).build();
        BlogPost blogPost2 = BlogPost.builder()
                .id(3L)
                .title("title2")
                .content("content")
                .tags("tags")
                .views(2L)
                .searchCount(2L).build();
        BlogPost blogPost3 = BlogPost.builder()
                .id(4L)
                .title("title3")
                .content("content")
                .tags("tags")
                .views(2L)
                .searchCount(2L).build();

        blogPost1.updateUser(user);
        blogPost2.updateUser(user);
        blogPost3.updateUser(user);
        List<BlogPost> blogPosts = new ArrayList<>();
        blogPosts.add(blogPost1);
        blogPosts.add(blogPost2);
        blogPosts.add(blogPost3);
        when(blogPostRepository.findAllBlogPostByUserId(any(Long.class))).thenReturn(Optional.of(blogPosts));

        //when
        List<BlogPostDto> blogPostDtos = blogPostService.getAllBlogPosts(2L);

        //then
        /**
         * size, 제목, id 확인
         */
        assertThat(blogPostDtos.size()).isEqualTo(3);
        assertThat(blogPostDtos.get(0).getTitle()).isEqualTo("title1");
        assertThat(blogPostDtos.get(1).getId()).isEqualTo(3L);

    }

    @Test
    void 게시물전체조회_존재하지않는UserEmail(){
        //given
        User user = User. builder()
                .id(1L)
                .email("email@email.com")
                .nickName("nickname")
                .profileUrl("profileimage")
                .build();

        BlogPost blogPost1 = BlogPost.builder()
                .id(2L)
                .title("title1")
                .content("content")
                .tags("tags")
                .views(2L)
                .searchCount(2L).build();
        BlogPost blogPost2 = BlogPost.builder()
                .id(3L)
                .title("title2")
                .content("content")
                .tags("tags")
                .views(2L)
                .searchCount(2L).build();
        BlogPost blogPost3 = BlogPost.builder()
                .id(4L)
                .title("title3")
                .content("content")
                .tags("tags")
                .views(2L)
                .searchCount(2L).build();

        blogPost1.updateUser(user);
        blogPost2.updateUser(user);
        blogPost3.updateUser(user);

        //when
        //
        when(blogPostRepository.findAllBlogPostByUserId(any(Long.class))).thenThrow(new NoSuchElementException());

        //then
        assertThrows(NoSuchElementException.class,()->blogPostService.getAllBlogPosts(0L));

    }

    @Test
    void 게시물전체조회_게시물존재하지않으면_빈리스트(){
        //given
        User user = User. builder()
                .id(1L)
                .email("email@email.com")
                .nickName("nickname")
                .profileUrl("profileimage")
                .build();
        when(blogPostRepository.findAllBlogPostByUserId(any(Long.class))).thenReturn(Optional.of(new ArrayList<>()));
        //when
        List<BlogPostDto> blogPostDtos = blogPostService.getAllBlogPosts(2L);

        //then
        assertThat(blogPostDtos.size()).isEqualTo(0);

    }



}
