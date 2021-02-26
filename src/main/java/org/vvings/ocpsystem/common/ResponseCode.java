package org.vvings.ocpsystem.common;

/**
 * @author vvings
 * @version 2020/3/20 18:15
 */
public enum ResponseCode {
    SUCCESS(200, "SUCCESS"),
    PROGRAMMER_ERROR(-1, "PROGRAMMER_ERROR"),
    ERROR(400, "ERROR"),
    NEED_LOGIN(401, "NEED_LOGIN"),
    LOGIN_FAIL(402,"LOGIN_FAIL"),
    REGISTER_FAIL(403,"REGISTER_FAIL"),
    NAME_SAME(404,"NAME_SAME"),
    UPDATE_FAIL(405,"UPDATE_FAIL"),
    USER_BANNED(406,"USER_BANNED"),
    CREATE_ORDER_FAIL(407,"CREATE_ORDER_FAIL"),
    FIRST_VERIFY_FAIL(408,"FIRST_VERIFY_FAIL"),
    SECOND_VERIFY_FAIL(409,"SECOND_VERIFY_FAIL"),
    UPLOAD_FILE_FAIL(410,"UPLOAD_FILE_FAIL"),
    ILLEGAL_ARGUMENT(300, "ILLEGAL_ARGUMENT"),
    RELOGIN(302,"RELOGIN"),
    NEED_ADD_CONTAINER(303, "NEED_ADD_CONTAINER"),
    PERMISSION_DENIED(411, "PERMISSION_DENIED");

    private final int code;

    private final String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
