package com.teach.response;

/**
 * 公共的返回码
 *    返回码code：
 *      成功：10000
 *      失败：10001
 *      未登录：10002
 *      未授权：10003
 *      抛出异常：99999
 */
public enum ResultCode {

    SUCCESS(true,10000,"操作成功！"),

    FAIL(false,10001,"操作失败"),
    UNAUTHENTICATED(false,10002,"您还未登录"),

    UNAUTHORISE(false,10003,"权限不足"),
    LOGIN_TIME_OUT(false,99998,"已超时,请重新登录"),
    SERVER_ERROR(false,99999,"抱歉，系统繁忙，请稍后重试！"),

    MOBILEORPASSWORDERROR(false,20001,"用户名或密码错误"),


    //权限 ------------------
    PERMISSION_IN_ROLE_HAS_DATA(false,11111,"无法删除权限,因为角色已经绑定了该权限"),


    //课程------------------
    CURRICULUM_IS_NOT_EXIST(false,30000,"所选阶段查无课程"),
    CURRICULUM_HAS_CHAPTER(false,30001,"课程中存在章节"),

    //学生 -----------------
    DO_NOT_INVALID_CLASSES(false,40001,"不能作废班级,有其他业务进行关联"),



    DEPARTMENT_HAS_USERS(false,30001,"部门存在员工,无法删除"),


    EXAM_TIME_LT_NEW_DATE(false,60001,"考试时间应该大于当前时间至少10分钟"),
    EXAM_MARKING_TEACHER_COMMENT(false,60002,"问答题的评语不能为空"),
    EXAM_MARKING_ASK_SCORE(false,60003,"问答题的评分不能为负数"),
    EXAM_MARKING_ASK_SCORE_MAX(false,60003,"评分分数过大"),
    EXAM_IS_COMMIT(false,60004,"试卷已提交,暂时无法查看"),
    EXAM_NO_START(false,60005,"考试未开始,你想做什么?"),


    // 试题 question
    SINGLE_SIZE_OUTPUT(false,70001,"所选单选题的数量大于题库总数量"),
    SELECTION_SIZE_OUTPUT(false,70002,"所选多选题的数量大于题库总数量"),
    ASK_SIZE_OUTPUT(false,70003,"所选问答题的数量大于题库总数量"),
    EXECL_DATA_IS_NULL(false,7004,"没有任何数据可供导出");

    //---权限操作返回码----
    //---其他操作返回码----

    //操作是否成功
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;

    ResultCode(boolean success,int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public boolean success() {
        return success;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

}
