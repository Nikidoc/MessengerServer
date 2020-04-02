package server.database.entity;


import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "chats")
public class Chat extends BaseEntity{

//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "creator_id")
    @Column(name = "creator_id")
    private Long creatorId;

    @Column(name = "name")
    private String name;

    @Column(name = "isGroup", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean group;

//    @ManyToMany(mappedBy = "chats" , fetch = FetchType.LAZY)
//    @JoinTable(name = "UserToChat",
//                joinColumns = @JoinColumn(name = "chat_id"),
//                inverseJoinColumns = @JoinColumn(name = "user_id"))
//    private List<User> users;

}
