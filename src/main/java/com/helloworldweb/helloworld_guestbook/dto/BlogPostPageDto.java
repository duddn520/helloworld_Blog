package com.helloworldweb.helloworld_guestbook.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class BlogPostPageDto {

    private List<BlogPostDto> blogPostDtos = new ArrayList<>();
    private int pageNum;


    public BlogPostPageDto(List<BlogPostDto> blogPostDtos, int pageNum)
    {
        this.blogPostDtos = blogPostDtos;
        this.pageNum = pageNum;
    }

}
