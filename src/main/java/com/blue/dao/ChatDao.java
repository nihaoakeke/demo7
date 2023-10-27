package com.blue.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blue.domain.Chat;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper
public interface ChatDao extends BaseMapper<Chat> {

}
