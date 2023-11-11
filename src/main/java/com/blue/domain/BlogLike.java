package com.blue.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("tbl_blog_like")
@NoArgsConstructor
public class BlogLike {

    @TableId(value="id",type= IdType.AUTO)
    private Integer id;

    @TableField(value="blog_id")
    private Integer blogId;

    @TableField(value="author_id")
    private Integer authorId;

    @TableField(value="like_post_id")
    private Integer postId;

    @TableField(value="like_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date likeTime;

}
