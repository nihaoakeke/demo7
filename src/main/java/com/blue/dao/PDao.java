package com.blue.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blue.domain.Book;
import com.blue.domain.P;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PDao extends BaseMapper<P> {


//    @Select("select * from tbl_p where pid = #{pid}")
//    public P getById(Integer pid);
}
