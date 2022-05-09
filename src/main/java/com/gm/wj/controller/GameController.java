package com.gm.wj.controller;

import com.gm.wj.entity.Game;
import com.gm.wj.result.Result;
import com.gm.wj.result.ResultFactory;
import com.gm.wj.service.GameService;
import com.gm.wj.service.UserBehaviorService;
import com.gm.wj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;

/**
 * Jotter controller.
 *
 * @author Brons
 * @date 2022/1/14 20:33
 */
@RestController
public class GameController {
    @Autowired
    GameService gameService;
    @Autowired
    UserBehaviorService userBehaviorService;

    @GetMapping("/api/games")
    public Result listgames() {
        return ResultFactory.buildSuccessResult(gameService.list());
    }

    @PostMapping("/api/admin/content/games")
    public Result addOrUpdategames(@RequestBody @Valid Game game) {
        gameService.addOrUpdate(game);
        return ResultFactory.buildSuccessResult("修改成功");
    }
    @PostMapping("/api/admin/info/games")
    public Result postGameInfo(MultipartFile[] uploadFiles, HttpServletRequest request,Game game) throws IOException {

        if(uploadFiles.length>0){
            String filePath = "";
            String folder = "e:/wjproject/White-Jotter/file/game/";
            for(int i=0;i< uploadFiles.length;i++) {
                MultipartFile uploadFile = uploadFiles[i];
                File imageFolder = new File(folder);
                File f = new File(imageFolder, StringUtils.getRandomString(6) + uploadFile.getOriginalFilename()
                        .substring(uploadFile.getOriginalFilename().length() - 4));
                if (!f.getParentFile().exists())
                    f.getParentFile().mkdirs();
                uploadFile.transferTo(f);
                if (i != 0) {
                    filePath = filePath + ";";
                }
                filePath = filePath + "/api/file/game/" + f.getName();
            }
            game.setFilepath(filePath);
        }
        gameService.addOrUpdate(game);
        return ResultFactory.buildSuccessResult("发布成功");
    }

    @PostMapping("/api/admin/content/games/delete")
    public Result deletegame(@RequestBody @Valid Game game) {
        gameService.deleteById(game.getId());
        return ResultFactory.buildSuccessResult("删除成功");
    }

    @GetMapping("/api/game/search")
    public Result searchResult(@RequestParam("keywords") String keywords) {
        if ("".equals(keywords)) {
            return ResultFactory.buildSuccessResult(gameService.list());
        } else {
            return ResultFactory.buildSuccessResult(gameService.Search(keywords));
        }
    }

    @GetMapping("/api/categories/{cid}/games")
    public Result listByCategory(@PathVariable("cid") int cid) {
        if (0 != cid) {
            return ResultFactory.buildSuccessResult(gameService.listByCategory(cid));
        } else {
            return ResultFactory.buildSuccessResult(gameService.list());
        }
    }

    @PostMapping("/api/admin/content/games/covers")
    public String coversUpload(MultipartFile file) {
        String folder = "e:/wjproject/White-Jotter/file/img/";
        File imageFolder = new File(folder);
        File f = new File(imageFolder, StringUtils.getRandomString(6) + file.getOriginalFilename()
                .substring(file.getOriginalFilename().length() - 4));
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        try {
            file.transferTo(f);
            //String imgURL = "http://localhost:8443/api/file/" + f.getName();
            String imgURL = "/api/file/img/" + f.getName();
            return imgURL;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    @GetMapping("/api/game/behavior")
    public Result addBehavior(@RequestParam("uid") String uidStr,@RequestParam("gid") String gidStr){
        int uid = Integer.parseInt(uidStr);
        int gid = Integer.parseInt(gidStr);
        userBehaviorService.addData(uid,gid);
        return ResultFactory.buildSuccessResult("添加成功");
    }
    @GetMapping("/api/game/recommend")
    public Result RecommendGame(@RequestParam("uid") String uidStr){
        int uid = Integer.parseInt(uidStr);
        gameService.addMore();
        return ResultFactory.buildSuccessResult("成功");
    }
}
