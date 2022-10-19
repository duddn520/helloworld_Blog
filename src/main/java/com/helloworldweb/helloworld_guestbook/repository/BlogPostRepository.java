package com.helloworldweb.helloworld_guestbook.repository;

import com.helloworldweb.helloworld_guestbook.domain.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BlogPostRepository extends JpaRepository<BlogPost,Long> {
    Optional<List<BlogPost>> findAllByUserId (Long userId);

    @Query(value = "select distinct b from BlogPost b left join fetch b.postComments pc left join fetch pc.postSubComments pcs left join fetch pcs.user where b.id = :blogPostId")
    Optional<BlogPost> findBlogPostWithPostCommentsByBlogPostId(@Param(value = "blogPostId") Long blgPostId);
}
