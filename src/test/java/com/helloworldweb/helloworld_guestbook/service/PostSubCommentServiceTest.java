package com.helloworldweb.helloworld_guestbook.service;
import com.helloworldweb.helloworld_guestbook.domain.BlogPost;
import com.helloworldweb.helloworld_guestbook.domain.PostComment;
import com.helloworldweb.helloworld_guestbook.domain.PostSubComment;
import com.helloworldweb.helloworld_guestbook.domain.User;
import com.helloworldweb.helloworld_guestbook.dto.PostSubCommentDto;
import com.helloworldweb.helloworld_guestbook.dto.UserDto;
import com.helloworldweb.helloworld_guestbook.repository.BlogPostRepository;
import com.helloworldweb.helloworld_guestbook.repository.PostCommentRepository;
import com.helloworldweb.helloworld_guestbook.repository.PostSubCommentRepository;
import com.helloworldweb.helloworld_guestbook.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
public class PostSubCommentServiceTest {

    @Mock
    BlogPostRepository blogPostRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    PostCommentRepository postCommentRepository;

    @Mock
    PostSubCommentRepository postSubCommentRepository;

    @InjectMocks
    PostSubCommentServiceImpl postSubCommentService;

    private static final String testUser1Email = "123@email.com";
    private static final String testUser2Email = "456@email.com";
    private static final User testUser1 = User.builder()
            .id(1L)
            .email(testUser1Email)
            .build();
    private static final User testUser2 = User.builder()
            .id(2L)
            .email(testUser2Email)
            .build();

    private static final BlogPost testBlogPost1 = BlogPost.builder()
            .id(3L)
            .content("blogpost1")
            .title("title1")
            .searchCount(1L)
            .views(1L)
            .build();
    private static final BlogPost testBlogPost2 = BlogPost.builder()
            .id(4L)
            .content("blogpost2")
            .title("title2")
            .searchCount(2L)
            .views(2L)
            .build();

    private static final PostComment testPostComment1 = PostComment.builder()
            .id(5L)
            .blogPost(testBlogPost1)
            .build();

