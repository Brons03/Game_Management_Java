package com.gm.wj.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

/**
 * Category entity.
 *
 * @author Evan
 * @date 2019/4
 */
@Data
@Entity
@Table(name = "gcategory")
@ToString
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class Gcategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * Category name in Chinese.
     */
    private String name;
}
