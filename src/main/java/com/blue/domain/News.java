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
@TableName("tbl_news")
@NoArgsConstructor
@AllArgsConstructor
public class News {

    @TableId(value="news_id",type = IdType.AUTO)
    private Integer newsId;

    @TableField(value="news_content")
    private  String newsContent;

    @TableField(value="user_id")
    private Integer uId;

    @TableField(value="news_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date newsTime;

}
