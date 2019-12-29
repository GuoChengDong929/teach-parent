package com.teach.entity.vo;

import com.teach.entity.access.Role;
import lombok.*;


import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserStudentVo implements Serializable {

    private String id;

    private String username;

    private String password;

    private String nickName;

    private String level; //saasAdmin：saas管理员具备所有权限  coAdmin：企业管理（创建租户企业的时候添加）user：普通用户（需要分配角色）

    private String telephone;

    private String email;

    private String sex;

    private String residenceAddress; //户籍所在地

    private String nowAddress; //现居住址
    private String type;    //老师1,学生2
    private String idCard;
    private Date birthday;
    private String education;   //学历
    private String graduationSchool; //毕业院校
    private String major;		//专业
    private String emergencyContact; //紧急联络人
    private String relation; //紧急联络人关系
    private String status;  //是否禁用该账号

    private String roleIds; //在页面新增,修改用户时,传递过来的角色ids字符串,用于绑定关系

    private Set<Role> roles = new HashSet<Role>();//用户与角色   多对多


    private String studyType;   //状态:1在读 2休学 3退学 4毕业

    private Date stopStudyTime;   //休学时间

    private Integer stopStudyLength;    //休学时长

    private String stopStudyReason;   //休学原因

    private Date backStudyTime;    //退学时间

    private String backStudyReason; //退学原因

    private Date finishStudyTime;//毕业时间

    private String classesId;

    private String classesName;

}
