package com.helloworldweb.helloworld_guestbook.controller;

import com.helloworldweb.helloworld_guestbook.dto.BlogPostDto;
import com.helloworldweb.helloworld_guestbook.dto.BlogPostPageDto;
import com.helloworldweb.helloworld_guestbook.model.ApiResponse;
import com.helloworldweb.helloworld_guestbook.model.HttpResponseMsg;
import com.helloworldweb.helloworld_guestbook.model.HttpStatusCode;
import com.helloworldweb.helloworld_guestbook.service.BlogPostService;
import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogPostController {

    private final BlogPostService blogPostService;

    //BlogPost 등록, 연관관계 주입
    @PostMapping("/api/blogpost")
    private ResponseEntity<ApiResponse> registerBlogPost(@RequestBody BlogPostDto blogPostDto){

        blogPostService.addBlogPost(blogPostDto);

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.POST_SUCCESS,
                HttpResponseMsg.POST_SUCCESS), HttpStatus.OK);

    }

    //특정 유저가 작성한 모든 BlogPost 조회 후 반환
    @GetMapping("/api/blogpost/all")
    private ResponseEntity<ApiResponse> getAllBlogPostsByUserId(@RequestParam(name = "user_id") Long userId,
                                                                @PageableDefault(size=10, sort="id", direction = Sort.Direction.DESC) Pageable pageable){

        int pageNum = blogPostService.getTotalPages(userId, pageable);
        List<BlogPostDto> blogPostDtos = blogPostService.getAllBlogPosts(userId, pageable);
        BlogPostPageDto blogPostPageDto = new BlogPostPageDto(blogPostDtos,pageNum);
        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.GET_SUCCESS, blogPostPageDto), HttpStatus.OK);

    }

    @GetMapping("/api/blogpost")
    private ResponseEntity<ApiResponse> getBlogPostById(@RequestParam(name = "blogpost_id") Long blogPostId){

        BlogPostDto blogPostDto = blogPostService.getBlogPost(blogPostId);

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.GET_SUCCESS, blogPostDto), HttpStatus.OK);
    }

    @PutMapping("/api/blogpost")
    private ResponseEntity<ApiResponse> updateBlogPost(@RequestBody BlogPostDto blogPostDto){
        BlogPostDto savedDto = blogPostService.updateBlogPost(blogPostDto);

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.PUT_SUCCESS, savedDto), HttpStatus.OK);
    }

    @DeleteMapping("/api/blogpost")
    private ResponseEntity<ApiResponse> deleteBlogPost(@RequestParam(name = "blogpost_id") Long blogPostId){
        blogPostService.deleteBlogPost(blogPostId);
        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.DELETE_SUCCESS), HttpStatus.OK);
    }
}
