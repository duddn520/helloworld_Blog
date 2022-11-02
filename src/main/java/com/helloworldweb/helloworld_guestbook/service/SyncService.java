package com.helloworldweb.helloworld_guestbook.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.helloworldweb.helloworld_guestbook.domain.GuestBook;
import com.helloworldweb.helloworld_guestbook.domain.User;
import com.helloworldweb.helloworld_guestbook.dto.UserDto;
import com.helloworldweb.helloworld_guestbook.jwt.JwtTokenService;
import com.helloworldweb.helloworld_guestbook.model.ApiResponse;
import com.helloworldweb.helloworld_guestbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class SyncService {
    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;

    public User syncUser(Long userId) {
        String token = jwtTokenService.createToken(String.valueOf(userId));
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8000")
                .defaultHeader("Auth",token)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        WebClient.ResponseSpec responseSpec = webClient.get().uri(uriBuilder -> uriBuilder.path("/api/user/register/sync").queryParam("user_id", userId).build())
                .retrieve();

        ResponseEntity<ApiResponse> responseEntity = responseSpec.toEntity(ApiResponse.class).block();
        System.out.println("responseEntity = " + responseEntity.getStatusCode().value());

        //UserServer에도 존재하지 않는 유저인 경우, NoSuchElementException.
        if (responseEntity.getStatusCode().value() == 204){
            throw new NoSuchElementException("해당 유저가 존재하지 않습니다.");
        }
        String userData = responseEntity.getBody().getData().toString();

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(userData);
        JsonObject object = element.getAsJsonObject();

        Long id = object.get("id").getAsLong();
        String email = object.get("email").isJsonNull()? null : object.get("email").getAsString();
        String socialAccountId = object.get("socialAccountId").isJsonNull()? null: object.get("socialAccountId").getAsString();
        String nickName = object.get("nickName").isJsonNull()? null: object.get("nickName").getAsString();
        String profileUrl = object.get("profileUrl").isJsonNull()?null:object.get("profileUrl").getAsString();
        String repoUrl = object.get("repoUrl").isJsonNull()? null: object.get("repoUrl").getAsString();
        String profileMusicName = object.get("profileMusicName").isJsonNull()? null: object.get("profileMusicName").getAsString();
        String profileMusicUrl = object.get("profileMusicUrl").isJsonNull()? null: object.get("profileMusicUrl").getAsString();
        String fcm = object.get("fcm").isJsonNull()? null : object.get("fcm").getAsString();

        UserDto userDto = UserDto.builder()
                .id(id)
                .email(email)
                .socialAccountId(socialAccountId)
                .nickName(nickName)
                .profileUrl(profileUrl)
                .repoUrl(repoUrl)
                .profileMusicUrl(profileMusicUrl)
                .profileMusicName(profileMusicName)
                .fcm(fcm)
                .build();

        GuestBook guestBook = GuestBook.builder().build();
        User user = userDto.toEntity();
        user.updateGuestBook(guestBook);

        return userRepository.save(user);
    }
}
