package com.helloworldweb.helloworld_guestbook.dto;

import com.helloworldweb.helloworld_guestbook.domain.BlogPost;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BlogPostDto {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String tags;
    private Long searchCount ;
    private Long views;

    public BlogPostDto(BlogPost blogPost){
        this.id = blogPost.getId();
        this.userId = blogPost.getUser().getId();
        this.title = blogPost.getTitle();
        this.content = blogPost.getContent();
        this.tags = blogPost.getTags();
        this.searchCount = blogPost.getSearchCount();
        this.views = blogPost.getViews();
    }

    @Builder
    public BlogPostDto (Long id,Long userId, String title, String content, String tags, Long searchCount, Long views){
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.searchCount = searchCount;
        this.views = views;
    }

}
