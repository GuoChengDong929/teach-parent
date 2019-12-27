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
    INSERT_STUDENT_ERROR_IS_CLASSES_HAS_EXAMS_ING(false,40002,"班级有未结束的试卷,暂时无法添加学生"),


    DEPARTMENT_HAS_USERS(false,30001,"部门存在员工,无法删除"),


    EXAM_TIME_LT_NEW_DATE(false,60001,"考试时间应该大于当前时间至少10分钟"),
    EXAM_MARKING_TEACHER_COMMENT(false,60002,"问答题的评语不能为空"),
    EXAM_MARKING_ASK_SCORE(false,60003,"问答题的评分不能为负数"),
    EXAM_MARKING_ASK_SCORE_MAX(false,60004,"评分分数过大"),
    EXAM_IS_COMMIT(false,60005,"试卷已提交,暂时无法查看"),
    EXAM_NO_START(false,60006,"考试未开始,你想做什么?"),
    EXAM_DAY_IS_ALWAYS(false,60007,"当前选择的日期已有日测试卷"),
    EXAM_WEEK_IS_ALWAYS(false,60008,"当前选择的日期已有周测试卷"),
    EXAM_MONTH_IS_ALWAYS(false,60009,"当前选择的日期已有月考试卷"),

    // 试题 question
    SINGLE_SIZE_OUTPUT(false,70001,"所选单选题的数量大于题库总数量"),
    SINGLE_IS_ZERO(false,70002,"所选章节中的单选题库没有任何试题"),
    SELECTION_SIZE_OUTPUT(false,70003,"所选多选题的数量大于题库总数量"),
    SELECTION_IS_ZERO(false,70004,"所选章节中的多选题库没有任何试题"),
    ASK_SIZE_OUTPUT(false,70005,"所选问答题的数量大于题库总数量"),
    EXECL_DATA_IS_NULL(false,70006,"没有任何数据可供导出"),
    ASK_IS_ZERO(false,70007,"所选章节中的问答题库没有任何试题"),
    UPPER_IS_ZERO(false,70008,"所选章节中的上机题库没有任何试题"),
    OK_IS_ZERO(false,70009,"所选章节中的已答试题为空");

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
