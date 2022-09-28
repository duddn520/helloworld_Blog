package com.helloworldweb.helloworld_guestbook.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class GuestBook {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "guestBook", cascade = CascadeType.ALL)
    private List<GuestBookComment> guestBookComments = new ArrayList<>();

    @Builder
    public GuestBook(Long id, User user,List<GuestBookComment> guestBookComments){
        this.id = id;
        this.user = user;
        this.guestBookComments = guestBookComments;
    }

    public void updateUser(User user){
        this.user = user;
        user.updateGuestBook(this);
    }

}
