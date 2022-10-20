package com.helloworldweb.helloworld_guestbook.repository;

import com.helloworldweb.helloworld_guestbook.domain.PostSubComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostSubCommentRepository extends JpaRepository<PostSubComment,Long> {
    Optional<List<PostSubComment>> findAllByUserId(Long userId);
}
