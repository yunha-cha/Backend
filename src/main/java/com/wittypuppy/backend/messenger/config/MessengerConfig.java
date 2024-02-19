package com.wittypuppy.backend.messenger.config;

import com.wittypuppy.backend.messenger.entity.Chatroom;
import com.wittypuppy.backend.messenger.entity.ChatroomMember;
import com.wittypuppy.backend.messenger.repository.ChatroomRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 채팅방에 새로운 멤버가 발생하고 이 멤버가 있는 상태에서 새로운 채팅이 발생할 경우<br>
 * 구독의 횟수가 많아질 수 있는데. 이를 방지하기 위해 Bean에서 채팅방의 멤버에 대한 정보를 담아두는 Configuration
 */
@Configuration
@AllArgsConstructor
@Slf4j
public class MessengerConfig {
    private final ChatroomRepository chatroomRepository;

    /**
     * 최초로 backend 서버가 실행되면, 데이터베이스에 "현재" 가지고 있는 채팅방 과 채팅방 멤버 정보를 읽어서 bean으로 저장한다.
     *
     * @return key가 채팅방 식별 코드이고 value가 그 채팅방의 멤버의 식별코드들의 Set 인 Map
     */
    @Bean
    @Scope("singleton")
    public Map<Long, Set<Long>> oldChatMemberMap() {
        Map<Long, Set<Long>> oldChatMemberMap = new HashMap<>();
        // 여기서 위의 repository를 통해 최초의 oldChatMemberMap을 구해야 한다.
        // 최초의 oldChatMemberMap을 읽어 왔다면.
        List<Chatroom> chatroomList = null;
        try {
            chatroomList = chatroomRepository.findAllByChatroomMemberList_ChatroomMemberTypeNot("삭제");

            chatroomList.forEach(chatroom -> oldChatMemberMap.put(
                    chatroom.getChatroomCode(),
                    chatroom.getChatroomMemberList().stream()
                            .map(ChatroomMember::getChatroomMemberCode)
                            .collect(Collectors.toSet())
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info(oldChatMemberMap.toString());
        return oldChatMemberMap;
    }

    /**
     * 채팅방에 멤버가 추가 될 경우 이 bean값에 추가된다.
     * 만약 그 상황에서 해당 채팅방에서 채팅이 발생할 경우 해당 사람을 강제로 구독시키고
     * 여기 있던 값을 위에 있는 값으로 이동시킨다.
     *
     * @return key가 채팅방 식별 코드이고 value가 그 채팅방의 멤버의 식별코드들의 Set 인 Map
     */
    @Bean
    @Scope("singleton")
    public Map<Long, Set<Long>> newChatMemberMap() {
        return new HashMap<>();
    }
}
