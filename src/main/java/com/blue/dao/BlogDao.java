package com.blue.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blue.domain.Blog;
import com.blue.domain.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BlogDao extends BaseMapper<Blog> {

//    @Select("select * from tbl_blog where uId = #{uId}")
//    public Blog getByuId(Integer uId);

//    @Select("select blogType from tbl_blog")
//    public Blog getAlltype();
}
