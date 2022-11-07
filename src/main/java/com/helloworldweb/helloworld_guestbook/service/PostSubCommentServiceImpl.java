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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
    public PostSubCommentDto createPostSubComment(Long postId, PostSubCommentDto postSubCommentDto) {
        String callerEmail = getCallerEmailFromSecurityContextHolder();
        BlogPost blogPost = getBlogPostById(postId);
        PostComment postComment = PostComment.builder().build();
        PostSubComment postSubComment = postSubCommentDto.toEntity();
        User writer = getUserByEmail(callerEmail);

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
    public PostSubCommentDto addPostSubComment(PostSubCommentDto postSubCommentDto) {
        String callerEmail = getCallerEmailFromSecurityContextHolder();
        PostComment postComment = getPostCommentWithPostSubCommentsById(postSubCommentDto.getPostCommentId());
        User caller = getUserByEmail(callerEmail);

        PostSubComment postSubComment = postSubCommentDto.toEntity();
        postSubComment.updateUser(caller);
        postSubComment.updatePostComment(postComment);

        postSubCommentRepository.save(postSubComment);
        //Id를 할당받아 리턴하기 위한 save.
        return new PostSubCommentDto(postSubComment);
    }

    @Override
    @Transactional(readOnly = true)
    public PostSubCommentDto getPostSubComment(Long postSubCommentId) {
        PostSubComment postSubComment = getPostSubCommentWithUserById(postSubCommentId);
        return new PostSubCommentDto(postSubComment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostSubCommentDto> getAllSubCommentsByUserId(Long userId) {
        User user = getUserById(userId);
        return user.getPostSubComments().stream().map((psc)-> new PostSubCommentDto(psc)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PostSubCommentDto updatePostSubComment(PostSubCommentDto postSubCommentDto) {
        String callerEmail = getCallerEmailFromSecurityContextHolder();
        PostSubComment postSubComment = getPostSubCommentWithUserById(postSubCommentDto.getId());
        if (validateCaller(postSubComment.getUser().getEmail(),callerEmail)) {
            return new PostSubCommentDto(postSubComment.updatePostSubComment(postSubCommentDto));
        }else{
            throw new IllegalCallerException("댓글 작성자만 수정할 수 있습니다.");
        }
    }

    @Override
    @Transactional
    //실제 데이터를 지우지 않고, 유저 연관을 끊고 내용 바꿈, postsubcomment -> user == null
    public void deletePostSubComment(Long postSubCommentId) {
        String callerEmail = getCallerEmailFromSecurityContextHolder();
        PostSubComment postSubComment = getPostSubCommentWithUserById(postSubCommentId);
        String commentEmail = postSubComment.getUser().getEmail();
        if(validateCaller(commentEmail,callerEmail)){
            postSubComment.delete();
        }else{
            throw new IllegalCallerException("댓글 작성자만 삭제할 수 있습니다.");
        }

    }

    private BlogPost getBlogPostById(Long id){
        return blogPostRepository.findById(id).orElseThrow(()-> new NoSuchElementException("해당 포스트가 존재하지 않습니다."));
    }

    private PostComment getPostCommentWithPostSubCommentsById(Long id){
        return postCommentRepository.findPostCommentWithPostSubCommentsById(id).orElseThrow(()-> new NoSuchElementException("댓글이 존재하지 않습니다."));
    }

    private User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()-> new NoSuchElementException("해당 유저가 존재하지 않습니다."));
    }

    private User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(()-> new NoSuchElementException("해당 유저가 존재하지 않습니다."));
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

    private String getCallerEmailFromSecurityContextHolder(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getEmail();
    }
}
