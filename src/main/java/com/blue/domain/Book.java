package com.blue.domain;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tbl_book")
public class Book {
    private Integer id;
    private String type;
    private String name;
    private String description;

}
