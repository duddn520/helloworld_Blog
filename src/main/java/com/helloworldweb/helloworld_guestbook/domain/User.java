package com.helloworldweb.helloworld_guestbook.domain;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class User {

    @Id @GeneratedValue
    private Long id;

    @NotNull
    private String email;
    private String profileUrl;
    private String nickName;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<BlogPost> blogPosts = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_book_id")
    private GuestBook guestBook;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<GuestBookComment> guestBookComments = new ArrayList<>();

    @Builder
    public User (Long id, String email, String profileUrl, String nickName,List<BlogPost> blogPosts, GuestBook guestBook, List<GuestBookComment> guestBookComments){
        this.id = id;
        this.email = email;
        this.profileUrl = profileUrl;
        this.nickName = nickName;
        this.blogPosts = blogPosts == null ? new ArrayList<>() : blogPosts;
        this.guestBook = guestBook;
        this.guestBookComments = guestBookComments == null ? new ArrayList<>() : guestBookComments;
    }

    public void updateGuestBook(GuestBook guestBook){
        this.guestBook = guestBook;
        guestBook.updateUser(this);
    }
}
