package com.helloworldweb.helloworld_guestbook.kafka;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.helloworldweb.helloworld_guestbook.dto.UserDto;
import com.helloworldweb.helloworld_guestbook.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class Consumer {

    private final UserService userService;


    @KafkaListener(topics = "user_register",groupId = "user_register_guestbook")
    public void userRegisterListener(String dtoString, Acknowledgment ack){
        System.out.println("dtoString = " + dtoString);
        userService.addUser(messageToUserDto(dtoString));
        ack.acknowledge();

    }

    @KafkaListener(topics = "user_update", groupId = "user_update_guestbook")
    public void userUpdateListener(String dtoString, Acknowledgment ack){
        userService.updateUser(messageToUserDto(dtoString));
        ack.acknowledge();

    }

    @KafkaListener(topics = "user_delete", groupId = "user_delete_guestbook")
    public void userDeleteListener(String dtoString, Acknowledgment ack){
        userService.deleteUser(messageToUserDto(dtoString).getId());
        ack.acknowledge();

    }
    private UserDto messageToUserDto(String jsonString ){
        JsonParser jsonParser = new JsonParser();
        JsonElement element = jsonParser.parse(jsonString);
        JsonObject object = element.getAsJsonObject();

        Long id = object.get("id").getAsLong();
        String email = object.get("email").isJsonNull()? null : object.get("email").getAsString();
        String socialAccountId = object.get("socialAccountId").isJsonNull()? null: object.get("socialAccountId").getAsString();
        String nickName = object.get("nickName").isJsonNull()? null: object.get("nickName").getAsString();
        String profileUrl = object.get("profileUrl").isJsonNull()?null:object.get("profileUrl").getAsString();
//        String repoUrl = object.get("repoUrl").isJsonNull()? null: object.get("repoUrl").getAsString();
//        String profileMusicName = object.get("profileMusicName").isJsonNull()? null: object.get("profileMusicName").getAsString();
//        String profileMusicUrl = object.get("profileMusicUrl").isJsonNull()? null: object.get("profileMusicUrl").getAsString();
//        String fcm = object.get("fcm").isJsonNull()? null : object.get("fcm").getAsString();

        UserDto userDto = UserDto.builder()
                .id(id)
                .email(email)
                .socialAccountId(socialAccountId)
                .nickName(nickName)
                .profileUrl(profileUrl)
//                .repoUrl(repoUrl)
//                .profileMusicUrl(profileMusicUrl)
//                .profileMusicName(profileMusicName)
//                .fcm(fcm)
                .build();

        return userDto;
    }


}
