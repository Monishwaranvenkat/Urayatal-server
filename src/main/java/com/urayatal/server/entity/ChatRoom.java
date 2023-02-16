package com.urayatal.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity

@NamedQuery(name = "ChatRoom.findAllByUser", query = "select distinct c from ChatRoom c where c.user2 = ?1 or c.user1 = ?1")
@NamedQuery(name = "ChatRoom.findChatRoomByUserName", query = "select distinct c from ChatRoom c where (c.user2.userName = ?2 and c.user1.userName = ?1) or (c.user2.userName = ?1 and c.user1.userName = ?2) ")
public class ChatRoom {

    @Id
    @Column(name = "roomId")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ChatRoom_SEQ_Gen")
    @SequenceGenerator(name="ChatRoom_SEQ_Gen", sequenceName="ChatRoom_SEQ",allocationSize = 50)
    private Long roomId;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "user1", nullable = false, referencedColumnName = "userName",unique = false)
    private User user1;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "user2", nullable = false, referencedColumnName = "userName",unique = false)
    private User user2;

    @OneToMany(mappedBy="chatRoom", cascade={CascadeType.ALL})
    @JsonBackReference
    private List<Message> messages;

    @Formula("(Select count(*) from message m where m.status = 'unread' and m.chat_rom_id =room_id)")
    private long unreadMessage;
}
