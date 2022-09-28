package com.helloworldweb.helloworld_guestbook.repository;

import com.helloworldweb.helloworld_guestbook.domain.GuestBook;
import com.helloworldweb.helloworld_guestbook.domain.GuestBookComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestBookCommentRepository extends JpaRepository<GuestBookComment,Long> {

}
