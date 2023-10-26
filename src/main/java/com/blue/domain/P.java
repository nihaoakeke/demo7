package com.blue.domain;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("tbl_p")
@NoArgsConstructor
public class P {

    @TableId(value = "pid")
    private int pid;

    @TableField(value="pName")
    private String pName;
}
