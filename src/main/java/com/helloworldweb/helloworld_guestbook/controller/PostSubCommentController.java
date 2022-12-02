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

    @PostMapping("/api/postsubcomment")
    private ResponseEntity<ApiResponse> createPostSubComment(@RequestParam(name = "blogpost_id", required = false) Long blogPostId, @RequestBody PostSubCommentDto postSubCommentDto)
    {
        if (blogPostId != null){
            PostSubCommentDto savedDto = postSubCommentService.createPostSubComment(blogPostId,postSubCommentDto);
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.OK,
                    HttpResponseMsg.POST_SUCCESS,
                    savedDto), HttpStatus.OK);
        }else
        {
            PostSubCommentDto savedDto = postSubCommentService.addPostSubComment(postSubCommentDto);
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.OK,
                    HttpResponseMsg.POST_SUCCESS,
                    savedDto), HttpStatus.OK);
        }
    }

    @GetMapping("/api/postsubcomment")
    private ResponseEntity<ApiResponse> getPostSubComment(@RequestParam(name = "postsubcomment_id")Long postSubCommentId)
    {
        PostSubCommentDto postSubCommentDto = postSubCommentService.getPostSubComment(postSubCommentId);
        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.GET_SUCCESS,
                postSubCommentDto), HttpStatus.OK);
    }

    @GetMapping("/api/postsubcomment/user")
    private ResponseEntity<ApiResponse> getAllPostSubCommentsById(@RequestParam(name = "user_id")Long userId)
    {
        List<PostSubCommentDto> postSubCommentDtos = postSubCommentService.getAllSubCommentsByUserId(userId);
        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.GET_SUCCESS,
                postSubCommentDtos), HttpStatus.OK);
    }

    @PutMapping("/api/postsubcomment")
    private ResponseEntity<ApiResponse> updatePostSubComment(@RequestBody PostSubCommentDto postSubCommentDto)
    {
        PostSubCommentDto savedDto = postSubCommentService.updatePostSubComment(postSubCommentDto);
        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.PUT_SUCCESS,
                savedDto), HttpStatus.OK);
    }

    @DeleteMapping("/api/postsubcomment")
    private ResponseEntity<ApiResponse> deletePostSubComment(@RequestParam(name = "postsubcomment_id")Long postSubCommentId)
    {
        postSubCommentService.deletePostSubComment(postSubCommentId);
        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.DELETE_SUCCESS), HttpStatus.OK);
    }
}
