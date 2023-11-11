package com.blue.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blue.config.Log;
import com.blue.dao.*;
import com.blue.domain.*;
import com.blue.service.BloagService;
import com.blue.service.TypeService;
import com.blue.utils.ComparatorUtils;
import com.blue.utils.KeyUtils;
import com.blue.utils.RedisUtil;
import com.google.gson.Gson;
import com.vdurmont.emoji.EmojiParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/blogs")
public class BlogController {
    @Autowired
    private BlogLikeDao blogLikeDao;
    @Autowired
    private BlogDao blogDao;
    @Autowired
    private BloagService bloagService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private BlogTypeDao blogTypeDao;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private BlogFileDao blogFileDao;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private CollectDao collectDao;
    /**
     * 管理员审核博客状态
     * @param bid
     * @param flag
     * @return
     */
    @Log(operation = "审核博客状态")
    @GetMapping("/manager/check/{bid}/{flag}")
    public Result checkBlog(@PathVariable  Integer bid,@PathVariable Integer flag) {
        Blog blog =blogDao.selectById(bid);
        blog.setBlogFlag(flag);
        Integer num =blogDao.updateById(blog);
        Integer code = num != null ? Code.GET_OK : Code.GET_ERR;
        String msg = num != null ? "设置成功" : "设置失败，请重试！";
        return new Result(code,"",msg);
    }
    /**
     * 浏览某一篇博客，博客的浏览量会增加
     * @param id
     * @return
     * @throws Exception
     */
    @Log(operation = "浏览某一篇博客")
    @GetMapping("/getById/{id}")
    public Result getViewById(@PathVariable Integer id) throws Exception {
        Blog blog = bloagService.getById(id);
        //RedisUtil.zIncrementScore("viewNum",id.toString(),1);
        if (blog == null) {
            return new Result(10,blog,"kke");
        }
        return new Result(11,blog,"kek");
    }
    /**
     * 点赞某一篇博客，并且不能重复点赞
     * @param flag
     * @param bid
     * @param postid
     * @return
     */
    @Log(operation = "点赞某一篇博客，并且记录信息")
    @GetMapping("/like/{bid}/{postid}/{flag}")
    public Result likeBlog(@PathVariable Integer flag,@PathVariable Integer bid,@PathVariable Integer postid) {
        Blog blog =blogDao.selectById(bid);
        Integer authorid = blog.getUId();
        Map<String,String> map2 = new HashMap<>();
        if(flag==0){
            if(bloagService.checkLike(postid,authorid,bid)!=null){
                return new Result(Code.GET_OK,"","1");
            }else{
                return new Result(Code.GET_OK,"","0");
            }
        } else if(flag==1){
            if(bloagService.checkLike(postid,authorid,bid)!=null){
                return new Result(Code.GET_OK,"","您已经点过赞了");
            }
            Double score = redisUtil.zScore("BlogLike",KeyUtils.gengerateKey(authorid.toString(),postid.toString(),bid.toString()));
            if(score!=null){
                return new Result(Code.GET_OK,"","您只能点一次赞");
            }
            //把赞得数量加一
            redisUtil.zIncrementScore("blogLike",bid.toString(),1);
            //把联合用户加进去
            redisUtil.zAdd("BlogLike", KeyUtils.gengerateKey(authorid.toString(),postid.toString(),bid.toString()),1);
            Double score1 = redisUtil.zScore("blogLike",bid.toString());
            map2.put("blogLike", String.valueOf(score1.intValue()));
            return new Result(Code.GET_OK,map2,"点赞成功");
        }else{
            redisUtil.zIncrementScore("blogLike",bid.toString(),-1);
            redisUtil.zRemove("BlogLike",KeyUtils.gengerateKey(authorid.toString(),postid.toString(),bid.toString()));
            Double score1 = redisUtil.zScore("blogLike",bid.toString());
            map2.put("blogLike", String.valueOf(score1.intValue()));
            return new Result(Code.GET_OK,map2,"取消点赞成功");}
    }
    /**
     * 根据id，查询某一篇博客
     * @param id 传入的博客id
     * @return
     */
    @Log(operation = "根据id查询谋篇博客详情")
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
     Blog blog = blogDao.selectById(id);
     if(blog==null){
         return new Result(Code.GET_ERR,"","您查询的博客根本不存在，error！！！");
     }
     if(blog.getBlogFlag()==0||blog.getBlogPublish()==1){
         return new Result(Code.GET_ERR,"","博客未审核通过,或着还未发布");
     }
     List<Integer> list = bloagService.selectTidBybid(blog.getBlogId());
     String name = blog.getBlogContent();
     blog.setBlogContent(EmojiParser.parseToUnicode(name));
     blog.setTypeid(list);
     Integer code = blog != null ? Code.GET_OK : Code.GET_ERR;
     String msg = blog != null ? "数据查询成功" : "数据查询失败，请重试！";
     return new Result(code,blog,msg);
    }
    /**
     * 查询某一个用户发表的所有博客
     * @param uid
     * @param flag
     * @return
     */
    @Log(operation = "根据发表的用户进行查询博客详情")
    @GetMapping("/uid/{uid}/{flag}")
    public Result getByuid(@PathVariable Integer uid,@PathVariable Integer flag) {
        List<Blog> blogList1 = bloagService.selectBlogByUid(uid);
        Iterator<Blog> iterator = blogList1.iterator();
        List<Blog> blogList =new ArrayList<>();
        while (iterator.hasNext()){
            Blog blog = iterator.next();
            List<Integer> list = bloagService.selectTidBybid(blog.getBlogId());
            String name = blog.getBlogContent();
            blog.setBlogContent(EmojiParser.parseToUnicode(name));
            blog.setTypeid(list);
            if(blog.getBlogFlag()==1&&blog.getBlogPublish()==1){
                blogList.add(blog);
            }
        }
        Integer code = blogList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = blogList != null ? "数据查询成功" : "数据查询失败，请重试！";
        if(flag==0) {
            blogList.sort(Comparator.comparing(Blog::getBlogTime, Collections.reverseOrder()));
        } else if(flag==1){
            blogList.sort(Comparator.comparing(Blog::getBlogLike, Collections.reverseOrder()));
        }
        else if(flag==2){
            blogList.sort(Comparator.comparing(Blog::getBlogView, Collections.reverseOrder()));
        }else{
            Comparator<Blog> weightedComparator = new ComparatorUtils(0.7, 0.3);
            blogList1.sort(weightedComparator);
        }
        return new Result(code,blogList,msg);
    }
    /**
     * 进行分页操作的
     * @param flag
     * @param current
     * @param num
     * @param flag1
     * @return
     */
    @Log(operation = "根据分页页码查询博客详情内容")
    @GetMapping("/getAllpage/{flag}/{current}/{num}/{flag1}")
    public Result getAllPage(@PathVariable Integer flag,@PathVariable Integer current,@PathVariable Integer num,@PathVariable Integer flag1)
    {
            List<Blog> blogList2 = new ArrayList<>();
            if(flag==0) {blogList2 = bloagService.list();}
            else{blogList2=bloagService.selectBlogBytid(flag);
            }
            Iterator<Blog> iterator = blogList2.iterator();
            List<Blog> blogList =new ArrayList<>();
            while (iterator.hasNext()){
            Blog blog = iterator.next();
            List<Integer> list = bloagService.selectTidBybid(blog.getBlogId());
            String name = blog.getBlogContent();
            blog.setBlogContent(EmojiParser.parseToUnicode(name));
            blog.setTypeid(list);
            if(blog.getBlogFlag()==1&&blog.getBlogPublish()==1){
                blogList.add(blog);}
        }
            int page = current; // 页码，从1开始
            int pageSize = num; // 每页的大小
            int startIndex = (page - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, blogList.size());
            List<Blog> blogList1 =new ArrayList<>();
            if(flag1==0) {
                blogList.sort(Comparator.comparing(Blog::getBlogTime, Collections.reverseOrder()));
                blogList1 =blogList.subList(startIndex,endIndex);
                blogList1.sort(Comparator.comparing(Blog::getBlogTime, Collections.reverseOrder()));
            } else if(flag1==1){
                blogList.sort(Comparator.comparing(Blog::getBlogLike, Collections.reverseOrder()));
                blogList1 =blogList.subList(startIndex,endIndex);
                blogList1.sort(Comparator.comparing(Blog::getBlogLike, Collections.reverseOrder()));
            }
            else if(flag1==2){
                blogList.sort(Comparator.comparing(Blog::getBlogView, Collections.reverseOrder()));
                blogList1 =blogList.subList(startIndex,endIndex);
                blogList1.sort(Comparator.comparing(Blog::getBlogView, Collections.reverseOrder()));
            }else{
        Comparator<Blog> weightedComparator = new ComparatorUtils(0.7, 0.3);
        blogList1.sort(weightedComparator);
    }
            return new Result(Code.GET_OK,blogList1,"查询成功");
    }
    /**
     * 得到所有的数据，
     * @param flag 排序规则
     * @param num 查找的级别，是审核已经发布的，还是发布的未审核的
     * @return
     */
    @Log(operation = "得到所有数据")
    @GetMapping("/getAllBlog/{num}/{flag}")
    public Result getAllBlog(@PathVariable Integer flag,@PathVariable Integer num)
    {
        List<Blog> blogList = bloagService.selectPublish();
        Iterator<Blog> iterator = blogList.iterator();
        List<Blog> blogList1 = new ArrayList<>();
        while (iterator.hasNext()) {
            Blog blog = iterator.next();
            List<Integer> list = bloagService.selectTidBybid(blog.getBlogId());
            String name = blog.getBlogContent();
            blog.setBlogContent(EmojiParser.parseToUnicode(name));
            blog.setTypeid(list);
            if (num == 0) {
                blogList1.add(blog);
            } else {
                if (blog.getBlogFlag() == 1) {
                    blogList1.add(blog);}
            }
        }
        Integer code = blogList1 != null ? Code.GET_OK : Code.GET_ERR;
        String msg = blogList1 != null ? "数据查询成功" : "数据查询失败，请重试！";
        if(flag==0) {
            blogList1.sort(Comparator.comparing(Blog::getBlogTime, Collections.reverseOrder()));
        } else if(flag==1){
            blogList1.sort(Comparator.comparing(Blog::getBlogLike, Collections.reverseOrder()));
        }
        else if(flag==2){
            blogList1.sort(Comparator.comparing(Blog::getBlogView, Collections.reverseOrder()));
        }else{
        Comparator<Blog> weightedComparator = new ComparatorUtils(0.7, 0.3);
        blogList1.sort(weightedComparator);}
      return new Result(code,blogList1,msg);
     }

    /**
     * 更新博客信息
     * @param blog 传入的博客的信息
     * @return
     */
    @Log(operation = "更新博客信息")
    @PutMapping
    public Result updateBlog(@RequestBody Blog blog) {
        Integer num = blogDao.updateById(blog);
        Integer code = num !=null ? Code.UPDATE_OK :Code.UPDATE_ERR;
        String msg = num != null?"数据更新成功":"数据更新失败，请重试";
        return new Result(code,blog,msg);
    }
    /**
     * 删除博客
     * @param blog 传入要删除博客的数组id，可以多个博客
     * @return
     */
    @Log(operation = "删除博客")
    @DeleteMapping("/manager")
    public Result deleteBlog(@RequestBody Blog blog) {
        List<Integer> list =blog.getDeleteid();
        for(Integer bid :list) {
            QueryWrapper qw = new QueryWrapper<>();
            qw.eq("blog_id", bid);
            blogTypeDao.delete(qw);
            collectDao.delete(qw);
            commentDao.delete(qw);
            blogFileDao.delete(qw);
            blogLikeDao.delete(qw);
            redisUtil.zRemove("blogView", bid.toString());
            redisUtil.zRemove("blogLike", bid.toString());
            Integer num =blogDao.deleteById(bid);
        }
        return new Result(Code.GET_OK,"","删除成功");
    }
    /**
     *
     * @param
     * @return
     */
    @Log(operation = "删除博客")
    @DeleteMapping("/delete")
    public Result userdeleteBlog(@RequestBody Blog blog) {
        List<Integer> list =blog.getDeleteid();
        for(Integer bid :list) {
            QueryWrapper qw = new QueryWrapper<>();
            qw.eq("blog_id", bid);
            blogTypeDao.delete(qw);
            collectDao.delete(qw);
            commentDao.delete(qw);
            blogFileDao.delete(qw);
            blogLikeDao.delete(qw);
            redisUtil.zRemove("blogView", bid.toString());
            redisUtil.zRemove("blogLike", bid.toString());
            Integer num =blogDao.deleteById(bid);
        }
        return new Result(Code.GET_OK,"","删除成功");
    }
    /**
     * 发布某一篇博客
     * @param blog 要发布的博客
     * @return
     */
    @Log(operation = "发表某一篇博客")
    @PostMapping("/register")
    public Result saveBlog(@RequestBody Blog blog){
        if(blog.getBlogName()==null||blog.getBlogContent()==null||blog.getUId()==null) {
            return new Result(Code.GET_ERR,blog,"缺少信息，请补充好信息在发表");
        }
        List<Integer> list =blog.getTypeid();
        if(blog.getBlogContent()!=null){
            String nameCovert = EmojiParser.parseToAliases(blog.getBlogContent());
            blog.setBlogContent(nameCovert);
        }
        Date publishdate = blog.getBlogTime();
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        if(publishdate.equals(null)||date.compareTo(publishdate)>=0){
            blog.setBlogPublish(1);
            blog.setBlogTime(date);
        }
        /**
         * 管理员端未做好的先设置博客默认审核通过
         */
        blog.setBlogTime(publishdate);
        blog.setBlogFlag(1);
        Integer num = blogDao.insert(blog);
        for(Integer i:list){
            BlogType blogType = new BlogType();
            blogType.setTypeId(i);
            blogType.setBlogId(blog.getBlogId());
            blogTypeDao.insert(blogType);
        }
        Integer code = num !=null ? Code.SAVE_OK :Code.SAVE_ERR;
        String msg = num != null?"数据保存成功":"数据保存失败，请重试";
        return new Result(code,blog,msg);
    }
    /**
     * 根据博客类型进行查询
     * @param tid
     * @param flag
     * @return
     */
    @Log(operation = "根据博客类型进行查询")
    @GetMapping("/type/{tid}/{flag}")
    public Result getByType(@PathVariable Integer tid,@PathVariable Integer flag) {
        List<Blog> blogList1 = bloagService.selectBlogBytid(tid);
        List<Blog> blogList =new ArrayList<>();
        for(Blog blog : blogList1){
            if(blog.getBlogFlag()==1){
                blogList.add(blog);
            }
        }
        Integer code = blogList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = blogList != null ? "数据查询成功" : "数据查询失败，请重试！";
        if(flag==0) {
            blogList.sort(Comparator.comparing(Blog::getBlogTime, Collections.reverseOrder()));
        } else if(flag==1){
            blogList.sort(Comparator.comparing(Blog::getBlogLike, Collections.reverseOrder()));
        }
        else if(flag ==2){
            blogList.sort(Comparator.comparing(Blog::getBlogView, Collections.reverseOrder()));
        }else{
            Comparator<Blog> weightedComparator = new ComparatorUtils(0.7, 0.3);
            blogList1.sort(weightedComparator);
        }
        return new Result(code,blogList,msg);
    }
    /**
     * 获取所有的博客类型
     * @param
     * @return
     */
    @Log(operation = "获取所有的博客类型")
    @GetMapping("/getAllType")
    public Result getAllType(){
        List<Type> typeList = typeService.list();
        Integer code = typeList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = typeList != null ? "数据查询成功" : "数据查询失败，请重试！";
        return new Result(code,typeList,msg);
    }
    /**
     * 根据博客名字进行模糊查询
     * @param blogName
     * @return
     */
    @Log(operation = "模糊查询博客名字")
    @GetMapping("/likename")
    public Result getBlogLikename(@RequestParam(name = "blogName",required = true) String blogName) {
        List<Blog> blogList1 =bloagService.selectLikeName(blogName);
        List<Blog> blogList =new ArrayList<>();
        for(Blog blog : blogList1){
            if(blog.getBlogFlag()==1&&blog.getBlogPublish()==1){
                blogList.add(blog);
            }
        }
        Integer code = blogList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = blogList != null ? "数据查询成功" : "数据查询失败，请重试！";
        return new Result(code,blogList,msg);
    }
    /**
     * 查询附件是否存在
     * @param bid
     * @return
     */
    @GetMapping("/checkfile/{bid}")
    public Result checkBlogFileExist(@PathVariable Integer bid) {
        Boolean flag = bloagService.checkBlogFile(bid);
        String msg=null;
        if(flag==false) {
            msg="0";}
        else{
            msg="1";}
        return new Result(Code.GET_OK,"",msg);
    }
    /**
     * 对某一篇博客进行点赞
     * @param id
     * @return
     */
    @Log(operation = "对某篇博客确认点赞")
    @GetMapping("/okLike/{id}")
    public Result OKLikeById(@PathVariable Integer id) {
        Blog blog = bloagService.getById(id);
        if (blog == null) {
            return new Result(Code.GET_ERR,blog,"点赞失败，请重试");
        }
        return new Result(Code.GET_OK,blog,"点赞成功");
    }
    /**
     * 对某一篇博客取消点赞
     * @param id
     * @return
     */
    @Log(operation = "对某篇博客取消点赞")
    @GetMapping("/flaseLike/{id}")
    public Result falseLikeById(@PathVariable Integer id) {
        Blog blog = bloagService.getById(id);
        if (blog == null) {
            return new Result(Code.GET_ERR,blog,"取消点赞失败，请重试");
        }
        return new Result(Code.GET_OK,blog,"取消点赞成功");
    }

}
