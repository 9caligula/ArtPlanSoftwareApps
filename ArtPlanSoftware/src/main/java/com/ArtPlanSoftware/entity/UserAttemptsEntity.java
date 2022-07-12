package com.ArtPlanSoftware.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_attempts", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username")
})
@Getter
@Setter
@ToString
public class UserAttemptsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private int attempts;

    private Date lastModified;

    public UserAttemptsEntity() { }

    public UserAttemptsEntity(String username, int attempts, Date lastModified) {
        this.username = username;
        this.attempts = attempts;
        this.lastModified = lastModified;
    }
}
