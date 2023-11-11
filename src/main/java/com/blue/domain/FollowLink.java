package com.blue.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("tbl_user_follow")
@NoArgsConstructor
public class FollowLink {

    @TableId(value="id",type= IdType.AUTO)
    private Integer id;

    @TableField(value="from_user")
    private Integer fromUser;

    @TableField(value="to_user")
    private Integer toUser;


    @TableField(value="follow_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date followTime;

    @TableField(value="follow_flag")
    private Integer followFlag;


}
