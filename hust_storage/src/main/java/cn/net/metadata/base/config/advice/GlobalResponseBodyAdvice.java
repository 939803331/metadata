package cn.net.metadata.base.config.advice;

import cn.net.metadata.base.common.RestResult;
import cn.net.metadata.base.common.RestResultGenerator;
import cn.net.metadata.base.utility.Message;
import com.google.common.collect.Lists;
import java.io.File;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 配合GlobalExceptionHandler实现返回值统一处理
 *
 * @author xiaopo
 * @package cn.net.metadata.novelty.config
 * @date 2018/7/19 17:48
 */
@Slf4j
@Component
@RestControllerAdvice
public class GlobalResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private static List<String> exclude_urls = Lists.newArrayList("springfox", "swagger", "webjars");

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return MappingJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
        ServerHttpResponse response) {
        if (body == null) {
            return RestResultGenerator.success();
        }
        // 不同的返回值
        if (body instanceof RestResult || body instanceof File || exclude_urls.stream().anyMatch(s -> returnType.getGenericParameterType().getTypeName().contains(s))) {
            return body;
        } else {
            return RestResultGenerator.success(body);
        }
    }
}
