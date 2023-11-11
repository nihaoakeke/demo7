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
@TableName("tbl_user_collect")

@NoArgsConstructor
public class Collect {

    @TableId(value="collect_id",type= IdType.AUTO)
    private Integer collectId;

    @TableField(value="user_id")
    private Integer uId;

    @TableField(value="blog_id")
    private Integer blogId;

    @TableField(value="collect_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date collectTime;


    @TableField(exist = false)
    private List<Integer> deleteid;
}
