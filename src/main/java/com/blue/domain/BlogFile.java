package com.blue.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@TableName("tbl_blog_file")
@NoArgsConstructor
public class BlogFile {

    @TableId(value="id",type = IdType.AUTO)
    private Integer id;

    @TableField(value="blog_id")
    private Integer blogId;

    @TableField(value="blog_file")
    private String blogFile;

    @TableField(exist = false)
    private List<String> filename;
}
