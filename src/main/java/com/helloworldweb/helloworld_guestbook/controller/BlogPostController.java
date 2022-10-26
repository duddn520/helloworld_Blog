package com.helloworldweb.helloworld_guestbook.controller;

import com.helloworldweb.helloworld_guestbook.domain.User;
import com.helloworldweb.helloworld_guestbook.dto.BlogPostDto;
import com.helloworldweb.helloworld_guestbook.model.ApiResponse;
import com.helloworldweb.helloworld_guestbook.model.HttpResponseMsg;
import com.helloworldweb.helloworld_guestbook.model.HttpStatusCode;
import com.helloworldweb.helloworld_guestbook.service.BlogPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
public class BlogPostController {

    private final BlogPostService blogPostService;

    @PostMapping("/api/blogpost")
    private ResponseEntity<ApiResponse> registerBlogPost(@RequestBody BlogPostDto blogPostDto){

        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String callerEmail = user.getEmail();
            System.out.println("callerEmail = " + callerEmail);
            blogPostService.addBlogPost(blogPostDto);
        }catch (ClassCastException e) {
            //jwt가 존재하지 않을 떄, Authentication 객체가 ContextHolder에 등록되지 않아 (User) 캐스팅 실패하여 예외 발생.
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.UNAUTHORIZED,
                    HttpResponseMsg.NO_JWT), HttpStatus.UNAUTHORIZED);
        }catch (NoSuchElementException e){
            //jwt에 DB에 존재하지 않는 유저 있을 떄, NoSuchElementException
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.UNAUTHORIZED,
                    HttpResponseMsg.NOT_FOUND_USER), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.POST_SUCCESS), HttpStatus.OK);
    }


}
