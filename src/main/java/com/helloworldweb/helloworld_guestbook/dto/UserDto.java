package com.helloworldweb.helloworld_guestbook.dto;

import com.helloworldweb.helloworld_guestbook.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserDto {
    private Long id;
    private String email;
    private String profileUrl;
    private String nickName;
    private String repoUrl;
    private String profileMusicName;
    private String profileMusicUrl;
    private String fcm;

    public UserDto(User user)
    {
        this.id = user.getId();
        this.email = user.getEmail();
        this.profileUrl = user.getProfileUrl();
        this.nickName = user.getNickName();
        this.repoUrl = user.getRepoUrl();
        this.profileMusicName = user.getProfileMusicName();
        this.profileMusicUrl = user.getProfileMusicUrl();
        this.fcm = user.getFcm();
    }

    @Builder
    public UserDto(Long id, String email, String profileUrl, String nickName, String repoUrl, String profileMusicUrl, String profileMusicName, String fcm){
        this.id = id;
        this.email = email;
        this.profileUrl = profileUrl;
        this.nickName = nickName;
        this.repoUrl = repoUrl;
        this.profileMusicUrl = profileMusicUrl;
        this.profileMusicName = profileMusicName;
        this.fcm = fcm;
    }

    public User toEntity(){
        return User.builder()
                .id(this.id)
                .email(this.email)
                .nickName(this.nickName)
                .profileUrl(this.profileUrl)
                .repoUrl(this.repoUrl)
                .profileMusicName(this.profileMusicName)
                .profileMusicUrl(this.profileMusicUrl)
                .fcm(this.fcm)
                .build();
    }

}
