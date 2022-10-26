package com.helloworldweb.helloworld_guestbook.kafka;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.helloworldweb.helloworld_guestbook.dto.UserDto;
import com.helloworldweb.helloworld_guestbook.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Consumer {

    private final UserService userService;

    @KafkaListener(topics = "user_register",groupId = "user_register_guestbook")
    public void userRegisterListener(String dtoString, Acknowledgment ack){
        System.out.println("dtoString = " + dtoString);
        userService.addUser(messageToUserDto(dtoString));
    }

    @KafkaListener(topics = "user_update", groupId = "user_update_guestbook")
    public void userUpdateListener(String dtoString, Acknowledgment ack){
        userService.updateUser(messageToUserDto(dtoString));
    }

    @KafkaListener(topics = "user_delete", groupId = "user_delete_guestbook")
    public void userDeleteListener(String dtoString, Acknowledgment ack){
        userService.deleteUser(messageToUserDto(dtoString).getId());
    }
    private UserDto messageToUserDto(String jsonString ){
        JsonParser jsonParser = new JsonParser();
        JsonElement element = jsonParser.parse(jsonString);
        JsonObject json = element.getAsJsonObject();
        UserDto userDto = UserDto.builder()
                .id(json.get("id").getAsLong())
                .email(json.get("email").getAsString())
                .profileUrl(json.get("profileUrl").getAsString())
                .nickName(json.get("nickName").getAsString())
                .repoUrl(json.get("repoUrl").getAsString())
                .profileMusicName(json.get("profileMusicName").getAsString())
                .profileMusicUrl(json.get("profileMusicUrl").getAsString())
                .fcm(json.get("fcm").getAsString())
                .build();
        return userDto;
    }


}
