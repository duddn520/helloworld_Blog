package com.helloworldweb.helloworld_guestbook.service;

import com.helloworldweb.helloworld_guestbook.domain.BlogPost;
import com.helloworldweb.helloworld_guestbook.domain.PostComment;
import com.helloworldweb.helloworld_guestbook.domain.PostSubComment;
import com.helloworldweb.helloworld_guestbook.domain.User;
import com.helloworldweb.helloworld_guestbook.dto.PostSubCommentDto;
import com.helloworldweb.helloworld_guestbook.repository.BlogPostRepository;
import com.helloworldweb.helloworld_guestbook.repository.PostCommentRepository;
import com.helloworldweb.helloworld_guestbook.repository.PostSubCommentRepository;
import com.helloworldweb.helloworld_guestbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

// TODO: 2022/10/18 구현체 작성, 테스트코드 작성.
@RequiredArgsConstructor
@Service
public class PostSubCommentServiceImpl implements PostSubCommentService{

    private final BlogPostRepository blogPostRepository;
    private final UserRepository userRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostSubCommentRepository postSubCommentRepository;

    @Override
    @Transactional
    //첫번째 댓글 작성.(존재하지 않는 PostComment)
    public PostSubCommentDto createPostSubComment(Long postId, PostSubCommentDto postSubCommentDto, String writerEmail) {
        BlogPost blogPost = getBlogPostById(postId);
        PostComment postComment = PostComment.builder().build();
        PostSubComment postSubComment = postSubCommentDto.toEntity();
        User writer = getUserByEmail(writerEmail);

        //연관관계 주입(PostComment)
        postComment.updateBlogPost(blogPost);

        //연관관계 주입 (PostSubComment)
        postSubComment.updatePostComment(postComment);
        postSubComment.updateUser(writer);
        
        postSubCommentRepository.save(postSubComment);
        //Id를 할당받아 리턴하기 위한 save.

        return new PostSubCommentDto(postSubComment);
    }

    @Override
    @Transactional
    //존재하는 PostComment에 PostSubComment 추가.
    public PostSubCommentDto addPostSubComment(Long postCommentId, PostSubCommentDto postSubCommentDto, String writerEmail) {
        PostComment postComment = getPostCommentById(postCommentId);
        User writer = getUserByEmail(writerEmail);

        PostSubComment postSubComment = postSubCommentDto.toEntity();
        postSubComment.updateUser(writer);
        postSubComment.updatePostComment(postComment);

        postSubCommentRepository.save(postSubComment);
        //Id를 할당받아 리턴하기 위한 save.
        return new PostSubCommentDto(postSubComment);
    }

    @Override
    public PostSubCommentDto getPostSubComment(Long postSubCommentId) {
        PostSubComment postSubComment = getPostSubCommentWithUserById(postSubCommentId);
        return new PostSubCommentDto(postSubComment);
    }

    @Override
    @Transactional
    public List<PostSubCommentDto> getAllMySubComments(Long userId) {
        List<PostSubCommentDto> postSubCommentDtos = postSubCommentRepository.findAllByUserId(userId).orElseGet(()->new ArrayList<>()).stream().map((psc) -> new PostSubCommentDto(psc)).collect(Collectors.toList());
        return postSubCommentDtos;
    }

    @Override
    public PostSubCommentDto updatePostSubComment(PostSubCommentDto postSubCommentDto, String modifierEmail) {
        PostSubComment postSubComment = getPostSubCommentWithUserById(postSubCommentDto.getId());
        if (validateCaller(postSubComment.getUser().getEmail(),modifierEmail)) {
            return new PostSubCommentDto(postSubComment.updatePostSubComment(postSubCommentDto));
        }else{
            throw new IllegalCallerException("댓글 작성자만 수정할 수 있습니다.");
        }
    }

    @Override
    public void deletePostSubComment(Long postSubCommentId, String callerEmail) {
        PostSubComment postSubComment = getPostSubCommentWithUserById(postSubCommentId);
        String commentEmail = postSubComment.getUser().getEmail();
        if(validateCaller(commentEmail,callerEmail)){
            postSubCommentRepository.delete(postSubComment);
        }else{
            throw new IllegalCallerException("댓글 작성자만 삭제할 수 있습니다.");
        }

    }

    private BlogPost getBlogPostById(Long id){
        return blogPostRepository.findById(id).orElseThrow(()-> new NoSuchElementException("해당 포스트가 존재하지 않습니다."));
    }

    private PostComment getPostCommentById(Long id){
        return postCommentRepository.findById(id).orElseThrow(()-> new NoSuchElementException("해당 댓글이 존재하지 않습니다."));
    }

    private User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()-> new NoSuchElementException("해당 유저가 존재하지 않습니다."));
    }
    
    private PostSubComment getPostSubCommentById(Long postSubCommentId){
        return postSubCommentRepository.findById(postSubCommentId).orElseThrow(()-> new NoSuchElementException("해당 댓글이 존재하지 않습니다."));
        
    }

    private PostSubComment getPostSubCommentWithUserById(Long postSubCommentId){
        return postSubCommentRepository.findPostSubCommentWithUserById(postSubCommentId).orElseThrow(()-> new NoSuchElementException("해당 댓글이 존재하지 않습니다."));
    }

    private boolean validateCaller(String email, String callerEmail){
        if(email.equals(callerEmail)){
            return true;
        }else{
            return false;
        }
    }
}
