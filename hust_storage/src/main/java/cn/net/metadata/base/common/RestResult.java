package cn.net.metadata.base.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * Rest接口返回状态常量
 * </p>
 *
 * @author SageZhang
 * @version 2017/5/17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "返回响应数据")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResult<T> implements Serializable {


    private static final long serialVersionUID = -2044772626801676962L;
    @ApiModelProperty("编号")
    private Integer code;
    @ApiModelProperty("信息")
    private String msg;
    @ApiModelProperty("返回的数据")
    private T data;


}