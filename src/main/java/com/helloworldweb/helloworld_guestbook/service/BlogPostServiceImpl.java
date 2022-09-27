package com.helloworldweb.helloworld_guestbook.service;


import com.helloworldweb.helloworld_guestbook.domain.BlogPost;
import com.helloworldweb.helloworld_guestbook.domain.User;
import com.helloworldweb.helloworld_guestbook.dto.BlogPostDto;
import com.helloworldweb.helloworld_guestbook.repository.BlogPostRepository;
import com.helloworldweb.helloworld_guestbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
    public BlogPostDto getBlogPost(Long id) throws NoSuchElementException {
        try{
            BlogPost blogPost = getBlogPostById(id);
            return new BlogPostDto(blogPost);
        }catch(NoSuchElementException e)
        {
            throw e;
        }
    }

    @Override
    public List<BlogPostDto> getAllBlogPosts(String email) {
        return null;
    }

    @Override
    @Transactional
    public BlogPostDto updateBlogPost(BlogPostDto blogPostDto) throws NoSuchElementException {
        try {
            BlogPost blogPost = getBlogPostById(blogPostDto.getId());
            blogPost.updateBlogPost(blogPostDto);

            BlogPost savedBlogPost = blogPostRepository.save(blogPost);

            return BlogPostDto.builder()
                    .id(savedBlogPost.getId())
                    .title(savedBlogPost.getTitle())
                    .content(savedBlogPost.getContent())
                    .tags(savedBlogPost.getTags())
                    .searchCount(savedBlogPost.getSearchCount())
                    .views(savedBlogPost.getViews())
                    .build();
        }catch (NoSuchElementException e)
        {
            throw e;
        }
    }

    @Override
    @Transactional
    public void deleteBlogPost(Long id) throws NoSuchElementException {
        try{
            BlogPost blogPost = getBlogPostById(id);
            blogPostRepository.delete(blogPost);
        }catch (NoSuchElementException e){
            throw e;
        }

    }

    private BlogPost getBlogPostById(Long id){
        return blogPostRepository.findById(id).orElseThrow(()->new NoSuchElementException("해당 포스트가 없습니다."));
    }
}
