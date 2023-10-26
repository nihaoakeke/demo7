package com.blue.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("article")
@NoArgsConstructor
public class Article {
    @TableId(value="id",type = IdType.AUTO)
    private Integer id;

    @TableField(value="viewNum")
    private Integer viewNum;

    @TableField(value="commentNum")
    private Integer commentNum;

    @TableField(value="likeNum")
    private Integer likeNum;


}
