package server.database.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
@Data
public class Role extends BaseEntity {

    @Column(name = "name")
    private String name;

//    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
//    private List<User> users;

}
