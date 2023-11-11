package com.blue.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("tbl_blog_type")
@NoArgsConstructor
public class BlogType {

    @TableId(value="id",type= IdType.AUTO)
    private Integer id;

    @TableField(value="blog_id")
    private Integer blogId;

    @TableField(value="type_id")
    private Integer typeId;

}
