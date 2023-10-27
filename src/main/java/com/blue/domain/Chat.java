package com.blue.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@TableName("tbl_chat")
@NoArgsConstructor
public class Chat {

    @TableId(value="chatId",type= IdType.AUTO)
    private Integer chatId;

    @TableField(value="fromUser")
    private Integer fromUser;

    @TableField(value="toUser")
    private Integer toUser;

    @TableField(value="message")
    private String message;

    @TableField(value="flag")
    private Integer flag;

    @TableField(value="time")
    private Date time;
}
