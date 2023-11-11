package com.blue.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@TableName("tbl_blog")
@NoArgsConstructor
public class Blog {

    @TableId(value="blog_id",type = IdType.AUTO)
    private Integer blogId;

    @TableField(value="blog_name")
    private String blogName;


    @TableField(value="blog_flag")
    private Integer blogFlag;

    @TableField(value="blog_view")
    private Integer blogView;

    @TableField(value="blog_like")
    private Integer blogLike;

    @TableField(value="blog_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date blogTime;

    @TableField(value="user_id")
    private Integer uId;

    @TableField(value="blog_description")
    private String blogDescription;
//    @TableField(value="blogContent")
//    private String blogContent;

    @TableField(value="blog_content")
    private String blogContent;

    @TableField(exist = false)
    private List<Integer> typeid;

    @TableField(value="blog_picture")
    private String blogPicture;

    @TableField(value="blog_publish")
    private Integer blogPublish;

    @TableField(exist = false)
    private List<Integer> deleteid;


}
