package com.helloworldweb.helloworld_guestbook.dto;


import com.helloworldweb.helloworld_guestbook.domain.GuestBook;
import com.helloworldweb.helloworld_guestbook.domain.GuestBookComment;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class GuestBookDto {
    private Long id;
    private Long userId;
    private List<GuestBookCommentDto> guestBookCommentDtos = new ArrayList<>();

    public GuestBookDto(GuestBook guestBook)
    {
        this.id = guestBook.getId();
        this.userId = guestBook.getUser().getId();
        this.guestBookCommentDtos = guestBook.getGuestBookComments() == null ? new ArrayList<>() :
                guestBook.getGuestBookComments().stream().map((g)->new GuestBookCommentDto(g)).collect(Collectors.toList());
    }

}
