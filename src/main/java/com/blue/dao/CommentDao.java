package com.blue.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blue.domain.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentDao extends BaseMapper<Comment> {


}
