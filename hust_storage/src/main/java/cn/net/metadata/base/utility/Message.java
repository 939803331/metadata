package cn.net.metadata.base.utility;

import com.alibaba.fastjson.JSONObject;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Created by xiaopo on 15/12/11.
 */
@Data
@Builder
@Accessors(chain = true)
public class Message implements Serializable {

    private static final long serialVersionUID = 5207178535135769475L;

    /**
     * 0 失败 / 1 成功
     */
    @Builder.Default
    private Integer code = 0;

    @Builder.Default
    private String message = "操作成功";

    @Builder.Default
    private Integer total;

    @Builder.Default
    private Object data = new JSONObject();


    //*************************** utils ****************************
    public static Message successResponse() {
        return Message.builder().build();
    }

    public static Message successResponse(Object data) {
        return Message.builder().data(data).build();
    }

    public static Message failedResponse() {
        return Message.builder().code(0).message("操作失败").build();
    }

    public static Message failedResponse(Object data) {
        return Message.builder().code(0).message("操作失败").data(data).build();
    }
}
