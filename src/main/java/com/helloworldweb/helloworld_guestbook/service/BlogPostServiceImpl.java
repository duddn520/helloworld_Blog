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

    @Override
    public BlogPostDto getBlogPost(Long id) throws NoSuchElementException {
        BlogPost blogPost = getBlogPostById(id);
        return new BlogPostDto(blogPost);
    }

    @Override
    public List<BlogPostDto> getAllBlogPosts(String email) {

        User findUser = getUserByEmail(email);

        List<BlogPost> blogPosts = blogPostRepository.findAllByUserId(findUser.getId()).orElseGet(()->new ArrayList<>());

        List<BlogPostDto> blogPostDtos  = blogPosts.stream().map((b)->new BlogPostDto(b)).collect(Collectors.toList());

        return blogPostDtos;


    }

    @Override
    @Transactional
    public BlogPostDto updateBlogPost(BlogPostDto blogPostDto, String email){

        if(!vaildateCaller(blogPostDto.getUserId(),email)){
            throw new IllegalCallerException("게시글 작성자만 수정할 수 있습니다.");
        }
        else{
            BlogPost blogPost = getBlogPostById(blogPostDto.getId());
            blogPost.updateBlogPost(blogPostDto);
            return new BlogPostDto(blogPostRepository.save(blogPost));
        }

    }

    @Override
    @Transactional
    public void deleteBlogPost(Long blogPostId, String email){
        if(!vaildateCaller(getBlogPostById(blogPostId).getUser().getId(),email)){
            throw new IllegalCallerException("게시글 작성자만 수정할 수 있습니다.");
        }else{
            BlogPost blogPost = getBlogPostById(blogPostId);
            blogPostRepository.delete(blogPost);
        }
    }

    private BlogPost getBlogPostById(Long id){
        return blogPostRepository.findById(id).orElseThrow(()->new NoSuchElementException("해당 포스트가 없습니다."));
    }

    private User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()->new NoSuchElementException("해당 유저가 존재하지 않습니다."));

    }

    private boolean vaildateCaller(Long userId, String callerEmail){
        if (userId.equals(getUserByEmail(callerEmail).getId())){
            return true;
        }else{
            return false;
        }
    }
}
