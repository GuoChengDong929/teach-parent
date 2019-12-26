package com.teach.entity.access;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "pe_department")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department implements Serializable {


    @Id
    private String id;

    private String name; //部门名称

    private Date createTime; //创建时间

    private Date modifyTime;
    private String modifyUser;
    private String modifyId;
}
