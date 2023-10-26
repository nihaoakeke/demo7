package com.blue.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blue.domain.Article;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
   //public int updateNumById(Article article);

}
