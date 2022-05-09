package com.gm.wj.controller;

import com.gm.wj.entity.Team;
import com.gm.wj.result.Result;
import com.gm.wj.result.ResultFactory;
import com.gm.wj.service.TeamService;
import com.gm.wj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;

@RestController
public class TeamController {
    @Autowired
    TeamService teamService;

    @GetMapping("/api/teams")
    public Result listteams() {
        return ResultFactory.buildSuccessResult(teamService.list());
    }

    @PostMapping("/api/admin/content/teams")
    public Result addOrUpdateteams(@RequestBody @Valid Team team) {
        teamService.addOrUpdate(team);
        return ResultFactory.buildSuccessResult("修改成功");
    }

    @PostMapping("/api/admin/content/teams/delete")
    public Result deleteteam(@RequestBody @Valid Team team) {
        teamService.deleteById(team.getId());
        return ResultFactory.buildSuccessResult("删除成功");
    }

    @GetMapping("/api/team/search")
    public Result searchResult(@RequestParam("keywords") String keywords) {
        if ("".equals(keywords)) {
            return ResultFactory.buildSuccessResult(teamService.list());
        } else {
            return ResultFactory.buildSuccessResult(teamService.Search(keywords));
        }
    }

    @GetMapping("/api/categories/{cid}/teams")
    public Result listByCategory(@PathVariable("cid") int cid) {
        if (0 != cid) {
            return ResultFactory.buildSuccessResult(teamService.listByCategory(cid));
        } else {
            return ResultFactory.buildSuccessResult(teamService.list());
        }
    }

    @PostMapping("/api/admin/content/teams/covers")
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
}
