package com.blue.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("tbl_link")
@NoArgsConstructor
public class Link {

    @TableId(value="linkId",type= IdType.AUTO)
    private Integer linkId;

    @TableField(value="fromUser")
    private Integer fromUser;

    @TableField(value="toUser")
    private Integer toUser;

    @TableField(value="message")
    private String message;

    @TableField(value="time")
    private Date time;

    @TableField(value="flag")
    private Integer flag;

    @TableField(value="follow")
    private Integer follow;

    @TableField(value="unfriend")
    private Integer unfriend;

}
