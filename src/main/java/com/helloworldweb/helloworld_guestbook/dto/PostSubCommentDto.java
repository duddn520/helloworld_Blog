package com.helloworldweb.helloworld_guestbook.dto;

import com.helloworldweb.helloworld_guestbook.domain.PostComment;
import com.helloworldweb.helloworld_guestbook.domain.PostSubComment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostSubCommentDto {
    private Long id;
    private Long postCommentId;
    private String content;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    private UserDto userDto;

    public PostSubCommentDto(PostSubComment postSubComment){
        this.id = postSubComment.getId();
        this.postCommentId = postSubComment.getPostComment().getId();
        this.content = postSubComment.getContent();
        this.createdTime = postSubComment.getCreatedTime();
        this.modifiedTime = postSubComment.getModifiedTime();
        this.userDto = new UserDto(postSubComment.getUser());

    }

    @Builder
    public PostSubCommentDto(Long id,Long postCommentId ,UserDto userDto, String content){
        this.id = id;
        this.postCommentId = postCommentId;
        this.userDto = userDto;
        this.content = content;
    }

    public PostSubComment toEntity(){
        return PostSubComment.builder()
                .content(this.content)
                .build();
    }
}
