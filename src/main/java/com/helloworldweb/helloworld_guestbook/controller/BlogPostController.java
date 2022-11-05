package com.helloworldweb.helloworld_guestbook.controller;

import com.helloworldweb.helloworld_guestbook.dto.BlogPostDto;
import com.helloworldweb.helloworld_guestbook.model.ApiResponse;
import com.helloworldweb.helloworld_guestbook.model.HttpResponseMsg;
import com.helloworldweb.helloworld_guestbook.model.HttpStatusCode;
import com.helloworldweb.helloworld_guestbook.service.BlogPostService;
import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class BlogPostController {

    private final BlogPostService blogPostService;

    //BlogPost 등록, 연관관계 주입
    @PostMapping("/api/blogpost")
    private ResponseEntity<ApiResponse> registerBlogPost(@RequestBody BlogPostDto blogPostDto){

        try {
            blogPostService.addBlogPost(blogPostDto);

            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.POST_SUCCESS,
                    HttpResponseMsg.POST_SUCCESS), HttpStatus.OK);

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

    }

    //특정 유저가 작성한 모든 BlogPost 조회 후 반환
    @GetMapping("/api/blogpost/all")
    private ResponseEntity<ApiResponse> getAllBlogPostsByUserId(@RequestParam(name = "user_id") Long userId,
                                                                @PageableDefault(size=10, sort="id", direction = Sort.Direction.DESC) Pageable pageable){

        List<BlogPostDto> blogPostDtos = blogPostService.getAllBlogPosts(userId, pageable);

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.GET_SUCCESS, blogPostDtos), HttpStatus.OK);

    }

    @GetMapping("/api/blogpost")
    private ResponseEntity<ApiResponse> getBlogPostById(@RequestParam(name = "blogpost_id") Long blogPostId){

        try {
            BlogPostDto blogPostDto = blogPostService.getBlogPost(blogPostId);

            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.OK,
                    HttpResponseMsg.GET_SUCCESS, blogPostDto), HttpStatus.OK);
        }catch (NoSuchElementException e){
            //해당 ID를 갖는 포스트가 존재하지 않는경우.
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.BAD_REQUEST,
                    HttpResponseMsg.NO_CONTENT), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/api/blogpost")
    private ResponseEntity<ApiResponse> updateBlogPost(@RequestBody BlogPostDto blogPostDto){
        try{
            BlogPostDto savedDto = blogPostService.updateBlogPost(blogPostDto);

            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.OK,
                    HttpResponseMsg.PUT_SUCCESS, savedDto), HttpStatus.OK);
        }catch (ClassCastException e){
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.UNAUTHORIZED,
                    HttpResponseMsg.NO_JWT), HttpStatus.UNAUTHORIZED);
        }
        catch (IllegalCallerException e){
            //게시물 작성자와 메소드 요청자가 다른 경우.
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.UNAUTHORIZED,
                    HttpResponseMsg.FORBIDDEN), HttpStatus.UNAUTHORIZED);
        }
        catch (NoSuchElementException e) {
            //해당 ID를 갖는 포스트가 존재하지 않는경우.
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.BAD_REQUEST,
                    HttpResponseMsg.NO_CONTENT), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/api/blogpost")
    private ResponseEntity<ApiResponse> deleteBlogPost(@RequestParam(name = "blogpost_id") Long blogPostId){
        try{
            blogPostService.deleteBlogPost(blogPostId);
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.OK,
                    HttpResponseMsg.DELETE_SUCCESS), HttpStatus.OK);
        }catch (ClassCastException e){
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.UNAUTHORIZED,
                    HttpResponseMsg.NO_JWT), HttpStatus.UNAUTHORIZED);
        }
        catch (IllegalCallerException e){
            //게시물 작성자와 메소드 요청자가 다른 경우.
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.UNAUTHORIZED,
                    HttpResponseMsg.FORBIDDEN), HttpStatus.UNAUTHORIZED);
        }
        catch (NoSuchElementException e) {
            //해당 ID를 갖는 포스트가 존재하지 않는경우.
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.BAD_REQUEST,
                    HttpResponseMsg.NO_CONTENT), HttpStatus.BAD_REQUEST);
        }
    }





}
