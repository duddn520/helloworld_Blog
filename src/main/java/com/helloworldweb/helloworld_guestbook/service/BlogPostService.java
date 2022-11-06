package com.helloworldweb.helloworld_guestbook.service;

import com.helloworldweb.helloworld_guestbook.dto.BlogPostDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BlogPostService {

    /** C
     * addBlogPost - 웹서버에서 받아온 정보를 통해 BLOGPOST객체 생성 및 DB 저장.
     * @param blogPostDto - 웹서버에서 받아오는 BLOGPOST 정보
     * @return 등록된 BlogPost 객체
     */
    BlogPostDto addBlogPost(BlogPostDto blogPostDto);

    /** R
     * getBlogPost - 웹서버에서 받아오는 BlogPost의 ID를 통하여 해당 BLOGPOST DTO를 반환하는 함수.
     * @param id - BlogPost의 ID
     * @return BLOGPOST 정보가 담긴 BLOGPOSTDTO
     */
    BlogPostDto getBlogPost(Long id);

    /** R
     * getAllBlogPosts - 유저 이메일을 통해 해당 유저가 작성한 모든 BLOGPOST 객체를 반환하는 함수.
     * @param userId - BLOGPOST를 작성한 User의 Email
     * @param pageable - Pagination을 위한 Pageable 객체
     * @return BLOGPOST 정보가 담긴 BLOGPOSTDTO의 LIST
     */
    List<BlogPostDto> getAllBlogPosts(Long userId, Pageable pageable);

    /** U
     * updateBlogPost - BLOGPOST 정보를 받고, 기존 BLOGPOST객체를 UPDATE하는 함수.
     * @param blogPostDto - 변경할 BLOGPOST에 대한 정보를 담은 DTO
     * 글 작성자와 요청자가 일치하지 않을경우 IllegalCallerException 발생.
     * @return - 변경 이후의 BLOGPOST 정보.
     */
    BlogPostDto updateBlogPost(BlogPostDto blogPostDto);

    /** D
     * deleteBlogPost - BLOGPOST의 ID를 통해 BLOGPOST객체 삭제.
     * @param blogPostId - 삭제할 BLOGPOST 객체의 ID제
     * 글 작성자와 요청자가 일치하지 않을경우 IllegalCallerException 발생.
     */
    void deleteBlogPost(Long blogPostId);

    /**
     * pagination을 위한 전체 페이지 조회 메서드
     * @param userId - 특정 유저의 id(블로그 작성자)
     * @param pageable - pageable 조건 객체
     * @return - 해당 유저가 작성한 블로그 게시글의 총 페이지갯수.
     */
    int getTotalPages(Long userId, Pageable pageable);
}
