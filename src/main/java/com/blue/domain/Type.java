package com.blue.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("tbl_type")
@AllArgsConstructor
@NoArgsConstructor
public class Type {


    @TableId(value="type_id",type = IdType.AUTO)
    private Integer typeId;

    @TableField(value="type_name")
    private String typeName;

}
