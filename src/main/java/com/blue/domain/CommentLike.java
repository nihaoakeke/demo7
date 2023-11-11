package com.blue.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("tbl_comment_like")
@NoArgsConstructor
@AllArgsConstructor
public class CommentLike {

    @TableId(value="id",type= IdType.AUTO)
    private Integer id;

    @TableField(value="comment_id")
    private Integer commentId;

    @TableField(value="author_id")
    private Integer authorId;

    @TableField(value="like_post_id")
    private Integer postId;

    @TableField(value="like_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date likeTime;
}
