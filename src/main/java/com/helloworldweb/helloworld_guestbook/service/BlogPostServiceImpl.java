package com.helloworldweb.helloworld_guestbook.service;


import com.helloworldweb.helloworld_guestbook.domain.BlogPost;
import com.helloworldweb.helloworld_guestbook.domain.User;
import com.helloworldweb.helloworld_guestbook.dto.BlogPostDto;
import com.helloworldweb.helloworld_guestbook.repository.BlogPostRepository;
import com.helloworldweb.helloworld_guestbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogPostServiceImpl implements BlogPostService{

    private final UserRepository userRepository;
    private final BlogPostRepository blogPostRepository;

    @Override
    @Transactional
    public BlogPostDto addBlogPost(BlogPostDto blogPostDto, String email) {

        BlogPost blogPost = blogPostDto.toEntity();
        User user = getUserByEmail(email);
        blogPost.updateUser(user);
        return new BlogPostDto(blogPostRepository.save(blogPost));

    }

    // FetchJoin 시 post - fetch - postcomment , 이후 지연로딩 (batchSize조절)
    @Override
    @Transactional
    //댓글, 대댓글, 대댓글을 단 user 모두 표시해야하기 때문에 지연로딩 필요.
    public BlogPostDto getBlogPost(Long id){
        BlogPost blogPost = getBlogPostWithUserByID(id);
        return new BlogPostDto(blogPost);
    }

    @Override
    //댓글이나 대댓글, 작성자를 표시할 필요 없어 지연로딩 관련 서비스 불필요.
    // TODO: 2022/10/22 fetchJoin을 통해 구현하자.
    public List<BlogPostDto> getAllBlogPosts(String email) {

        List<BlogPost> blogPosts = getAllBlogPostsByEmail(email);

        List<BlogPostDto> blogPostDtos  = blogPosts.stream().map((b)->new BlogPostDto(b)).collect(Collectors.toList());

        return blogPostDtos;


    }

    @Override
    public BlogPostDto updateBlogPost(BlogPostDto blogPostDto, String callerEmail){

        BlogPost blogPost = getBlogPostWithUserByID(blogPostDto.getId());
        if(vaildateCaller(blogPost.getUser().getEmail(),callerEmail)){
            blogPost.updateBlogPost(blogPostDto);
            return new BlogPostDto(blogPostRepository.save(blogPost)); // 갱신된 BlogPost객체 Dto화 하기위해 DirtyCheck 대신 직접 save
        }
        else{
            throw new IllegalCallerException("게시글 작성자만 수정할 수 있습니다.");
        }

    }

    @Override
    @Transactional
    public void deleteBlogPost(Long blogPostId, String callerEmail){
        BlogPost blogPost = getBlogPostWithUserByID(blogPostId);
        if(vaildateCaller(blogPost.getUser().getEmail(),callerEmail)){
            blogPostRepository.delete(blogPost);
        }else{
            throw new IllegalCallerException("게시글 작성자만 삭제할 수 있습니다.");
        }
    }

    private BlogPost getBlogPostWithUserByID(Long blogPostId){
        return blogPostRepository.findBlogPostWithUserById(blogPostId).orElseThrow(()-> new NoSuchElementException("해당 포스트가 존재하지 않습니다."));
    }

    private User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()->new NoSuchElementException("해당 유저가 존재하지 않습니다."));

    }

    private List<BlogPost> getAllBlogPostsByEmail(String email){
        return blogPostRepository.findAllBlogPostByEmail(email).orElseGet(()->new ArrayList<>());
    }

    private boolean vaildateCaller(String writerEmail, String callerEmail){
        if (writerEmail.equals(callerEmail)){
            return true;
        }else{
            return false;
        }
    }
}
