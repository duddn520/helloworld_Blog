package com.helloworldweb.helloworld_guestbook.repository;

import com.helloworldweb.helloworld_guestbook.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    @Query(value = "select distinct u from User u left join fetch u.guestBook g left join fetch g.guestBookComments gc left join fetch gc.user where u.id = :userId")
    Optional<User> findUserWithGuestBookWithGuestBookCommentsbyId(@Param(value = "userId") Long userId);

    @Query(value = "select u from User u join fetch u.guestBook g where u.id = :userId")
    Optional<User> findUserWithGuestBookById(@Param(value = "userId")Long userId);

    @Query(value = "select u from User u left join fetch u.blogPosts b where u.email = :userEmail")
    Optional<User> findUserWithBlogPostsByEmail(@Param(value = "userEmail") String userEmail);
}
