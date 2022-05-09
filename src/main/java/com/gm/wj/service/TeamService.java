package com.gm.wj.service;

import com.gm.wj.dao.TeamDAO;
import com.gm.wj.entity.Team;
import com.gm.wj.entity.Gcategory;
import com.gm.wj.redis.RedisService;
import com.gm.wj.util.CastUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {
    @Autowired
    private TeamDAO teamDAO;
    @Autowired
    private GcategoryService gcategoryService;
    @Autowired
    private RedisService redisService;

    private String team_key = "teamlist";

    public List<Team> list() {
        List<Team> teams;
        Object teamCache = redisService.get(team_key);

        if (teamCache == null) {
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            teams = teamDAO.findAll(sort);
            redisService.set(team_key, teams);
        } else {
            teams = CastUtils.objectConvertToList(teamCache, Team.class);
        }
        return teams;
    }

//    直接用注解实现缓存
//    @Cacheable(value = RedisConfig.REDIS_KEY_DATABASE)
//    public List<Book> list() {
//        List<Book> books;
//        Sort sort = new Sort(Sort.Direction.DESC, "id");
//        books = bookDAO.findAll(sort);
//        return books;
//    }

    public void addOrUpdate(Team team) {
        redisService.delete(team_key);
        teamDAO.save(team);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        redisService.delete(team_key);
    }

    public void deleteById(int id) {
        redisService.delete(team_key);
        teamDAO.deleteById(id);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        redisService.delete(team_key);
    }

    public List<Team> listByCategory(int cid) {

        Gcategory category = gcategoryService.get(cid);
        return teamDAO.findAllByGcategory(category);
    }

    public List<Team> Search(String keywords) {
        return teamDAO.findAllByArticleTitleLikeOrAuthorLike('%' + keywords + '%', '%' + keywords + '%');
    }
}
