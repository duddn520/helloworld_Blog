package com.helloworldweb.helloworld_guestbook.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class PostComment {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blogpost_id")
    private BlogPost blogPost;

    @OneToMany(mappedBy = "postComment", cascade = CascadeType.ALL)
    private List<PostSubComment> postSubComments = new ArrayList<>();

    @Builder
    public PostComment(Long id, BlogPost blogPost, List<PostSubComment> postSubComments){
        this.id = id;
        this.blogPost = blogPost;
        this.postSubComments = postSubComments == null ? new ArrayList<>(): postSubComments;
    }

    public void updateBlogPost(BlogPost blogPost){
        this.blogPost = blogPost;
        blogPost.getPostComments().add(this);
    }

}
