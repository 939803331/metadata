package cn.net.metadata.base.common;

/**
 * <p></p>
 *
 * @author SageZhang
 * @version 2018/10/26
 */
public class RestResultGenerator {

    /**
     * 生成返回结果
     *
     * @param code 返回编码
     * @param message 返回消息
     * @param data 返回数据
     * @param <T> 返回数据类型
     * @return 返回结果
     */
    public static <T> RestResult<T> generate(final int code, final String message, T data) {
        return new RestResult<>(code, message, data);
    }

    /**
     * 操作成功响应结果， 默认结果
     *
     * @return 操作成功的默认响应结果
     */
    public static <T> RestResult<T> success() {
        return new RestResult<>(RestResultEnum.SUCCESS.getCode(), RestResultEnum.SUCCESS.getMessage(), null);
    }

    /**
     * 操作成功响应结果， 自定义数据及信息
     *
     * @param message 自定义信息
     * @param data 自定义数据
     * @param <T> 自定义数据类型
     * @return 响应结果
     */
    public static <T> RestResult<T> success(final String message, final T data) {
        return new RestResult<>(RestResultEnum.SUCCESS.getCode(), message, data);
    }

    /**
     * 操作成功响应结果，自定义数据，默认信息
     *
     * @param data 自定义数据
     * @param <T> 自定义数据类型
     * @return 响应结果
     */
    public static <T> RestResult<T> success(final T data) {
        return new RestResult<>(RestResultEnum.SUCCESS.getCode(), RestResultEnum.SUCCESS.getMessage(), data);
    }

    /**
     * 操作成功响应结果，自定义信息，无数据
     *
     * @param message 自定义信息
     * @return 响应结果
     */
    public static <T> RestResult<T> success4Message(final String message) {
        return new RestResult<>(RestResultEnum.SUCCESS.getCode(), message, null);
    }

    /**
     * 操作失败响应结果， 默认结果
     *
     * @return 操作成功的默认响应结果
     */
    public static <T> RestResult<T> failure() {
        return new RestResult<>(RestResultEnum.FAIL.getCode(), RestResultEnum.FAIL.getMessage(), null);
    }

    /**
     * 操作失败响应结果， 自定义错误编码及信息
     *
     * @param code 自定义错误编码
     * @param message 自定义信息
     * @return 响应结果
     */
    public static <T> RestResult<T> failure(final int code, final String message) {
        return new RestResult<>(code, message, null);
    }

    /**
     * 操作失败响应结果， 自定义错误编码及信息
     *
     * @param code 自定义错误编码
     * @param message 自定义信息
     * @return 响应结果
     */
    public static <T> RestResult<T> failure(final int code, final String message, T data) {
        return new RestResult<>(code, message, data);
    }

    /**
     * 操作失败响应结果，自定义错误编码
     *
     * @param baseResultEnum 自定义错误编码枚举
     * @return 响应结果
     */
    public static <T> RestResult<T> failure(final RestResultEnum baseResultEnum) {
        return new RestResult<>(baseResultEnum.getCode(), baseResultEnum.getMessage(), null);
    }

    /**
     * 操作失败响应结果，自定义信息
     *
     * @param message 自定义信息
     * @return 响应结果
     */
    public static <T> RestResult<T> failure(final String message) {
        return new RestResult<>(RestResultEnum.FAIL.getCode(), message, null);
    }

    /**
     * 异常响应结果， 默认结果
     *
     * @return 操作成功的默认响应结果
     */
    public static <T> RestResult<T> error() {
        return new RestResult<>(RestResultEnum.SERVER_ERROR.getCode(), RestResultEnum.SERVER_ERROR.getMessage(), null);
    }

    /**
     * 异常响应结果， 自定义错误编码及信息
     *
     * @param code 自定义错误编码
     * @param message 自定义信息
     * @return 响应结果
     */
    public static <T> RestResult<T> error(final int code, final String message) {
        return new RestResult<>(code, message, null);
    }

    /**
     * 异常响应结果，自定义错误编码
     *
     * @param baseResultEnum 自定义错误编码枚举
     * @return 响应结果
     */
    public static <T> RestResult<T> error(final RestResultEnum baseResultEnum) {
        return new RestResult<>(baseResultEnum.getCode(), baseResultEnum.getMessage(), null);
    }

    /**
     * 业务异常响应结果
     *
     * @param be 业务异常
     * @return 响应结果
     */
    public static <T> RestResult<T> error(final BusinessException be) {
        return new RestResult<>(RestResultEnum.SERVER_ERROR.getCode(), be.getErrorMessage(), null);
    }

    /**
     * 异常响应结果，自定义信息
     *
     * @param message 自定义信息
     * @return 响应结果
     */
    public static <T> RestResult<T> error(final String message) {
        return new RestResult<>(RestResultEnum.SERVER_ERROR.getCode(), message, null);
    }
}
