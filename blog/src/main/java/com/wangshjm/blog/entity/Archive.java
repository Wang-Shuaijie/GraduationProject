package com.wangshjm.blog.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;

@Data
@NoArgsConstructor
@Table(name = "tb_archive")
public class Archive {
    private Long id;

    //归档日期
    private String archiveName;
}
