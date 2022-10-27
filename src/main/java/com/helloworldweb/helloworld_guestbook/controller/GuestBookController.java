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
        try{
            GuestBookDto guestBookDto = guestBookService.addGuestBookComment(userId,guestBookCommentDto);

            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.OK,
                    HttpResponseMsg.POST_SUCCESS,
                    guestBookDto), HttpStatus.OK);
        }catch(ClassCastException e)
        {
            //jwt가 존재하지 않을 떄, Authentication 객체가 ContextHolder에 등록되지 않아 (User) 캐스팅 실패하여 예외 발생.
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.UNAUTHORIZED,
                    HttpResponseMsg.NO_JWT), HttpStatus.UNAUTHORIZED);
        }catch(NoSuchElementException e)
        {
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.UNAUTHORIZED,
                    HttpResponseMsg.NOT_FOUND_USER), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/api/guestbook")
    private ResponseEntity<ApiResponse> getGuestBook(@RequestParam(name = "user_id")Long userId){
        try{
            GuestBookDto guestBookDto = guestBookService.getGuestBook(userId);
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.OK,
                    HttpResponseMsg.GET_SUCCESS,
                    guestBookDto), HttpStatus.OK);

        }catch (NoSuchElementException e)
        {
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.UNAUTHORIZED,
                    HttpResponseMsg.NOT_FOUND_USER), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/api/guestbook")
    private ResponseEntity<ApiResponse> updateGuestBookComment(@RequestBody GuestBookCommentDto guestBookCommentDto){
        try{
            GuestBookCommentDto responseDto = guestBookService.updateGuestBookComment(guestBookCommentDto);
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.OK,
                    HttpResponseMsg.PUT_SUCCESS,
                    responseDto), HttpStatus.OK);

        }catch (ClassCastException e)
        {
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.UNAUTHORIZED,
                    HttpResponseMsg.NO_JWT), HttpStatus.UNAUTHORIZED);
        }catch(IllegalCallerException e)
        {
            //게시물 작성자와 메소드 요청자가 다른 경우.
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.UNAUTHORIZED,
                    HttpResponseMsg.FORBIDDEN), HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/api/guestbook")
    private ResponseEntity<ApiResponse> deleteGuestBookComment(@RequestParam(name = "guestbook_comment_id") Long guestBookCommentId){
        try{
            guestBookService.deleteGuestBookComment(guestBookCommentId);
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.OK,
                    HttpResponseMsg.DELETE_SUCCESS), HttpStatus.OK);
        }catch (ClassCastException e){
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.UNAUTHORIZED,
                    HttpResponseMsg.NO_JWT), HttpStatus.UNAUTHORIZED);
        }catch (IllegalCallerException e){
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.UNAUTHORIZED,
                    HttpResponseMsg.FORBIDDEN), HttpStatus.UNAUTHORIZED);
        }
    }

}
