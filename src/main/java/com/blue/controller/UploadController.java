package com.blue.controller;

import com.blue.config.Log;
import com.blue.dao.UserDao;
import com.blue.domain.Code;
import com.blue.domain.Result;
import com.blue.domain.User;
import com.blue.utils.UploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

@RestController
@RequestMapping("/upload")
public class UploadController {
    @Autowired UserDao userDao;


    @Log(operation = "上传用户头像文件")
    @GetMapping("/{uid}")
    public Result upload(MultipartFile file, @PathVariable Integer uid) throws IOException {

        System.out.println("dadas_____________________");
        //获取文件的内容
        InputStream is = file.getInputStream();
        //获取原始文件名
        String originalFilename = file.getOriginalFilename();

        //生成一个uuid名称出来
        String uuidFilename = UploadUtils.getUUIDName(originalFilename);
        System.out.println(uuidFilename+">>>>>>>>>>>>>>>>>");

        //产生一个随机目录
//        String randomDir = UploadUtils.getDir();
//System.out.println(randomDir);
        //File fileDir = new File("D:/code/demo7/src/main/resources/static/images" + randomDir);
        //若文件夹不存在,则创建出文件夹
        //if (!fileDir.exists()) {
            //fileDir.mkdirs();
        //}
        //创建新的文件夹
        File newFile = new File("D:/code/demo7/src/main/resources/static/images/" +  uuidFilename);
        //将文件输出到目标的文件中
        file.transferTo(newFile);

        //将保存的文件路径更新到用户信息headimg中
        String savePath = "http://localhost/images/"+uuidFilename;

        //获取当前的user
        User user =  userDao.selectById(uid);
        System.out.println(uid);
        System.out.println(user.getUName());
        //设置头像图片路径
        user.setUPicture(savePath);

        user.setUId(uid);

        //调用业务更新user
        userDao.updateById(user);
        //生成响应 : 跳转去用户详情页面
        user.setUPassword(null);
        return new Result(Code.GET_OK,user,"上传成功");
    }












//    @Autowired
//    ResourceLoader resourceLoader;
//
//    @GetMapping("/get/{filename:.+}")
//    public ResponseEntity get(
//                              @PathVariable String filename) {
//        //1.根据用户名去获取相应的图片
//        Path path = Paths.get("D:/code/demo7/src/main/resources/static/images/" + filename);
//        //2.将文件加载进来
//        Resource resource = resourceLoader.getResource("file:" + path.toString());
//        //返回响应实体
//        return ResponseEntity.ok(resource);
//    }

}