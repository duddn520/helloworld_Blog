package com.helloworldweb.helloworld_guestbook.dto;

import com.helloworldweb.helloworld_guestbook.domain.PostComment;
import com.helloworldweb.helloworld_guestbook.domain.PostSubComment;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostCommentDto {
    private Long id;
    private List<PostSubCommentDto> postSubCommentDtos = new ArrayList<>();

    public PostCommentDto(PostComment postComment){
        this.id = postComment.getId();
        this.postSubCommentDtos = postComment.getPostSubComments().stream().map((p)-> new PostSubCommentDto(p)).collect(Collectors.toList());

    }
}
