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

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    @Test
    void 댓글생성_성공(){
        //given
        PostSubCommentDto postSubCommentDto = PostSubCommentDto.builder()
                .content("new subcomment")
                .build();
        when(blogPostRepository.findById(any(Long.class))).thenReturn(Optional.of(testBlogPost1));
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(testUser2));
        when(postSubCommentRepository.save(any(PostSubComment.class))).then(AdditionalAnswers.returnsFirstArg());
        //when
        PostSubCommentDto returnDto = postSubCommentService.createPostSubComment(testBlogPost1.getId(),postSubCommentDto,testUser2Email);

        //then
        //값 확인
        assertThat(returnDto.getContent()).isEqualTo("new subcomment");

        //연관 확인
        assertThat(returnDto.getUserDto().getEmail()).isEqualTo(testUser2Email);
        assertThat(returnDto.getUserDto().getId()).isEqualTo(2L);

    }

    @Test
    void 댓글생성_잘못된BlogPostId(){
        //given
        PostSubCommentDto postSubCommentDto = PostSubCommentDto.builder()
                .content("new subcomment")
                .build();
        when(blogPostRepository.findById(any(Long.class))).thenThrow(new NoSuchElementException("해당 포스트가 존재하지 않습니다."));

        //when
        //then
        assertThrows(NoSuchElementException.class,()-> postSubCommentService.createPostSubComment(999L,postSubCommentDto,testUser1Email));

    }

    @Test
    void 댓글생성_잘못된WriterEmail(){
        //given
        PostSubCommentDto postSubCommentDto = PostSubCommentDto.builder()
                .content("new subcomment")
                .build();

        when(blogPostRepository.findById(any(Long.class))).thenReturn(Optional.of(testBlogPost1));
        when(userRepository.findByEmail(any(String.class))).thenThrow(new NoSuchElementException("해당 유저가 존재하지 않습니다."));
        //when
        //then
        assertThrows(NoSuchElementException.class, ()-> postSubCommentService.createPostSubComment(testBlogPost1.getId(),postSubCommentDto,"123123123123123"));


    }

    @Test
    void 댓글추가_성공(){
        //given
        PostSubCommentDto postSubCommentDto = PostSubCommentDto.builder()
                .content("new subcomment123")
                .build();

        when(postCommentRepository.findById(any(Long.class))).thenReturn(Optional.of(testPostComment1));
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(testUser2));
        when(postSubCommentRepository.save(any(PostSubComment.class))).then(AdditionalAnswers.returnsFirstArg());

        //when
        PostSubCommentDto savedDto = postSubCommentService.addPostSubComment(testPostComment1.getId(),postSubCommentDto,testUser2Email);

        //then
        //값 확인
        assertThat(savedDto.getContent()).isEqualTo("new subcomment123");

        //연관 및 값 확인
        assertThat(savedDto.getUserDto().getId()).isEqualTo(2L);
        assertThat(savedDto.getUserDto().getEmail()).isEqualTo(testUser2Email);
    }

    @Test
    void 댓글추가_잘못된PostCommentId(){
        //given
        PostSubCommentDto postSubCommentDto = PostSubCommentDto.builder()
                .content("new subcomment123")
                .build();

        when(postCommentRepository.findById(any(Long.class))).thenThrow(new NoSuchElementException("해당 댓글이 존재하지 않습니다."));
        //when
        //then
        assertThrows(NoSuchElementException.class,()-> postSubCommentService.addPostSubComment(999L,postSubCommentDto,testUser2Email));

    }

    @Test
    void 댓글추가_잘못된WriterEmail(){
        //given
        PostSubCommentDto postSubCommentDto = PostSubCommentDto.builder()
                .content("new subcomment123")
                .build();

        when(postCommentRepository.findById(any(Long.class))).thenReturn(Optional.of(testPostComment1));
        when(userRepository.findByEmail(any(String.class))).thenThrow(new NoSuchElementException("해당 유저가 존재하지 않습니다."));

        //when
        //then

        assertThrows(NoSuchElementException.class,()->postSubCommentService.addPostSubComment(5L,postSubCommentDto,"345678923123@email.com"));

    }

    @Test
    void 댓글조회_성공(){
        //given
        //연관이 주입되어있는 존재하는 postsubcomment 객체
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
        //값 확인
        assertThat(returnDto.getId()).isEqualTo(6L);
        assertThat(returnDto.getContent()).isEqualTo("subcomment1");

        //연관 및 값 확인
        assertThat(returnDto.getUserDto().getEmail()).isEqualTo(testUser1.getEmail());
        assertThat(returnDto.getUserDto().getId()).isEqualTo(1L);
    }

    @Test
    void 댓글조회_잘못된PostSubCommentId(){
        //given
        //연관이 주입되어있는 존재하는 postsubcomment 객체
        PostSubComment existingPostSubComment = PostSubComment.builder()
                .id(6L)
                .content("subcomment1")
                .build();
        existingPostSubComment.updatePostComment(testPostComment1);
        existingPostSubComment.updateUser(testUser1);

        when(postSubCommentRepository.findPostSubCommentWithUserById(any(Long.class))).thenThrow(new NoSuchElementException("해당 댓글이 존재하지 않습니다."));

        //when
        //then

        assertThrows(NoSuchElementException.class, ()-> postSubCommentService.getPostSubComment(999L));
    }

    @Test
    void 댓글전체조회_성공(){
        //given
        //연관이 주입되어있는 존재하는 postsubcomment 객체들
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

        List<PostSubComment> postSubComments = new ArrayList<>();
        postSubComments.add(existingPostSubComment1);
        postSubComments.add(existingPostSubComment2);
        postSubComments.add(existingPostSubComment3);


        when(postSubCommentRepository.findAllByUserId(any(Long.class))).thenReturn(Optional.of(postSubComments));

        //when
        List<PostSubCommentDto> returnDtos = postSubCommentService.getAllMySubComments(testUser1.getId());
        //then
        //값 확인
        assertThat(returnDtos.size()).isEqualTo(3);
        assertThat(returnDtos.get(0).getContent()).isEqualTo("subcomment1");
        assertThat(returnDtos.get(1).getContent()).isEqualTo("subcomment2");
        assertThat(returnDtos.get(2).getContent()).isEqualTo("subcomment3");

    }

    @Test
    void 댓글전체조회_잘못된UserID(){
        //given
        when(postSubCommentRepository.findAllByUserId(any(Long.class))).thenThrow(new NoSuchElementException("해당 유저가 존재하지 않습니다."));

        //when
        //then
        assertThrows(NoSuchElementException.class,()-> postSubCommentService.getAllMySubComments(999L));

    }

    @Test
    void 댓글수정_성공(){
        //given
        //연관이 주입되어있는 존재하는 postsubcomment 객체
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
        PostSubCommentDto returnDto = postSubCommentService.updatePostSubComment(postSubCommentDto,testUser1Email);

        //then
        //값 및 연관 확인
        assertThat(returnDto.getContent()).isEqualTo("updated content");
        assertThat(returnDto.getId()).isEqualTo(existingPostSubComment.getId());
        assertThat(returnDto.getUserDto().getId()).isEqualTo(testUser1.getId());

    }

    @Test
    void 댓글수정_잘못된PostSubCommentDto(){
        //given
        //연관이 주입되어있는 존재하는 postsubcomment 객체
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

        when(postSubCommentRepository.findPostSubCommentWithUserById(any(Long.class))).thenThrow(new NoSuchElementException("해당 댓글이 존재하지 않습니다."));

        //when
        //then
        assertThrows(NoSuchElementException.class, ()-> postSubCommentService.updatePostSubComment(postSubCommentDto,testUser1Email));
    }

    @Test
    void 댓글수정_잘못된ModifierEmail(){
        //given
        //연관이 주입되어있는 존재하는 postsubcomment 객체
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
        assertThrows(IllegalCallerException.class,()-> postSubCommentService.updatePostSubComment(postSubCommentDto,testUser2Email));

    }

    @Test
    void 댓글삭제_잘못된PostSubCommentId(){
        //given
        PostSubComment existingPostSubComment = PostSubComment.builder()
                .id(6L)
                .content("subcomment1")
                .build();
        existingPostSubComment.updatePostComment(testPostComment1);
        existingPostSubComment.updateUser(testUser1);

        when(postSubCommentRepository.findPostSubCommentWithUserById(any(Long.class))).thenThrow(new NoSuchElementException("해당 댓글이 존재하지 않습니다."));

        //when
        //then
        assertThrows(NoSuchElementException.class,()->postSubCommentService.deletePostSubComment(999L,testUser1Email));

    }

    @Test
    void 댓글삭제_잘못된CallerEmail(){
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
        assertThrows(IllegalCallerException.class,()-> postSubCommentService.deletePostSubComment(existingPostSubComment.getId(),testUser2Email));


    }

}
