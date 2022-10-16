package com.helloworldweb.helloworld_guestbook.service;

import com.helloworldweb.helloworld_guestbook.domain.GuestBook;
import com.helloworldweb.helloworld_guestbook.domain.GuestBookComment;
import com.helloworldweb.helloworld_guestbook.domain.User;
import com.helloworldweb.helloworld_guestbook.dto.GuestBookCommentDto;
import com.helloworldweb.helloworld_guestbook.dto.GuestBookDto;
import com.helloworldweb.helloworld_guestbook.repository.GuestBookCommentRepository;
import com.helloworldweb.helloworld_guestbook.repository.GuestBookRepository;
import com.helloworldweb.helloworld_guestbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class GuestBookServiceImpl implements GuestBookService{

    private final UserRepository userRepository;
    private final GuestBookCommentRepository guestBookCommentRepository;

    @Override
    @Transactional
    public GuestBookDto addGuestBookComment(Long userId, GuestBookCommentDto guestBookCommentDto, String email) {
        User owner = getUserById(userId);
        User writer = getUserByEmail(email);
        GuestBook guestBook = owner.getGuestBook();
        GuestBookComment guestBookComment = guestBookCommentDto.toEntity();
        //연관관계 등록, GuestBook -> GuestBookComment CascadeType.PERSIST 이므로, 영속화된 GuestBook객체의 변화 -> persist의 전이 => GuestBookComment 의 save가 필요없음.
        guestBookComment.updateGuestBook(guestBook);
        guestBookComment.updateUser(writer);

        return new GuestBookDto(guestBook);
    }

    @Override
    // TODO: 2022/10/16 User를 통해 GuestBook 가져오는 부분 fetchJoin통해 한번 쿼리로 처리. 로직 변경 필요.
    public GuestBookDto getGuestBook(Long userId) {
        GuestBookDto guestBookDto =  new GuestBookDto(getUserWithGuestBook(userId).getGuestBook());
        return guestBookDto;
    }

    @Override
    @Transactional
    public GuestBookCommentDto updateGuestBookComment(GuestBookCommentDto guestBookCommentDto, String email) {
        if (validateCaller(guestBookCommentDto,email)) {
            GuestBookComment guestBookComment = getGuestBookCommentById(guestBookCommentDto.getId());
            return new GuestBookCommentDto(guestBookComment.updateGuestBookComment(guestBookCommentDto));
        }else{
            throw new IllegalCallerException("방명록 작성자만 수정할 수 있습니다.");
        }
    }

    @Override
    @Transactional
    public void deleteGuestBookComment(Long guestBookCommentId, String email) {
        GuestBookComment guestBookComment = getGuestBookCommentById(guestBookCommentId);
        if(validateCaller(new GuestBookCommentDto(guestBookComment),email)) {
            guestBookCommentRepository.delete(guestBookComment);
        }else{
            throw new IllegalCallerException("방명록 작성자만 삭제할 수 있습니다.");

        }
    }

    private User getUserById(Long userId){
        User user = userRepository.findById(userId).orElseThrow(()-> new NoSuchElementException("해당 유저가 존재하지 않습니다."));
        return user;
    }

    private User getUserByEmail(String email){
        User user = userRepository.findByEmail(email).orElseThrow(()->new NoSuchElementException("해당 유저가 존재하지 않습니다."));
        return user;
    }

    private GuestBookComment getGuestBookCommentById(Long guestBookCommentId){
        GuestBookComment guestBookComment = guestBookCommentRepository.findById(guestBookCommentId).orElseThrow(()-> new NoSuchElementException("해당 방명록이 존재하지 않습니다."));
        return guestBookComment;
    }

    private boolean validateCaller(GuestBookCommentDto guestBookCommentDto, String email){
        if( guestBookCommentDto.getUserId().equals(getUserByEmail(email).getId())){
            return true;
        }else{
            return false;
        }
    }

    private User getUserWithGuestBook(Long userId){
        User user = userRepository.findUserWithGuestBook(userId).orElseThrow
                (()-> new NoSuchElementException("해당 유저가 존재하지 않습니다."));
        return user;
    }
}
