package com.urayatal.server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="Message_SEQ_Gen")
    @SequenceGenerator(name="Message_SEQ_Gen", sequenceName="Message_SEQ",allocationSize = 50)
    private Long msgId;
    private String data;
    private String messageTo;
    private String messageFrom;
    private String type;
    private String timestamp;
    private String status;
    @ManyToOne(cascade =CascadeType.ALL)
    @JoinColumn(name = "chatRomId",referencedColumnName = "roomId", nullable = false)
    private ChatRoom chatRoom;

}
