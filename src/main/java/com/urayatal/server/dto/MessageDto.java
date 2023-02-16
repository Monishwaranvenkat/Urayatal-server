package com.urayatal.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto implements Serializable {

    //To receive new message
    public MessageDto(String data, String messageTo, String messageFrom, String type, String timestamp, String status, Long chatRoomRoomId) {
        this.data = data;
        this.messageTo = messageTo;
        this.messageFrom = messageFrom;
        this.type = type;
        this.timestamp = timestamp;
        this.status = status;
        this.chatRoomRoomId = chatRoomRoomId;
    }
    // To receive message for first time
    public MessageDto(String data, String messageTo, String messageFrom, String type, String timestamp, String status) {
        this.data = data;
        this.messageTo = messageTo;
        this.messageFrom = messageFrom;
        this.type = type;
        this.timestamp = timestamp;
        this.status = status;
    }

    private Long msgId;
    private String data;
    private String messageTo;
    private String messageFrom;
    private String type;
    private String timestamp;
    private String status;
    private Long chatRoomRoomId;
}
