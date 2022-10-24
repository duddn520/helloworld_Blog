package com.helloworldweb.helloworld_guestbook.service;

import com.helloworldweb.helloworld_guestbook.domain.User;
import com.helloworldweb.helloworld_guestbook.dto.UserDto;
import com.helloworldweb.helloworld_guestbook.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void 회원가입_성공(){
        //given
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("email@email.com")
                .nickName("babo")
                .profileUrl("http://123.com")
                .repoUrl("duddn520@github.com")
                .profileMusicName("OvertheHorizen.mp3")
                .profileMusicUrl("s3.com")
                .fcm("@!#!@#ASDZCXASD!@#")
                .build();

        when(userRepository.save(any(User.class))).then(AdditionalAnswers.returnsFirstArg());
        //when

        UserDto savedUserDto = userService.addUser(userDto);
        //then
        assertThat(savedUserDto.getEmail()).isEqualTo(userDto.getEmail());
        assertThat(savedUserDto.getNickName()).isEqualTo(userDto.getNickName());

    }

    @Test
    void 회원수정_성공(){
        //given
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("email@email.com")
                .nickName("babo")
                .profileUrl("http://123.com")
                .repoUrl("duddn520@github.com")
                .profileMusicName("OvertheHorizen.mp3")
                .profileMusicUrl("s3.com")
                .fcm("@!#!@#ASDZCXASD!@#")
                .build();

        UserDto updateDto = UserDto.builder()
                .id(1L)
                .email("123@email.com")
                .nickName("babo123")
                .profileUrl("http://123456.com")
                .repoUrl("duddn123@github.com")
                .profileMusicName("OvertheHorizen.mp4")
                .profileMusicUrl("s4.com")
                .fcm("123")
                .build();

        User updatedUser = updateDto.toEntity();

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(updatedUser));

        //when
        UserDto updatedDto = userService.updateUser(updateDto);

        //then
        assertThat(updatedDto.getId()).isEqualTo(updatedUser.getId());
        assertThat(updatedDto.getEmail()).isEqualTo("123@email.com");
        assertThat(updatedDto.getNickName()).isEqualTo("babo123");
    }

    @Test
    void 회원수정_존재하지않는UserId(){
        //given
        UserDto updateDto = UserDto.builder()
                .id(1L)
                .email("123@email.com")
                .nickName("babo123")
                .profileUrl("http://123456.com")
                .repoUrl("duddn123@github.com")
                .profileMusicName("OvertheHorizen.mp4")
                .profileMusicUrl("s4.com")
                .fcm("123")
                .build();
        when(userRepository.findById(any(Long.class))).thenThrow(new NoSuchElementException("해당 유저가 존재하지 않습니다."));
        //when
        //then
        assertThrows(NoSuchElementException.class,()->userService.updateUser(updateDto));
    }

}
