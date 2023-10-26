package com.blue.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("tbl_user")
@NoArgsConstructor
public class User {

    @TableId(value="uId",type = IdType.AUTO)
    private Integer uId;

    @TableField(value="uName")
    private String uName;

    @TableField(value="uPassword")
    private String uPassword;


    @TableField(value="uFollowers")
    private Integer uFollowers;

    @TableField(value="uAge")
    private Integer uAge;

    @TableField(value="uEmail")
    private String uEmail;

    @TableField(value="uNickname")
    private String uNickname;

    @TableField(value="uPhone")
    private String uPhone;

    @TableField(value="uPicture")
    private String uPicture;

    @TableField(value = "uFlag")
    private Integer uFlag;


}
