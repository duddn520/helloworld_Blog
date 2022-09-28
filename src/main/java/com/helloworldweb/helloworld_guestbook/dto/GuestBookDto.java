package com.helloworldweb.helloworld_guestbook.dto;


import com.helloworldweb.helloworld_guestbook.domain.GuestBook;
import com.helloworldweb.helloworld_guestbook.domain.GuestBookComment;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GuestBookDto {
    private Long id;
    private Long userId;
    private List<GuestBookCommentDto> guestBookCommentDtos = new ArrayList<>();

    public GuestBookDto(GuestBook guestBook)
    {
        this.id = guestBook.getId();
        this.userId = guestBook.getUser().getId();
        this.guestBookCommentDtos = guestBookCommentToDtos(guestBook.getGuestBookComments());
    }

    private List<GuestBookCommentDto> guestBookCommentToDtos(List<GuestBookComment> guestBookComments){
        List<GuestBookCommentDto> guestBookCommentDtos = new ArrayList<>();

        for(GuestBookComment g : guestBookComments){
            guestBookCommentDtos.add(new GuestBookCommentDto(g));
        }

        return guestBookCommentDtos;
    }

}
