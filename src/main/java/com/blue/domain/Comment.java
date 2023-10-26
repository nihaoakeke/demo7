package com.blue.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("tbl_comment")
@NoArgsConstructor
public class Comment {

    @TableId(value="cId",type = IdType.AUTO)
    private Integer cId;

    @TableField(value="blogId")
    private Integer blogId;

    @TableField(value="uId")
    private Integer uId;

    @TableField(value="cLike")
    private Integer cLike;

    @TableField(value="cTime")
    private Date cTime;

    @TableField(value="cContent")
    private String cContent;


}
