package com.helloworldweb.helloworld_guestbook.service;

import com.helloworldweb.helloworld_guestbook.dto.PostSubCommentDto;

import java.util.List;

public interface PostSubCommentService {
    /**
     * createPostSubComment - PostSubComment 생성, 새로운 PostComment 객체를 Post객체에 연관하고, PostComment에 새로운 PostSubComment 객체 추가.
     * @param postId - 댓글을 달 Post의 id
     * @param postSubCommentDto - 댓글 내용을 가진 Dto, PostSubCommentDto
     * @return 작성 성공한 PostSubComment의 Dto
     */
    PostSubCommentDto createPostSubComment(Long postId, PostSubCommentDto postSubCommentDto);

    /**
     * addPostSubComment - 이미 존재하는 PostComment에 댓글 추가, 기존 존재하는 PostComment 객체 찾고, User연관.
     * @param postSubCommentDto - 댓글 내용을 가진 Dto
     * @return 작성 성공한 PostSubComment의 Dto
     */
    PostSubCommentDto addPostSubComment(PostSubCommentDto postSubCommentDto);

    /** R
     * getPostSubComment - PostSubcomment를 조회하는 메서드.
     * @param postSubCommentId - 작성되어있는 PostSubComment의 ID
     * @return 조회된 댓글의 PostSubCommentDto
     */
    PostSubCommentDto getPostSubComment(Long postSubCommentId);

    /** R
     * getAllMySubComments - 특정 유저가 작성한 모든 PostSubComments를 반환.
     * @param userId - 댓글 작성한 유저 ID
     * @return 작성한 댓글들의 Dto
     */
    List<PostSubCommentDto> getAllSubCommentsByUserId(Long userId);

    /** U
     * updatePostSubComment - PostSubComment의 내용물 update, 작성자 일치시에만 수정 가능.
     * @param postSubCommentDto - 수정할 댓글의 정보가 담긴 PostSubCommentDto
     * @return 수정 완료 이후의 PostSubComment의 Dto
     */
    PostSubCommentDto updatePostSubComment(PostSubCommentDto postSubCommentDto);

    /** U
     * deletePostSubComment - PostSubComment의 삭제, 실제 삭제는 이루어지지 않고, content ->"삭제된 게시물입니다." 로 update
     * @param postSubCommentId - 내용물을 삭제된 댓글로 바꾸기 위한 PostSubComment의 ID
     */
    void deletePostSubComment(Long postSubCommentId);

}
