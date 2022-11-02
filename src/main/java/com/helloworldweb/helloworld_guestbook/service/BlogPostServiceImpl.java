package com.helloworldweb.helloworld_guestbook.service;


import com.helloworldweb.helloworld_guestbook.domain.BlogPost;
import com.helloworldweb.helloworld_guestbook.domain.User;
import com.helloworldweb.helloworld_guestbook.dto.BlogPostDto;
import com.helloworldweb.helloworld_guestbook.dto.UserDto;
import com.helloworldweb.helloworld_guestbook.repository.BlogPostRepository;
import com.helloworldweb.helloworld_guestbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogPostServiceImpl implements BlogPostService{

    private final UserRepository userRepository;
    private final BlogPostRepository blogPostRepository;
    private final SyncService syncService;

    @Override
    @Transactional
    public BlogPostDto addBlogPost(BlogPostDto blogPostDto) {
        Long callerId = getUserIdFromContextHolder();
        User writer = getUserWithBlogPostsByUserId(callerId);

        BlogPost blogPost = blogPostDto.toEntity();
        blogPost.updateUser(writer);
        return new BlogPostDto(blogPostRepository.save(blogPost));

    }

    // FetchJoin 시 post - fetch - postcomment , 이후 지연로딩 (batchSize조절)
    @Override
    @Transactional
    //댓글, 대댓글, 대댓글을 단 user 모두 표시해야하기 때문에 지연로딩 필요.
    public BlogPostDto getBlogPost(Long id){
        BlogPost blogPost = getBlogPostWithUserByID(id);
        return new BlogPostDto(blogPost, blogPost.getPostComments() );
    }

    @Override
    //댓글이나 대댓글, 작성자를 표시할 필요 없어 지연로딩 관련 서비스 불필요.
    public List<BlogPostDto> getAllBlogPosts(Long userId) {

        List<BlogPost> blogPosts = getAllBlogPostsById(userId);

        List<BlogPostDto> blogPostDtos = blogPosts.stream().map((b)->new BlogPostDto(b)).collect(Collectors.toList());

        return blogPostDtos;


    }

    @Override
    @Transactional
    public BlogPostDto updateBlogPost(BlogPostDto blogPostDto){

        Long callerId = getUserIdFromContextHolder();
        BlogPost blogPost = getBlogPostWithUserByID(blogPostDto.getId());
        if(vaildateCaller(blogPost.getUser().getId(),callerId)){
            blogPost.updateBlogPost(blogPostDto);
            return new BlogPostDto(blogPostRepository.save(blogPost)); // 갱신된 BlogPost객체 Dto화 하기위해 DirtyCheck 대신 직접 save
        }
        else{
            throw new IllegalCallerException("게시글 작성자만 수정할 수 있습니다.");
        }

    }

    @Override
    @Transactional
    public void deleteBlogPost(Long blogPostId){
        Long callerId = getUserIdFromContextHolder();
        BlogPost blogPost = getBlogPostWithUserByID(blogPostId);
        if(vaildateCaller(blogPost.getUser().getId(),callerId)){
            blogPostRepository.delete(blogPost);
        }else{
            throw new IllegalCallerException("게시글 작성자만 삭제할 수 있습니다.");
        }
    }

    private BlogPost getBlogPostWithUserByID(Long blogPostId){
        return blogPostRepository.findBlogPostWithUserById(blogPostId).orElseThrow(()-> new NoSuchElementException("해당 포스트가 존재하지 않습니다."));
    }

    private List<BlogPost> getAllBlogPostsById(Long userId){
        return blogPostRepository.findAllBlogPostByUserId(userId).orElseGet(()->new ArrayList<>());
    }

    private boolean vaildateCaller(Long writerId, Long callerId){
        if (writerId.equals(callerId)){
            return true;
        }else{
            return false;
        }
    }

    private Long getUserIdFromContextHolder() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }

    private User getUserWithBlogPostsByUserId(Long userId){
        return userRepository.findUserWithBlogPostsById(userId)
                .orElseGet(()-> syncService.syncUser(userId));
        //User서버에 Sync요청을 보내고, 없으면 NosuchElementException, 있으면 User객체 반환

//                .orElseThrow(()-> new NoSuchElementException("해당 유저가 존재하지 않습니다."));
        // TODO: 2022/11/01 DB동기화오류 -> orElseGet 처리 필요.
    }
}
