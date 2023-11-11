package com.blue.controller;




import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blue.config.Log;
import com.blue.dao.BlogDao;
import com.blue.dao.BlogFileDao;
import com.blue.dao.UserMapper;
import com.blue.domain.*;
import com.blue.service.BloagService;
import com.blue.utils.RedisUtil;
import com.blue.utils.UploadUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;



@RestController
@RequestMapping("/upload")
public class FileController {


    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BloagService bloagService;
    @Autowired
    private BlogFileDao blogFileDao;
    @Autowired
    private BlogDao blogDao;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 设置用户的头像文件
     * @param file
     * @param uid
     * @return
     * @throws IOException
     */
    @Log(operation = "上传用户头像文件")
    @PostMapping("/{uid}")
    public Result uploadavatar(MultipartFile file, @PathVariable Integer uid) throws IOException {
        InputStream is = file.getInputStream();
        String originalFilename = file.getOriginalFilename();
        String uuidFilename = UploadUtils.getUUIDName(originalFilename);
        String directoryPath = "/var/www/html/images/"; // 服务器上存储文件的目录路径
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs(); // 创建目录及其父目录
        }
        File newFile = new File(directoryPath + uuidFilename);
//        File newFile = new File("/D/code/demo7/src/main/resources/images/" +  uuidFilename);
//        File newFile = new File("D:/code/demo7/src/main/images/" +  uuidFilename);
//        File newFile = new File("D:/code/demo7/src/main/resources/images/" +  uuidFilename);
        file.transferTo(newFile);
//        String savePath = "http://localhost/images/"+uuidFilename;
        String savePath="http://8.130.27.148/images/"+uuidFilename;
        User user =  userMapper.selectById(uid);
        System.out.println(uid);
        System.out.println(user.getUname());
        //设置头像图片路径
        user.setUPicture(savePath);
        user.setUId(uid);
        userMapper.updateById(user);
        return new Result(Code.GET_OK,user,"上传成功");
    }
    /**
     * 保存博客的封面图片
     * @param file
     * @param bid
     * @return
     * @throws IOException
     */
    @Log(operation = "保存博客的封面图片")
    @PostMapping("/savepicture/{bid}")
    public Result uploadBlogPicture(MultipartFile file, @PathVariable Integer bid) throws IOException {
        InputStream is = file.getInputStream();
        String originalFilename = file.getOriginalFilename();
        String uuidFilename = UploadUtils.getUUIDName(originalFilename);
        String directoryPath = "/var/www/html/images/"; // 服务器上存储文件的目录路径
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs(); // 创建目录及其父目录
        }
        File newFile = new File(directoryPath + uuidFilename);
        file.transferTo(newFile);
        String savePath="http://8.130.27.148/images/"+uuidFilename;
        Blog blog = blogDao.selectById(bid);
        //设置头像图片路径
        blog.setBlogPicture(savePath);
        blog.setBlogId(bid);
        blogDao.updateById(blog);
        return new Result(Code.GET_OK,blog,"上传成功");
    }
    /**
     * 保存博客携带的附件
     * @param files
     * @param bid
     * @return
     * @throws IOException
     */
    @Log(operation = "保存博客的附件")
    @PostMapping("/savefile/{bid}")
    public Result uploadBlogFile(MultipartFile[] files, @PathVariable Integer bid) throws IOException {

        for(MultipartFile file:files) {
            InputStream is = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String uuidFilename = UploadUtils.getUUIDName(originalFilename);
            String directoryPath = "/var/www/html/images/"; // 服务器上存储文件的目录路径
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs(); // 创建目录及其父目录
            }
            File newFile = new File(directoryPath + uuidFilename);
            file.transferTo(newFile);
            String savePath = "http://8.130.27.148/images/" + uuidFilename;


            BlogFile blogFile = new BlogFile();
            blogFile.setBlogId(bid);
            blogFile.setBlogFile(savePath);
            blogFileDao.insert(blogFile);
        }

        return new Result(Code.GET_OK,"","上传成功");
    }
    /**
     * 下载博客的携带的附件
     * @param bid 博客的id
     * @param verifyCode 要输入的验证码
     * @return
     * @throws IOException
     */
    @GetMapping("/download/{bid}/{verifyCode}")
    public ResponseEntity<Resource> downloadMultipleFiles(@PathVariable Integer bid,@PathVariable String verifyCode) throws IOException {

        Map<String, Object> map = new HashMap<>();
        String code = (String) redisUtil.get("verifyCode");
        if(!verifyCode.equals(code)){
            map.put("msg","验证码错误");
            String json = new ObjectMapper().writeValueAsString(map);
            ByteArrayResource resource = new ByteArrayResource(json.getBytes());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resource);
        }
        QueryWrapper qw = new QueryWrapper();
        qw.eq("blog_id",bid);
        List<BlogFile> blogfileList = blogFileDao.selectList(qw);
        if(blogfileList==null) {
            map.put("msg","并没有相关的文件");
            String json = new ObjectMapper().writeValueAsString(map);
            ByteArrayResource resource = new ByteArrayResource(json.getBytes());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resource);
        }
        List<String> savePaths =new ArrayList<>();
        for(BlogFile blogFile:blogfileList){
            savePaths.add(blogFile.getBlogFile());
        }
        String directoryPath = "/var/www/html/images/"; // 服务器上存储文件的目录路径
        List<File> files = new ArrayList<>();
        for (String savePath : savePaths) {
            String filename = savePath.substring(savePath.lastIndexOf("/") + 1);
            File file = new File(directoryPath + filename);
            files.add(file);
        }

        List<Resource> resources = new ArrayList<>();
        for (File file : files) {
            Resource resource = new FileSystemResource(file);
            resources.add(resource);
        }

        String zipFilename = "download.zip";
        String zipFilePath = "/var/www/html/"; // Directory to store the zip file

        File zipFile = new File(zipFilePath + zipFilename);
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));

        for (Resource resource : resources) {
            ZipEntry zipEntry = new ZipEntry(resource.getFilename());
            zipOut.putNextEntry(zipEntry);

            InputStream fileInputStream = resource.getInputStream();
            IOUtils.copy(fileInputStream, zipOut);

            fileInputStream.close();
            zipOut.closeEntry();
        }

        zipOut.close();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + zipFilename);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        Resource zipResource = new FileSystemResource(zipFile);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(zipFile.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(zipResource);
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