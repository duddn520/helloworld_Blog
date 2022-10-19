package com.helloworldweb.helloworld_guestbook.service;

import com.helloworldweb.helloworld_guestbook.dto.PostSubCommentDto;

import java.util.List;

// TODO: 2022/10/18 구현체 작성, 테스트코드 작성. 
public class PostSubCommentServiceImpl implements PostSubCommentService{
    @Override
    public PostSubCommentDto addPostSubComment(Long postId, PostSubCommentDto postSubCommentDto, String writerEmail) {
        return null;
    }

    @Override
    public PostSubCommentDto getPostSubComment(Long postSubCommentId) {
        return null;
    }

    @Override
    public List<PostSubCommentDto> getAllMySubComments(Long userId) {
        return null;
    }

    @Override
    public PostSubCommentDto updatePostSubComment(PostSubCommentDto postSubCommentDto, String modifierEmail) {
        return null;
    }

    @Override
    public void deletePostSubComment(Long postSubCommentId) {

    }
}
