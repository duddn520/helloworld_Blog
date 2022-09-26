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
public class User {

    @Id @GeneratedValue
    private Long id;

    @NotNull
    private String email;
    private String profileUrl;
    private String nickName;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private GuestBook guestBook;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<GuestBookComment> guestBookComments = new ArrayList<>();

}
