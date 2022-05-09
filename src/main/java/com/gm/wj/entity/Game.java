package com.gm.wj.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;


/**
 * Game entity.
 *
 * @author brons
 * @date 2022/4/14 20:25
 */
@Data
@Entity
@Table(name = "game")
@ToString
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NotNull(message = "id 不能为 null")
    private int id;

    /**
     * Article title.
     */
    @NotEmpty(message = "文章标题不能为空")
    private String articleTitle;
    /**
     * Article author.
     */
    private String author;

    /**
     * Article content after render to html.
     */
    private String articleContentHtml;

    /**
     * Article content in markdown syntax.
     */
    private String articleContentMd;

    /**
     * Article abstract.
     */
    private String articleAbstract;

    /**
     * Article cover's url.
     */
    private String articleCover;

    /**
     * Article release date.
     */
    private Date articleDate;
    /**
     * Article watch count.
     */
    private int watchCount;
    /**
     * Article file.
     */
    private String filepath;

    /**
     * Gctegory gid.
     */
    @ManyToOne
    @JoinColumn(name="gid")
    private Gcategory gcategory;
}