    @BeforeEach
    void ContextHolder??????(){
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(testUser2,"", testUser2.getAuthorities()));
    }

    @Test
    void ????????????_??????(){
        //given
        PostSubCommentDto postSubCommentDto = PostSubCommentDto.builder()
                .content("new subcomment")
                .build();
        when(blogPostRepository.findById(any(Long.class))).thenReturn(Optional.of(testBlogPost1));
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(testUser2));
        when(postSubCommentRepository.save(any(PostSubComment.class))).then(AdditionalAnswers.returnsFirstArg());
        //when
        PostSubCommentDto returnDto = postSubCommentService.createPostSubComment(testBlogPost1.getId(),postSubCommentDto);

        //then
        //??? ??????
        assertThat(returnDto.getContent()).isEqualTo("new subcomment");

        //?????? ??????
        assertThat(returnDto.getUserDto().getEmail()).isEqualTo(testUser2Email);
        assertThat(returnDto.getUserDto().getId()).isEqualTo(2L);

    }

    @Test
    void ????????????_?????????BlogPostId(){
        //given
        PostSubCommentDto postSubCommentDto = PostSubCommentDto.builder()
                .content("new subcomment")
                .build();
        when(blogPostRepository.findById(any(Long.class))).thenThrow(new NoSuchElementException("?????? ???????????? ???????????? ????????????."));

        //when
        //then
        assertThrows(NoSuchElementException.class,()-> postSubCommentService.createPostSubComment(999L,postSubCommentDto));

    }

    @Test
    void ????????????_?????????WriterEmail(){
        //given
        PostSubCommentDto postSubCommentDto = PostSubCommentDto.builder()
                .content("new subcomment")
                .build();

        when(blogPostRepository.findById(any(Long.class))).thenReturn(Optional.of(testBlogPost1));
        when(userRepository.findById(any(Long.class))).thenThrow(new NoSuchElementException("?????? ????????? ???????????? ????????????."));
        //when
        //then
        assertThrows(NoSuchElementException.class, ()-> postSubCommentService.createPostSubComment(testBlogPost1.getId(),postSubCommentDto));


    }

    @Test
    void ????????????_??????(){
        //given
        PostSubCommentDto postSubCommentDto = PostSubCommentDto.builder()
                .postCommentId(5L)
                .content("new subcomment123")
                .build();

        when(postCommentRepository.findPostCommentWithPostSubCommentsById(any(Long.class))).thenReturn(Optional.of(testPostComment1));
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(testUser2));
        when(postSubCommentRepository.save(any(PostSubComment.class))).then(AdditionalAnswers.returnsFirstArg());

        //when
        PostSubCommentDto savedDto = postSubCommentService.addPostSubComment(postSubCommentDto);

        //then
        //??? ??????
        assertThat(savedDto.getContent()).isEqualTo("new subcomment123");

        //?????? ??? ??? ??????
        assertThat(savedDto.getUserDto().getId()).isEqualTo(2L);
        assertThat(savedDto.getUserDto().getEmail()).isEqualTo(testUser2Email);
    }

    @Test
    void ????????????_?????????PostCommentId(){
        //given
        PostSubCommentDto postSubCommentDto = PostSubCommentDto.builder()
                .postCommentId(5L)
                .content("new subcomment123")
                .build();

        when(postCommentRepository.findPostCommentWithPostSubCommentsById(any(Long.class))).thenThrow(new NoSuchElementException("?????? ????????? ???????????? ????????????."));
        //when
        //then
        assertThrows(NoSuchElementException.class,()-> postSubCommentService.addPostSubComment(postSubCommentDto));

    }

    @Test
    void ????????????_?????????WriterEmail(){
        //given
        PostSubCommentDto postSubCommentDto = PostSubCommentDto.builder()
                .postCommentId(5L)
                .content("new subcomment123")
                .build();

        when(postCommentRepository.findPostCommentWithPostSubCommentsById(any(Long.class))).thenReturn(Optional.of(testPostComment1));
        when(userRepository.findById(any(Long.class))).thenThrow(new NoSuchElementException("?????? ????????? ???????????? ????????????."));

        //when
        //then

        assertThrows(NoSuchElementException.class,()->postSubCommentService.addPostSubComment(postSubCommentDto));

    }

    @Test
    void ????????????_??????(){
        //given
        //????????? ?????????????????? ???????????? postsubcomment ??????
        PostSubComment existingPostSubComment = PostSubComment.builder()
                .id(6L)
                .content("subcomment1")
                .build();
        existingPostSubComment.updatePostComment(testPostComment1);
        existingPostSubComment.updateUser(testUser1);

        when(postSubCommentRepository.findPostSubCommentWithUserById(any(Long.class))).thenReturn(Optional.of(existingPostSubComment));

        //when
        PostSubCommentDto returnDto = postSubCommentService.getPostSubComment(6L);

        //then
        //??? ??????
        assertThat(returnDto.getId()).isEqualTo(6L);
        assertThat(returnDto.getContent()).isEqualTo("subcomment1");

        //?????? ??? ??? ??????
        assertThat(returnDto.getUserDto().getEmail()).isEqualTo(testUser1.getEmail());
        assertThat(returnDto.getUserDto().getId()).isEqualTo(1L);
    }

    @Test
    void ????????????_?????????PostSubCommentId(){
        //given
        //????????? ?????????????????? ???????????? postsubcomment ??????
        PostSubComment existingPostSubComment = PostSubComment.builder()
                .id(6L)
                .content("subcomment1")
                .build();
        existingPostSubComment.updatePostComment(testPostComment1);
        existingPostSubComment.updateUser(testUser1);

        when(postSubCommentRepository.findPostSubCommentWithUserById(any(Long.class))).thenThrow(new NoSuchElementException("?????? ????????? ???????????? ????????????."));

        //when
        //then

        assertThrows(NoSuchElementException.class, ()-> postSubCommentService.getPostSubComment(999L));
    }

    @Test
    void ??????????????????_??????(){
        //given
        //????????? ?????????????????? ???????????? postsubcomment ?????????
        testUser1.getPostSubComments().clear();
        PostSubComment existingPostSubComment1 = PostSubComment.builder()
                .id(6L)
                .content("subcomment1")
                .build();
        existingPostSubComment1.updatePostComment(testPostComment1);
        existingPostSubComment1.updateUser(testUser1);

        PostSubComment existingPostSubComment2 = PostSubComment.builder()
                .id(7L)
                .content("subcomment2")
                .build();
        existingPostSubComment2.updatePostComment(testPostComment1);
        existingPostSubComment2.updateUser(testUser1);

        PostSubComment existingPostSubComment3 = PostSubComment.builder()
                .id(8L)
                .content("subcomment3")
                .build();
        existingPostSubComment3.updatePostComment(testPostComment1);
        existingPostSubComment3.updateUser(testUser1);

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(testUser1));

        //when
        List<PostSubCommentDto> returnDtos = postSubCommentService.getAllSubCommentsByUserId(testUser1.getId());
        //then
        //??? ??????
        assertThat(returnDtos.size()).isEqualTo(3);
        assertThat(returnDtos.get(0).getContent()).isEqualTo("subcomment1");
        assertThat(returnDtos.get(1).getContent()).isEqualTo("subcomment2");
        assertThat(returnDtos.get(2).getContent()).isEqualTo("subcomment3");

    }

    @Test
    void ??????????????????_?????????UserID(){
        //given
        when(userRepository.findById(any(Long.class))).thenThrow(new NoSuchElementException("?????? ????????? ???????????? ????????????."));
        //when
        //then
        assertThrows(NoSuchElementException.class,()-> postSubCommentService.getAllSubCommentsByUserId(999L));


    }

    @Test
    void ????????????_??????(){
        //given
        //????????? ?????????????????? ???????????? postsubcomment ??????
        PostSubComment existingPostSubComment = PostSubComment.builder()
                .id(6L)
                .content("subcomment1")
                .build();
        existingPostSubComment.updatePostComment(testPostComment1);
        existingPostSubComment.updateUser(testUser2);

        PostSubCommentDto postSubCommentDto = PostSubCommentDto.builder()
                .id(existingPostSubComment.getId())
                .content("updated content")
                .userDto(new UserDto(existingPostSubComment.getUser()))
                .build();

        when(postSubCommentRepository.findPostSubCommentWithUserById(any(Long.class))).thenReturn(Optional.of(existingPostSubComment));
        //when
        PostSubCommentDto returnDto = postSubCommentService.updatePostSubComment(postSubCommentDto);

        //then
        //??? ??? ?????? ??????
        assertThat(returnDto.getContent()).isEqualTo("updated content");
        assertThat(returnDto.getId()).isEqualTo(existingPostSubComment.getId());
        assertThat(returnDto.getUserDto().getId()).isEqualTo(testUser2.getId());

    }

    @Test
    void ????????????_?????????PostSubCommentDto(){
        //given
        //????????? ?????????????????? ???????????? postsubcomment ??????
        PostSubComment existingPostSubComment = PostSubComment.builder()
                .id(6L)
                .content("subcomment1")
                .build();
        existingPostSubComment.updatePostComment(testPostComment1);
        existingPostSubComment.updateUser(testUser1);

        PostSubCommentDto postSubCommentDto = PostSubCommentDto.builder()
                .id(existingPostSubComment.getId())
                .content("updated content")
                .userDto(new UserDto(existingPostSubComment.getUser()))
                .build();

        when(postSubCommentRepository.findPostSubCommentWithUserById(any(Long.class))).thenThrow(new NoSuchElementException("?????? ????????? ???????????? ????????????."));

        //when
        //then
        assertThrows(NoSuchElementException.class, ()-> postSubCommentService.updatePostSubComment(postSubCommentDto));
    }

    @Test
    void ????????????_?????????ModifierEmail(){
        //given
        //????????? ?????????????????? ???????????? postsubcomment ??????
        PostSubComment existingPostSubComment = PostSubComment.builder()
                .id(6L)
                .content("subcomment1")
                .build();
        existingPostSubComment.updatePostComment(testPostComment1);
        existingPostSubComment.updateUser(testUser1);

        PostSubCommentDto postSubCommentDto = PostSubCommentDto.builder()
                .id(existingPostSubComment.getId())
                .content("updated content")
                .userDto(new UserDto(existingPostSubComment.getUser()))
                .build();

        when(postSubCommentRepository.findPostSubCommentWithUserById(any(Long.class))).thenReturn(Optional.of(existingPostSubComment));

        //when
        //then
        assertThrows(IllegalCallerException.class,()-> postSubCommentService.updatePostSubComment(postSubCommentDto));

    }
    
    @Test
    void ????????????_??????(){
        //given
        testUser2.getPostSubComments().clear();
        PostSubComment existingPostSubComment = PostSubComment.builder()
                .id(6L)
                .content("subcomment1")
                .build();
        existingPostSubComment.updatePostComment(testPostComment1);
        existingPostSubComment.updateUser(testUser2);

        when(postSubCommentRepository.findPostSubCommentWithUserById(any(Long.class))).thenReturn(Optional.of(existingPostSubComment));

        //when
        postSubCommentService.deletePostSubComment(6L);
        //then
        assertThat(testUser2.getPostSubComments().size()).isEqualTo(0);
        assertThat(existingPostSubComment.getUser()).isEqualTo(null);
        assertThat(existingPostSubComment.getContent()).isEqualTo("????????? ???????????????.");
    }

    @Test
    void ????????????_?????????PostSubCommentId(){
        //given
        PostSubComment existingPostSubComment = PostSubComment.builder()
                .id(6L)
                .content("subcomment1")
                .build();
        existingPostSubComment.updatePostComment(testPostComment1);
        existingPostSubComment.updateUser(testUser1);

        when(postSubCommentRepository.findPostSubCommentWithUserById(any(Long.class))).thenThrow(new NoSuchElementException("?????? ????????? ???????????? ????????????."));

        //when
        //then
        assertThrows(NoSuchElementException.class,()->postSubCommentService.deletePostSubComment(999L));

    }

    @Test
    void ????????????_?????????CallerEmail(){
        //given
        PostSubComment existingPostSubComment = PostSubComment.builder()
                .id(6L)
                .content("subcomment1")
                .build();
        existingPostSubComment.updatePostComment(testPostComment1);
        existingPostSubComment.updateUser(testUser1);

        when(postSubCommentRepository.findPostSubCommentWithUserById(any(Long.class))).thenReturn(Optional.of(existingPostSubComment));
        //when
        //then
        assertThrows(IllegalCallerException.class,()-> postSubCommentService.deletePostSubComment(existingPostSubComment.getId()));


    }

}
