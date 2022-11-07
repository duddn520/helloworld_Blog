package com.helloworldweb.helloworld_guestbook.repository;

import com.helloworldweb.helloworld_guestbook.domain.BlogPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BlogPostRepository extends JpaRepository<BlogPost,Long> {
    Optional<List<BlogPost>> findAllByUserId (Long userId);

    @Query(value = "select b from BlogPost b join fetch b.user where b.id = :blogPostId")
    Optional<BlogPost> findBlogPostWithUserById(@Param(value = "blogPostId") Long blogPostId);

    @Query(value = "select b from BlogPost b join fetch b.user u where u.id = :userId")
    Optional<List<BlogPost>> findAllBlogPostByUserId(@Param(value = "userId")Long userId, Pageable pageable);

    Page<BlogPost> findAllByUserId(Long userId, Pageable pageable);


}
