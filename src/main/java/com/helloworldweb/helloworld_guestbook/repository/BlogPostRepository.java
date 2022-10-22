package com.helloworldweb.helloworld_guestbook.repository;

import com.helloworldweb.helloworld_guestbook.domain.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BlogPostRepository extends JpaRepository<BlogPost,Long> {
    Optional<List<BlogPost>> findAllByUserId (Long userId);

    @Query(value = "select b from BlogPost b join fetch b.user where b.id = :id")
    Optional<BlogPost> findBlogPostWithUserById(@Param(value = "blogPostId") Long blogPostId);

    @Query(value = "select b from BlogPost b join fetch b.user u where u.email = :email")
    Optional<List<BlogPost>> findAllBlogPostByEmail(@Param(value = "email")String email);

}
