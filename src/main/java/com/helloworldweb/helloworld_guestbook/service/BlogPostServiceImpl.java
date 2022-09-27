package com.helloworldweb.helloworld_guestbook.service;


import com.helloworldweb.helloworld_guestbook.domain.BlogPost;
import com.helloworldweb.helloworld_guestbook.domain.User;
import com.helloworldweb.helloworld_guestbook.dto.BlogPostDto;
import com.helloworldweb.helloworld_guestbook.repository.BlogPostRepository;
import com.helloworldweb.helloworld_guestbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BlogPostServiceImpl implements BlogPostService{

    private final UserRepository userRepository;
    private final BlogPostRepository blogPostRepository;

    @Override
    @Transactional
    public BlogPostDto addBlogPost(BlogPostDto blogPostDto, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new NoSuchElementException("해당 유저가 존재하지 않습니다."));

        BlogPost blogPost = BlogPost.builder()
                .title(blogPostDto.getTitle())
                .content(blogPostDto.getContent())
                .searchCount(blogPostDto.getSearchCount())
                .views(blogPostDto.getViews())
                .build();

        blogPost.updateUser(user);
        return new BlogPostDto(blogPostRepository.save(blogPost));

    }

    @Override
    public BlogPostDto getBlogPost(Long id) {
        BlogPost blogPost = blogPostRepository.findById(id).orElseThrow(()-> new NoSuchElementException());
        return new BlogPostDto(blogPost);
    }

    @Override
    public List<BlogPostDto> getAllBlogPosts(String email) {
        return null;
    }

    @Override
    public BlogPostDto updateBlogPost(BlogPostDto blogPostDto) {
        return null;
    }

    @Override
    public void deleteBlogPost(Long id) {

    }
}
