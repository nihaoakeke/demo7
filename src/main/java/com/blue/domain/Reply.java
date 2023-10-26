package com.blue.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("tbl_reply")
@NoArgsConstructor
public class Reply {

    @TableId(value="rId",type= IdType.AUTO)
    private Integer rId;

    @TableField(value="cId")
    private Integer cId;

    @TableField(value="uId")
    private Integer uId;

    @TableField(value="rTime")
    private Date rTime;

    @TableField(value="rContent")
    private String rContent;

    @TableField(value="rLikee")
    private Integer rLikee;

}
