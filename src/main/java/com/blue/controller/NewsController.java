package com.blue.controller;

import com.alibaba.druid.util.StringUtils;
import com.blue.config.Log;
import com.blue.dao.NewsDao;
import com.blue.domain.Blog;
import com.blue.domain.Code;
import com.blue.domain.News;
import com.blue.domain.Result;
import com.blue.service.NewsService;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/news")
public class NewsController {
    @Autowired
    private NewsDao newsDao;
    @Autowired
    private NewsService newsService;
    /**
     * 查询公告，根据news_id进行查询
     * @param id
     * @return
     */
    @Log(operation = "查询公告根据id主键")
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        if(id==0) {
            List<News> newsList = newsService.list();
            newsList.sort(Comparator.comparing(News::getNewsTime, Collections.reverseOrder()));
            Integer code = newsList != null ? Code.GET_OK : Code.GET_ERR;
            log.info("添加用户：{}", newsList);
            String msg = newsList != null ? "数据查询成功" : "数据查询失败，请重试！";
            Iterator<News> iterator = newsList.iterator();
            while (iterator.hasNext()){
                News news = iterator.next();
                String content = news.getNewsContent();
                news.setNewsContent(EmojiParser.parseToUnicode(content));
            }
            return new Result(code,newsList,msg);
        }
        else{
            News news = newsDao.selectById(id);
            Integer code = news != null ? Code.GET_OK : Code.GET_ERR;
            String msg = news != null ? "数据查询成功" : "数据查询失败，请重试！";
            String name = news.getNewsContent();
            news.setNewsContent(EmojiParser.parseToUnicode(name));
            return new Result(code,news,msg);
        }
    }

    @Log(operation = "查询本用户发表的公告")
    @GetMapping("/uid/{uid}")
    public Result getByUId(@PathVariable Integer uid) {
        List<News> newsList = newsService.selectByUid(uid);
        Integer code = newsList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = newsList != null ? "数据查询成功" : "数据查询失败，请重试！";
        Iterator<News> iterator = newsList.iterator();
        while (iterator.hasNext()){ //判断迭代器是否有元素
            News news = iterator.next();//获取集合下一个元素
            String name = news.getNewsContent();
            news.setNewsContent(EmojiParser.parseToUnicode(name));
        }
        return new Result(code,newsList,msg);
    }



    @Log(operation = "用户发布公告")
    @PostMapping("/manager")
    public Result saveNews(@RequestBody News news){

        if(news.getNewsContent()!=null){
            String nameCovert = EmojiParser.parseToAliases(news.getNewsContent());
            news.setNewsContent(nameCovert);
        }
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        news.setNewsTime(date);

        Integer num =newsDao.insert(news);
        Integer code = num!=null?Code.SAVE_OK:Code.SAVE_ERR;
        String msg = num!= null ? "数据保存成功" : "数据保存失败，请重试！";
        return new Result(code,news,msg);
    }

    @Log(operation = "删除公告")
    @DeleteMapping("/manager/{id}")
    public Result deleteNews(@PathVariable Integer id)
    {
        Integer num =newsDao.deleteById(id);
        Integer code = num!=null?Code.DELETE_OK:Code.DELETE_ERR;
        String msg = num!= null ? "数据删除成功" : "数据删除失败，请重试！";
        return new Result(code,"",msg);
    }

    @Log(operation = "更新公告信息")
    @PutMapping("/manager")
    public Result updateNews(@RequestBody News news)
    {
        Integer num = newsDao.updateById(news);
        if(news.getNewsContent()!=null){
            String nameCovert = EmojiParser.parseToAliases(news.getNewsContent());
            news.setNewsContent(nameCovert);
        }
        Integer code = num !=null ? Code.UPDATE_OK :Code.UPDATE_ERR;
        String msg = num != null?"数据更新成功":"数据更新失败，请重试";
        News news1 = newsDao.selectById(news.getNewsId());
        return new Result(code,news1,msg);
    }

}
