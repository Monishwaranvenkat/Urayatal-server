package com.urayatal.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomDto implements Serializable {
    private Long roomId;
    private String user1UserName;
    private String user2UserName;
    private List<MessageDto> messages;
    private long unreadMessage;
}
