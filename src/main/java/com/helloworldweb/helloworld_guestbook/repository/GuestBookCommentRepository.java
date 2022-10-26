package com.helloworldweb.helloworld_guestbook.repository;

import com.helloworldweb.helloworld_guestbook.domain.GuestBook;
import com.helloworldweb.helloworld_guestbook.domain.GuestBookComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GuestBookCommentRepository extends JpaRepository<GuestBookComment,Long> {

    @Query(value = "select gc from GuestBookComment gc join fetch gc.user where gc.id = :guestBookId")
    Optional<GuestBookComment> findGuestBookCommentWithUserById(@Param(value = "guestBookId")Long guestBookId);
}
