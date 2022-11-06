package com.helloworldweb.helloworld_guestbook.dto;

import com.helloworldweb.helloworld_guestbook.domain.BlogPost;
import com.helloworldweb.helloworld_guestbook.domain.PostComment;
import com.helloworldweb.helloworld_guestbook.domain.User;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Data
public class BlogPostDto {
    private Long id;
    private String title;
    private String content;
    private String tags;
    private Long searchCount ;
    private Long views;
    private UserDto userDto;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    private List<PostCommentDto> postCommentDtos = new ArrayList<>();

    public BlogPostDto(BlogPost blogPost){
        this.id = blogPost.getId();
        this.title = blogPost.getTitle();
        this.content = blogPost.getContent();
        this.tags = blogPost.getTags();
        this.searchCount = blogPost.getSearchCount();
        this.views = blogPost.getViews();
        this.createdTime = blogPost.getCreatedTime();
        this.modifiedTime = blogPost.getModifiedTime();
        this.userDto = new UserDto(blogPost.getUser());
    }

    public BlogPostDto(BlogPost blogPost, List<PostComment> postComments){
        this.id = blogPost.getId();
        this.title = blogPost.getTitle();
        this.content = blogPost.getContent();
        this.tags = blogPost.getTags();
        this.searchCount = blogPost.getSearchCount();
        this.views = blogPost.getViews();
        this.createdTime = blogPost.getCreatedTime();
        this.modifiedTime = blogPost.getModifiedTime();
        this.userDto = new UserDto(blogPost.getUser());
        this.postCommentDtos = postComments.stream().map((p)->new PostCommentDto(p)).collect(Collectors.toList());
    }

    @Builder
    public BlogPostDto (Long id,String title, String content, String tags, Long searchCount, Long views, UserDto userDto){
        this.id = id;
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.searchCount = searchCount;
        this.views = views;
        this.userDto = userDto;
    }

    public BlogPost toEntity(){
        return BlogPost.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .tags(this.tags)
                .searchCount(this.searchCount)
                .views(this.views)
                .build();
    }

}
