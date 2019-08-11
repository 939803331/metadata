package cn.net.metadata.base.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p></p>
 *
 * @author SageZhang
 * @version 2018/10/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 5311776766915996016L;
    /**
     * 异常代码
     */
    private int errorCode = RestResultEnum.SERVER_ERROR.getCode();
    /**
     * 异常信息
     */
    private String errorMessage;

    public BusinessException(String errorMessage) {
        this.errorCode = RestResultEnum.SERVER_ERROR.getCode();
        this.errorMessage = errorMessage;
    }

    public BusinessException(String errorMessage, Throwable e) {
        super(errorMessage, e);
        this.errorCode = RestResultEnum.SERVER_ERROR.getCode();
    }

    public BusinessException(int errorCode, String errorMessage, Throwable e) {
        super(errorMessage, e);
        this.errorCode = errorCode;
    }

}
