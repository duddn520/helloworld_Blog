package com.helloworldweb.helloworld_guestbook.domain;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class GuestBookComment {

    @Id @GeneratedValue
    private Long id;

    @NotNull
    private String content;
    // 작성한 유저
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "guest_book_id")
    private GuestBook guestBook;
}
