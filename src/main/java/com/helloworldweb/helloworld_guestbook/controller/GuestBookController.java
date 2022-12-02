package com.helloworldweb.helloworld_guestbook.controller;

import com.helloworldweb.helloworld_guestbook.dto.GuestBookCommentDto;
import com.helloworldweb.helloworld_guestbook.dto.GuestBookDto;
import com.helloworldweb.helloworld_guestbook.model.ApiResponse;
import com.helloworldweb.helloworld_guestbook.model.HttpResponseMsg;
import com.helloworldweb.helloworld_guestbook.model.HttpStatusCode;
import com.helloworldweb.helloworld_guestbook.service.GuestBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
public class GuestBookController {

    private final GuestBookService guestBookService;

    @PostMapping("/api/guestbook")
    private ResponseEntity<ApiResponse> registerGuestBookComment(@RequestParam(name = "user_id")Long userId,@RequestBody GuestBookCommentDto guestBookCommentDto){
        GuestBookDto guestBookDto = guestBookService.addGuestBookComment(userId,guestBookCommentDto);
        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.POST_SUCCESS,
                HttpResponseMsg.POST_SUCCESS,
                guestBookDto), HttpStatus.OK);
    }

    @GetMapping("/api/guestbook")
    private ResponseEntity<ApiResponse> getGuestBook(@RequestParam(name = "user_id")Long userId){
        GuestBookDto guestBookDto = guestBookService.getGuestBook(userId);
        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.GET_SUCCESS,
                HttpResponseMsg.GET_SUCCESS,
                guestBookDto), HttpStatus.OK);
    }

    @PutMapping("/api/guestbook")
    private ResponseEntity<ApiResponse> updateGuestBookComment(@RequestBody GuestBookCommentDto guestBookCommentDto){
        GuestBookCommentDto responseDto = guestBookService.updateGuestBookComment(guestBookCommentDto);
        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.PUT_SUCCESS,
                responseDto), HttpStatus.OK);
    }

    @DeleteMapping("/api/guestbook")
    private ResponseEntity<ApiResponse> deleteGuestBookComment(@RequestParam(name = "guestbook_comment_id") Long guestBookCommentId){
        guestBookService.deleteGuestBookComment(guestBookCommentId);
        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.DELETE_SUCCESS), HttpStatus.OK);
    }

}
