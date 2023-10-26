package com.blue.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blue.domain.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDao extends BaseMapper<User> {

//      @Select("select * from tbl_user where u_id = #{u_id}")
//      public User getById(Integer u_id);


}
