package com.helloworldweb.helloworld_guestbook.repository;

import com.helloworldweb.helloworld_guestbook.domain.PostSubComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostSubCommentRepository extends JpaRepository<PostSubComment,Long> {
    Optional<List<PostSubComment>> findAllByUserId(Long userId);

    @Query(value = "select psc from PostSubComment psc left join fetch psc.user where psc.id = :postSubCommentId")
    Optional<PostSubComment> findPostSubCommentWithUserById(@Param(value = "postSubCommentId")Long postSubCommentId);
}
