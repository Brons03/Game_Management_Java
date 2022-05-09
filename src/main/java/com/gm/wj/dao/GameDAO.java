package com.gm.wj.dao;

import com.gm.wj.entity.Game;
import com.gm.wj.entity.Gcategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameDAO extends JpaRepository<Game,Integer> {
    List<Game> findAllByGcategory(Gcategory gcategory);
    List<Game>findAllByArticleTitleLikeOrAuthorLike(String keyword1, String keyword2);
}
