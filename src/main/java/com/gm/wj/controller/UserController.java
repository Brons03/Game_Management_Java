package com.gm.wj.controller;

import com.gm.wj.entity.*;
import com.gm.wj.result.Result;
import com.gm.wj.result.ResultFactory;
import com.gm.wj.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;

/**
 * User controller.
 *
 * @author Evan
 * @date 2019/11
 */

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    AdminUserRoleService adminUserRoleService;

    @GetMapping("/api/admin/user")
    public Result listUsers() {
        return ResultFactory.buildSuccessResult(userService.list());
    }

    @PutMapping("/api/admin/user/status")
    public Result updateUserStatus(@RequestBody @Valid User requestUser) {
        userService.updateUserStatus(requestUser);
        return ResultFactory.buildSuccessResult("用户状态更新成功");
    }

    @PutMapping("/api/admin/user/password")
    public Result resetPassword(@RequestBody @Valid User requestUser) {
        userService.resetPassword(requestUser);
        return ResultFactory.buildSuccessResult("重置密码成功");
    }

    @PostMapping("/api/admin/user/avatar")
    public Result updateAvatar(@RequestParam(value = "file") MultipartFile file, HttpServletRequest req, User requestUser) throws IOException {
        if(file!=null){
            String uploadPathImg = "e:/wjproject/White-Jotter/file/";
            String fileName = System.currentTimeMillis() + file.getOriginalFilename();
            String upload_file_dir=uploadPathImg;//注意这里需要添加目录信息
            String destFileName =  uploadPathImg +fileName;
            //第一次运行的时候，这个文件所在的目录往往是不存在的，这里需要创建一下目录（创建到了webapp下uploaded文件夹下）
            File upload_file_dir_file = new File(upload_file_dir);
            if (!upload_file_dir_file.exists())
            {
                upload_file_dir_file.mkdirs();
            }
            //把浏览器上传的文件复制到希望的位置
            File targetFile = new File(upload_file_dir_file, fileName);
            file.transferTo(targetFile);
            requestUser.setAvatar(fileName);
            int res = userService.updateUserAvatar(requestUser);
            if(res==0){
                return ResultFactory.buildFailResult("用户不存在");
            }
            return ResultFactory.buildSuccessResult("头像设置成功");
        }
        return ResultFactory.buildFailResult("图片为空");
    }

    @PutMapping("/api/admin/user")
    public Result editUser(@RequestBody @Valid User requestUser) {
        userService.editUser(requestUser);
        return ResultFactory.buildSuccessResult("修改用户信息成功");
    }
}
