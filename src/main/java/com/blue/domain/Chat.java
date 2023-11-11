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
@TableName("tbl_user_chat")
@NoArgsConstructor
public class Chat {

    @TableId(value="chat_id",type= IdType.AUTO)
    private Integer chatId;

    @TableField(value="from_user")
    private Integer fromUser;

    @TableField(value="to_user")
    private Integer toUser;

    @TableField(value="chat_message")
    private String chatMessage;

    @TableField(value="chat_flag")
    private Integer chatFlag;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value="chat_time")
    private Date chatTime;

    @TableField(exist = false)
    private List<Integer> deleteid;
}
