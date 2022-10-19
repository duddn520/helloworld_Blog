package com.helloworldweb.helloworld_guestbook.dto;

import com.helloworldweb.helloworld_guestbook.domain.PostSubComment;
import lombok.Getter;

@Getter
public class PostSubCommentDto {
    private Long id;
    private UserDto userDto;
    private String content;

    public PostSubCommentDto(PostSubComment postSubComment){
        this.id = postSubComment.getId();
        this.userDto = new UserDto(postSubComment.getUser());
        this.content = postSubComment.getContent();
    }

}
