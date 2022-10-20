package com.helloworldweb.helloworld_guestbook.service;

import com.helloworldweb.helloworld_guestbook.domain.BlogPost;
import com.helloworldweb.helloworld_guestbook.domain.PostComment;
import com.helloworldweb.helloworld_guestbook.domain.PostSubComment;
import com.helloworldweb.helloworld_guestbook.domain.User;
import com.helloworldweb.helloworld_guestbook.dto.PostSubCommentDto;
import com.helloworldweb.helloworld_guestbook.repository.BlogPostRepository;
import com.helloworldweb.helloworld_guestbook.repository.PostCommentRepository;
import com.helloworldweb.helloworld_guestbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

// TODO: 2022/10/18 구현체 작성, 테스트코드 작성.
@RequiredArgsConstructor
@Service
public class PostSubCommentServiceImpl implements PostSubCommentService{

    private final BlogPostRepository blogPostRepository;
    private final UserRepository userRepository;
    private final PostCommentRepository postCommentRepository;

    @Override
    @Transactional
    public PostSubCommentDto addPostSubComment(Long postId, PostSubCommentDto postSubCommentDto, String writerEmail) {

        BlogPost blogPost = getBlogPostById(postId);
        PostComment postComment = PostComment.builder().build();
        PostSubComment postSubComment = PostSubComment.builder()
                .content(postSubCommentDto.getContent())
                .build();
        User writer = getUserByEmail(writerEmail);

        //연관관계 주입(PostComment)
        postComment.updateBlogPost(blogPost);

        //연관관계 주입 (PostSubComment)
        postSubComment.updatePostComment(postComment);
        postSubComment.updateUser(writer);

        return new PostSubCommentDto(postSubComment);
    }

    @Override
    public PostSubCommentDto getPostSubComment(Long postSubCommentId) {
        return null;
    }

    @Override
    public List<PostSubCommentDto> getAllMySubComments(Long userId) {
        return null;
    }

    @Override
    public PostSubCommentDto updatePostSubComment(PostSubCommentDto postSubCommentDto, String modifierEmail) {
        return null;
    }

    @Override
    public void deletePostSubComment(Long postSubCommentId) {

    }

    private BlogPost getBlogPostById(Long id){
        return blogPostRepository.findById(id).orElseThrow(()-> new NoSuchElementException("해당 포스트가 존재하지 않습니다."));
    }

    private User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()-> new NoSuchElementException("해당 유저가 존재하지 않습니다."));
    }
}
