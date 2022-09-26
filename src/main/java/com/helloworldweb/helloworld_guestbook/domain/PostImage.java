package com.helloworldweb.helloworld_guestbook.domain;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class PostImage {
    @Id
    @GeneratedValue
    private Long id;

    private String originalFileName;
    @NotNull
    private String storedFileName;
    @NotNull
    private String storedUrl;

    @ManyToOne
    @JoinColumn(name = "blogpost_id")
    private BlogPost blogPost;
}
