package com.blue.controller;

import com.blue.domain.Article;
import com.blue.domain.Result;
import com.blue.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableScheduling
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private  ArticleService articleService;



//    @Autowired
//    public ArticleController(ArticleService articleService) {
//        this.articleService = articleService;
//    }



    @GetMapping("/getById/{id}")
    public Result getById(@PathVariable Integer id) throws Exception {
        Article article = articleService.getById(id);
        //RedisUtil.zIncrementScore("viewNum",id.toString(),1);
        if (article == null) {
            return new Result(10,article,"kke");
        }
        return new Result(11,article,"kek");
    }
}
