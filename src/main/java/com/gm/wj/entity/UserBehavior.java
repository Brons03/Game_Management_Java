package com.gm.wj.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_behavior")
@ToString
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class UserBehavior {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    /**
     * Userid.
     */
    int uid;
    /**
     * Gameid.
     */
    int gid;
    /**
     * behavior_type.
     */
    int behavior_type;
    /**
     * Categoryid.
     */
    int cid;
    /**
     * Count.
     */
    int count;
    /**
     * Weight.
     */
    double weight;
    /**
     * Time.
     */
    String time;
}
