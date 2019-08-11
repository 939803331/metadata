package cn.net.metadata.base.config.advice;

import cn.net.metadata.base.config.converter.DateTimeEditor;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyEditorSupport;
import java.util.Date;

/**
 * @author admin
 * @package cn.net.metadata.mgather.configuration
 * @date 2018/4/3 18:04
 */
@ControllerAdvice
@RestControllerAdvice
public class InjectControllerAdvice {
	
	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		//对于需要转换为Date类型的属性，使用DateEditor进行处理
		binder.registerCustomEditor(Date.class, new DateTimeEditor());
		// 防止xss攻击
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			@Override
			public String getAsText() {
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
			
			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
			}
		});
	}
}
