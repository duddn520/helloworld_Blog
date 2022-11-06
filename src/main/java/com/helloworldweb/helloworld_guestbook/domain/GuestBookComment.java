package com.helloworldweb.helloworld_guestbook.domain;

import com.helloworldweb.helloworld_guestbook.dto.GuestBookCommentDto;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class GuestBookComment extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @NotNull
    private String content;

    private String reply;

    // 작성한 유저
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_book_id")
    private GuestBook guestBook;

    @Builder
    public GuestBookComment(Long id, String content, String reply, User user,GuestBook guestBook){
        this.id = id;
        this.content = content;
        this.reply = reply;
        this.user = user;
        this.guestBook = guestBook;
    }

    public void updateGuestBook(GuestBook guestBook){
        this.guestBook = guestBook;
        guestBook.getGuestBookComments().add(this);
    }

    public void updateUser(User user){
        this.user = user;
        user.getGuestBookComments().add(this);
    }

    // GuestBookComment에 댓글달기
    public GuestBookComment updateGuestBookComment(GuestBookCommentDto guestBookCommentDto){
        this.reply = guestBookCommentDto.getReply();
        return this;
    }
}
