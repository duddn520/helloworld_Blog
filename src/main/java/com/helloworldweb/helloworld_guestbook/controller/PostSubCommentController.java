package com.helloworldweb.helloworld_guestbook.controller;

import com.helloworldweb.helloworld_guestbook.dto.PostSubCommentDto;
import com.helloworldweb.helloworld_guestbook.model.ApiResponse;
import com.helloworldweb.helloworld_guestbook.model.HttpResponseMsg;
import com.helloworldweb.helloworld_guestbook.model.HttpStatusCode;
import com.helloworldweb.helloworld_guestbook.service.PostSubCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
public class PostSubCommentController {

    private final PostSubCommentService postSubCommentService;

    @PostMapping("/api/postsubcomment/new")
    private ResponseEntity<ApiResponse> createPostSubComment(@RequestParam(name = "blogpost_id")Long blogPostId, @RequestBody PostSubCommentDto postSubCommentDto)
    {
        try{
            PostSubCommentDto savedDto = postSubCommentService.createPostSubComment(blogPostId,postSubCommentDto);
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.OK,
                    HttpResponseMsg.POST_SUCCESS,
                    savedDto), HttpStatus.OK);
        }catch (ClassCastException e ){
            //jwt가 존재하지 않을 떄, Authentication 객체가 ContextHolder에 등록되지 않아 (User) 캐스팅 실패하여 예외 발생.
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.UNAUTHORIZED,
                    HttpResponseMsg.NO_JWT), HttpStatus.UNAUTHORIZED);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.UNAUTHORIZED,
                    HttpResponseMsg.NOT_FOUND_USER), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/api/postsubcomment")
    private ResponseEntity<ApiResponse> addPostSubComment(@RequestParam(name = "postcomment_id")Long postCommentId,@RequestBody PostSubCommentDto postSubCommentDto)
    {
        try{
            PostSubCommentDto savedDto = postSubCommentService.addPostSubComment(postSubCommentDto);
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.OK,
                    HttpResponseMsg.POST_SUCCESS,
                    savedDto), HttpStatus.OK);
        }catch (ClassCastException e)
        {
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.UNAUTHORIZED,
                    HttpResponseMsg.NO_JWT), HttpStatus.UNAUTHORIZED);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.UNAUTHORIZED,
                    HttpResponseMsg.NOT_FOUND_USER), HttpStatus.UNAUTHORIZED);
        }


    }

    @GetMapping("/api/postsubcomment")
    private ResponseEntity<ApiResponse> getPostSubComment(@RequestParam(name = "postsubcomment_id")Long postSubCommentId)
    {
        try{
            PostSubCommentDto postSubCommentDto = postSubCommentService.getPostSubComment(postSubCommentId);
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.OK,
                    HttpResponseMsg.GET_SUCCESS,
                    postSubCommentDto), HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.BAD_REQUEST,
                    HttpResponseMsg.NO_CONTENT), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/api/postsubcomment/user")
    private ResponseEntity<ApiResponse> getAllPostSubCommentsById(@RequestParam(name = "user_id")Long userId)
    {
        try{
            List<PostSubCommentDto> postSubCommentDtos = postSubCommentService.getAllSubCommentsByUserId(userId);
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.OK,
                    HttpResponseMsg.GET_SUCCESS,
                    postSubCommentDtos), HttpStatus.OK);
        }catch (NoSuchElementException e)
        {
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.BAD_REQUEST,
                    HttpResponseMsg.NOT_FOUND_USER), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/api/postsubcomment")
    private ResponseEntity<ApiResponse> updatePostSubComment(@RequestBody PostSubCommentDto postSubCommentDto)
    {
        try{
            PostSubCommentDto savedDto = postSubCommentService.updatePostSubComment(postSubCommentDto);
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.OK,
                    HttpResponseMsg.PUT_SUCCESS,
                    savedDto), HttpStatus.OK);
        }catch(ClassCastException e){
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.UNAUTHORIZED,
                    HttpResponseMsg.NO_JWT), HttpStatus.UNAUTHORIZED);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.BAD_REQUEST,
                    HttpResponseMsg.NO_CONTENT), HttpStatus.BAD_REQUEST);
        }catch (IllegalCallerException e){
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.UNAUTHORIZED,
                    HttpResponseMsg.FORBIDDEN), HttpStatus.UNAUTHORIZED);
        }

    }

    @DeleteMapping("/api/postsubcomment")
    private ResponseEntity<ApiResponse> deletePostSubComment(@RequestParam(name = "postsubcomment_id")Long postSubCommentId)
    {
        try{
            postSubCommentService.deletePostSubComment(postSubCommentId);
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.OK,
                    HttpResponseMsg.DELETE_SUCCESS), HttpStatus.OK);
        }catch (ClassCastException e){
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.UNAUTHORIZED,
                    HttpResponseMsg.NO_JWT), HttpStatus.UNAUTHORIZED);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.BAD_REQUEST,
                    HttpResponseMsg.NO_CONTENT), HttpStatus.BAD_REQUEST);
        }catch (IllegalCallerException e){
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.UNAUTHORIZED,
                    HttpResponseMsg.FORBIDDEN), HttpStatus.UNAUTHORIZED);
        }
    }

}
