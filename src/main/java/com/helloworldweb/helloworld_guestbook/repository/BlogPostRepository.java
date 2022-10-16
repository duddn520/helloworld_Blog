package com.helloworldweb.helloworld_guestbook.repository;

import com.helloworldweb.helloworld_guestbook.domain.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlogPostRepository extends JpaRepository<BlogPost,Long> {
    Optional<List<BlogPost>> findAllByUserId (Long userId);
}
