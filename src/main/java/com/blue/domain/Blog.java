package com.blue.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("tbl_blog")
@NoArgsConstructor
public class Blog {

    @TableId(value="blogId",type = IdType.AUTO)
    private Integer blogId;

    @TableField(value="blogName")
    private String blogName;

    @TableField(value="blogType")
    private String blogType;

    @TableField(value="blogFlag")
    private Integer blogFlag;

    @TableField(value="blogView")
    private Integer blogView;

    @TableField(value="blogLike")
    private Integer blogLike;

    @TableField(value="blogTime")
    private Date blogTime;

    @TableField(value="uId")
    private Integer uId;

    @TableField(value="blogDescription")
    private String blogDescription;
//    @TableField(value="blogContent")
//    private String blogContent;

    @TableField(value="blogContent")
    private String blogContent;

}
