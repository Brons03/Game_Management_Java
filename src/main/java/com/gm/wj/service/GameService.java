package com.gm.wj.service;

import com.gm.wj.dao.GameDAO;
import com.gm.wj.entity.Game;
import com.gm.wj.entity.Gcategory;
import com.gm.wj.redis.RedisService;
import com.gm.wj.util.CastUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GameService {
    @Autowired
    private GameDAO gameDAO;
    @Autowired
    private GcategoryService gcategoryService;
    @Autowired
    private RedisService redisService;

    private String game_key = "gamelist";

    public List<Game> list() {
        List<Game> games;
        String key = "gamelist";
        Object gameCache = redisService.get(game_key);

        if (gameCache == null) {
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            games = gameDAO.findAll(sort);
            redisService.set(key, games);
        } else {
            games = CastUtils.objectConvertToList(gameCache, Game.class);
        }
        return games;
    }

//    直接用注解实现缓存
//    @Cacheable(value = RedisConfig.REDIS_KEY_DATABASE)
//    public List<Book> list() {
//        List<Book> books;
//        Sort sort = new Sort(Sort.Direction.DESC, "id");
//        books = bookDAO.findAll(sort);
//        return books;
//    }

    public void addOrUpdate(Game game) {
        redisService.delete(game_key);
        gameDAO.save(game);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        redisService.delete(game_key);
    }
    public void addMore() {
        List<Game>games = new ArrayList<>();
        for(int i=3;i<21;i++){
            Gcategory gcategory = new Gcategory();
            gcategory.setId(i);
            for(int j=0;j<5;j++){
                Game game = new Game();
                game.setArticleAbstract(""+i);
                game.setArticleContentHtml(""+i);
                Date date = new Date();
                game.setArticleDate(date);
                game.setArticleContentMd(""+i);
                game.setArticleContentHtml(""+i);
                game.setArticleTitle(""+i);
                game.setGcategory(gcategory);
                game.setWatchCount(0);
                games.add(game);
            }
        }
        gameDAO.saveAll(games);
    }
    public void deleteById(int id) {
        redisService.delete(game_key);
        gameDAO.deleteById(id);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        redisService.delete(game_key);
    }

    public List<Game> listByCategory(int cid) {

        Gcategory category = gcategoryService.get(cid);
        return gameDAO.findAllByGcategory(category);
    }

    public List<Game> Search(String keywords) {
        return gameDAO.findAllByArticleTitleLikeOrAuthorLike('%' + keywords + '%', '%' + keywords + '%');
    }
}
