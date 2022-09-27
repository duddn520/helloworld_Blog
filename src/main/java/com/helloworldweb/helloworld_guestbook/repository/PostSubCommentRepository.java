package com.helloworldweb.helloworld_guestbook.repository;

import com.helloworldweb.helloworld_guestbook.domain.PostSubComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostSubCommentRepository extends JpaRepository<PostSubComment,Long> {
}
