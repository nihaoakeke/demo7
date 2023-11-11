package com.blue.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("tbl_user")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @TableId(value="user_id",type = IdType.AUTO)
    private Integer uId;

    @TableField(value="user_name")
    private String uname;

    @TableField(value="user_password")
    private String upassword;


    @TableField(value="user_followers")
    private Integer uFollowers;

    @TableField(value="user_age")
    private Integer uAge;

    @TableField(value="user_email")
    private String uEmail;

    @TableField(value="user_nickname")
    private String uNickname;

    @TableField(value="user_phone")
    private String uPhone;

    @TableField(value="user_picture")
    private String uPicture;

    @TableField(value = "user_flag")
    private Integer uFlag;

    @TableField(value="user_sex")
    private Integer uSex;


    @TableField(value="user_lastime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date ulastTime;

    @TableField(value="user_registime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date uregisTime;

}
