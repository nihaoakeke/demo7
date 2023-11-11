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
@TableName("tbl_comment")
@NoArgsConstructor
public class Comment {

    @TableId(value="comment_id",type = IdType.AUTO)
    private Integer cId;

    @TableField(value="blog_id")
    private Integer blogId;

    @TableField(value="user_id")
    private Integer uId;

    @TableField(value="comment_like")
    private Integer cLike;

    @TableField(value="comment_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date ctime;

    @TableField(value="comment_content")
    private String cContent;

    @TableField(value="parent_id")
    private Integer pId;

    @TableField(value="parent_user_id")
    private Integer pUid;

    @TableField(exist = false)
    private List<Integer> deleteid;

}
