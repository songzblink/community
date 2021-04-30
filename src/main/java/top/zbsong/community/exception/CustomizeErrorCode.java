package top.zbsong.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
    QUESTION_NOT_FOUND(2001, "你找的问题不在了，要不换个试试？"),
    TARGET_NOT_FOUND(2002, "未选中任何问题或评论进行回复！"),
    NO_LOGIN(2003, "当前操作需要登陆，请登陆后重试！"),
    SYS_ERROR(2004, "当前访问用户过多，请稍后重试。"),
    TYPE_PARAM_WRONG(2005, "评论类型错误或不存在！"),
    COMMENT_NOT_FOUND(2006,"回复的评论不存在！"),
    COMMENT_IS_EMPTY(2007,"回复的内容不能为空！"),
    READ_NOTIFICATION_FAIL(2008,"非法操作！操作的内容非本账号内容！"),
    NOTIFICATION_NOT_FOUND(2009,"消息不翼而飞了！");
    private String message;
    private Integer code;

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
