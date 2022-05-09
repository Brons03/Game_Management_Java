package com.gm.wj.dao;

import com.gm.wj.entity.Gcategory;
import com.gm.wj.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamDAO extends JpaRepository<Team,Integer> {
    List<Team> findAllByGcategory(Gcategory gcategory);
    List<Team>findAllByArticleTitleLikeOrAuthorLike(String keyword1, String keyword2);
}
