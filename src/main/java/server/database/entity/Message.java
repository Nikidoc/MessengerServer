package server.database.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "messages")
public class Message extends BaseEntity {

    @Column(name = "author_id")
//    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
//    @JoinColumn(name = "author_id")
    private Long author;

    @Column(name = "chat_id")
//    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinColumn(name = "chat_id")
    private Long chat;

    @Column(name = "text")
    private String text;



}
