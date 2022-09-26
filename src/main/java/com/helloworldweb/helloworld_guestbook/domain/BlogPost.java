package com.helloworldweb.helloworld_guestbook.domain;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class BlogPost {
    @Id
    @GeneratedValue
    private Long id;
    // 제목
    @NotNull
    private String title;
    // 내용
    @Lob @NotNull
    private String content;
    // 태그
    private String tags;
    // 검색횟수
    private Long searchCount = 0L;
    // 조회수
    private Long views = 0L;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "blogPost", cascade = CascadeType.ALL)
    private List<PostComment> postComments = new ArrayList<>();

    @OneToMany(mappedBy = "blogPost", cascade = CascadeType.ALL)
    private List<PostImage> postImages = new ArrayList<>();

}
