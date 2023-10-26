package com.blue.service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.blue.dao.ArticleMapper;
import com.blue.domain.Article;
import org.springframework.stereotype.Service;

/**
 * @author wl
 * @date 2022/2/22
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Override
    public Boolean updateNumById(Article article) {
//        return baseMapper.updateNumById(article) > 0;
        return baseMapper.updateById(article)>0;
    }
}
