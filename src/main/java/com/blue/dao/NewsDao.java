package com.blue.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blue.domain.News;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface NewsDao extends BaseMapper<News> {

}
