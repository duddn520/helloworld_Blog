package com.helloworldweb.helloworld_guestbook.controller;

import com.helloworldweb.helloworld_guestbook.dto.BlogPostDto;
import com.helloworldweb.helloworld_guestbook.model.ApiResponse;
import com.helloworldweb.helloworld_guestbook.model.HttpResponseMsg;
import com.helloworldweb.helloworld_guestbook.model.HttpStatusCode;
import com.helloworldweb.helloworld_guestbook.service.BlogPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
public class BlogPostController {

    private final BlogPostService blogPostService;

    @RequestMapping("/")
    private ResponseEntity<ApiResponse> registerBlogPost(BlogPostDto blogPostDto){

        try{
            blogPostService.addBlogPost(blogPostDto,"email");
        }catch(NoSuchElementException e)
        {
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.NO_CONTENT,
                    HttpResponseMsg.NOT_FOUND_USER), HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.POST_SUCCESS), HttpStatus.OK);
    }
}
