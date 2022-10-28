package com.helloworldweb.helloworld_guestbook.domain;

import com.helloworldweb.helloworld_guestbook.dto.PostSubCommentDto;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class PostSubComment {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_comment_id")
    private PostComment postComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public PostSubComment(Long id, String content, PostComment postComment, User user){
        this.id = id;
        this.content = content;
        this.postComment = postComment;
        this.user = user;
    }

    public void updateUser(User user){
        this.user = user;
        user.getPostSubComments().add(this);
    }
    public void updatePostComment(PostComment postComment){
        this.postComment = postComment;
        postComment.getPostSubComments().add(this);
    }

    public PostSubComment updatePostSubComment(PostSubCommentDto postSubCommentDto)
    {
        this.content = postSubCommentDto.getContent();
        return this;
    }

    public void delete(){
        this.user.getPostSubComments().remove(this);
        this.user = null;
        this.content = "삭제된 댓글입니다.";
    }

}
