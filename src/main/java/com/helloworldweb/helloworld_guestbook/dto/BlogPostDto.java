package com.helloworldweb.helloworld_guestbook.dto;

import com.helloworldweb.helloworld_guestbook.domain.BlogPost;
import com.helloworldweb.helloworld_guestbook.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private UserDto userDto;
    private List<PostCommentDto> postCommentDtos = new ArrayList<>();

    public BlogPostDto(BlogPost blogPost){
        this.id = blogPost.getId();
        this.userId = blogPost.getUser().getId();
        this.title = blogPost.getTitle();
        this.content = blogPost.getContent();
        this.tags = blogPost.getTags();
        this.searchCount = blogPost.getSearchCount();
        this.views = blogPost.getViews();
        this.userDto = new UserDto(blogPost.getUser());
        this.postCommentDtos = blogPost.getPostComments() == null? new ArrayList<>() : blogPost.getPostComments().stream().map((p) -> new PostCommentDto(p)).collect(Collectors.toList());
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
