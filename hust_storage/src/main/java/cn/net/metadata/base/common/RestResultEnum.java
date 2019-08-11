package cn.net.metadata.base.common;

import lombok.Getter;

/**
 * <p></p>
 *
 * @author SageZhang
 * @version 2018/10/26
 */
@Getter
public enum RestResultEnum {
    /**
     * <h4>错误码</h4>
     * 0:正常(SUCCESS)
     * 1:格式错误 (FORMAT_INVALID)
     * 2:数据不存在 (DATA_NOT_FOUND)
     * 3:数据已存在 (DATA_EXISTED)
     * 4:数据无效 (DATA_INVALID)
     * 5:登录错误 (LOGIN_REQUIRED)
     * 6:权限不足 (PERMISSION_DENIED)
     * 7:登录超时 (LOGIN_TIMEOUT)
     */
    FAIL(400, "请求失败"),
    SUCCESS(200, "正常"),
    FORMAT_INVALID(406, "格式错误"),
    DATA_NOT_FOUND(404, "数据不存在"),
    DATA_EXISTED(409, "数据已存在"),
    DATA_INVALID(409, "数据错误"),
    PERMISSION_FORBIDDEN(403, "禁止访问"),
    PERMISSION_DENIED(401, "权限不足"),
    LOGIN_TIMEOUT(401, "登录超时"),
    SERVER_ERROR(500, "网络服务异常");


    private int code;
    private String message;

    RestResultEnum(int code, String message) {
        this.code = code;
    }

}
