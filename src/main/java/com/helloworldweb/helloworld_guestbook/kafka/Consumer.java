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


    @KafkaListener(topics = "user_server")
    public void userServerListener(String dtoString, Acknowledgment ack){

        String op = getOpertaion(dtoString);

        switch (op){
            case "register":
                userService.addUser(messageToUserDto(dtoString));
                break;
            case "update":
                userService.updateUser(messageToUserDto(dtoString));
                break;
            case "delete":
                userService.deleteUser(getId(dtoString));
                break;
            default:
                throw new IllegalArgumentException("존재하지 않는 작업입니다.");
        }
        ack.acknowledge();

    }

//
//    @KafkaListener(topics = "user_update", groupId = "user_update_guestbook")
//    public void userUpdateListener(String dtoString, Acknowledgment ack){
//        userService.updateUser(messageToUserDto(dtoString));
//        ack.acknowledge();
//
//    }
//
//    @KafkaListener(topics = "user_delete", groupId = "user_delete_guestbook")
//    public void userDeleteListener(String dtoString, Acknowledgment ack){
//        userService.deleteUser(messageToUserDto(dtoString).getId());
//        ack.acknowledge();
//
//    }

    private UserDto messageToUserDto(String jsonString ){
        JsonParser jsonParser = new JsonParser();
        JsonElement element = jsonParser.parse(jsonString);
        JsonObject object = element.getAsJsonObject();

        Long id = object.get("id").getAsLong();
        String email = object.has("email") ? object.get("email").getAsString(): null;
        String socialAccountId = object.has("socialAccountId")? object.get("socialAccountId").getAsString():null;
        String nickName = object.has("nickName")? object.get("nickName").getAsString():null;
        String profileUrl = object.has("profileUrl")?object.get("profileUrl").getAsString():null;
        String repoUrl = object.has("repoUrl") ? object.get("repoUrl").getAsString() : null;
        String profileMusicName = object.has("profileMusicName")?object.get("profileMusicName").getAsString():null;
        String profileMusicUrl = object.has("profileMusicUrl")? object.get("profileMusicUrl").getAsString():null;
        String fcm = object.has("fcm")?object.get("fcm").getAsString():null;

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

        return userDto;
    }

    private String getOpertaion(String jsonString)
    {
        JsonParser jsonParser = new JsonParser();
        JsonElement element = jsonParser.parse(jsonString);
        JsonObject object = element.getAsJsonObject();

        return object.get("operation").getAsString();

    }

    private Long getId(String jsonString){
        JsonParser jsonParser = new JsonParser();
        JsonElement element = jsonParser.parse(jsonString);
        JsonObject object = element.getAsJsonObject();

        return object.get("Id").getAsLong();
    }


}
