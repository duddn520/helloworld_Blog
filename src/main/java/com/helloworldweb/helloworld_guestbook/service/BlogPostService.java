package com.helloworldweb.helloworld_guestbook.service;

import com.helloworldweb.helloworld_guestbook.dto.BlogPostDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BlogPostService {

    /** C
     * addBlogPost - 웹서버에서 받아온 정보를 통해 BLOGPOST객체 생성 및 DB 저장.
     * @param blogPostDto - 웹서버에서 받아오는 BLOGPOST 정보
     * @param email - 작성하는 User의 Email
     * @return 등록된 BlogPost 객체
     */
    BlogPostDto addBlogPost(BlogPostDto blogPostDto, String email);

    /** R
     * getBlogPost - 웹서버에서 받아오는 BlogPost의 ID를 통하여 해당 BLOGPOST DTO를 반환하는 함수.
     * @param id - User의 ID
     * @return BLOGPOST 정보가 담긴 BLOGPOSTDTO
     */
    BlogPostDto getBlogPost(Long id);

    /** R
     * getAllBlogPosts - 유저 이메일을 통해 해당 유저가 작성한 모든 BLOGPOST 객체를 반환하는 함수.
     * @param email - BLOGPOST를 작성한 User의 Email
     * @return BLOGPOST 정보가 담긴 BLOGPOSTDTO의 LIST
     */
    List<BlogPostDto> getAllBlogPosts(String email);

    /** U
     * updateBlogPost - BLOGPOST 정보를 받고, 기존 BLOGPOST객체를 UPDATE하는 함수.
     * @param blogPostDto - 변경할 BLOGPOST에 대한 정보를 담은 DTO
     * @return - 변경 이후의 BLOGPOST 정보.
     */
    BlogPostDto updateBlogPost(BlogPostDto blogPostDto);

    /** D
     * deleteBlogPost - BLOGPOST의 ID를 통해 BLOGPOST객체 삭제.
     * @param id - 삭제할 BLOGPOST 객체의 ID
     */
    void deleteBlogPost(Long id);

}
