package com.helloworldweb.helloworld_guestbook.repository;

import com.helloworldweb.helloworld_guestbook.domain.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.Optional;

public interface PostCommentRepository extends JpaRepository<PostComment,Long> {

    @Query("select p from PostComment p join fetch p.postSubComments where p.id = :id")
    Optional<PostComment> findPostCommentWithPostSubCommentsById(@Param(value = "id")Long postCommentId);
}
