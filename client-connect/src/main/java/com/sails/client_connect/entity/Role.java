package com.sails.client_connect.entity;

import com.sails.client_connect.entity.RoleName;
import com.sails.client_connect.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Role {

    @Id
    @GeneratedValue
    private Long role_id;

    @Enumerated(EnumType.STRING)
    @Column(unique=true)
    private RoleName name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

}



