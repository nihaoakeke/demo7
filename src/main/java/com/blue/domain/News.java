package com.blue.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("tbl_news")
@NoArgsConstructor
public class News {

    @TableId(value="nId",type = IdType.AUTO)
    private Integer nId;

    @TableField(value="nContent")
    private  String nContent;

    @TableField(value="uId")
    private Integer uId;

    @TableField(value="nTime")
    private Date nTime;

}
