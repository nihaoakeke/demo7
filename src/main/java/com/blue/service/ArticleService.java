package com.blue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blue.domain.Article;

public interface ArticleService extends IService<Article> {
    public Boolean updateNumById(Article article);
}
