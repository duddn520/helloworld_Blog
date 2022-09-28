package com.helloworldweb.helloworld_guestbook.dto;

import com.helloworldweb.helloworld_guestbook.domain.GuestBookComment;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GuestBookCommentDto {

    private Long id;
    private String content;
    private String reply;
    private Long userId;
    private Long guestBookId;

    public GuestBookCommentDto(GuestBookComment guestBookComment){
        this.id = guestBookComment.getId();
        this.content = guestBookComment.getContent();
        this.reply = guestBookComment.getReply();
        this.userId = guestBookComment.getUser().getId();
        this.guestBookId = guestBookComment.getGuestBook().getId();
    }

    public GuestBookComment toEntity(){
        return GuestBookComment.builder()
                .id(id)
                .content(content)
                .reply(reply)
                .build();
    }

    @Builder
    public GuestBookCommentDto(Long id, String content, String reply, Long userId, Long guestBookId){
        this.id = id;
        this.content = content;
        this.reply = reply;
        this.userId = userId;
        this.guestBookId = guestBookId;
    }

}
