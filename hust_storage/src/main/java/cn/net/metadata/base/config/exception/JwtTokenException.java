package cn.net.metadata.base.config.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xiaopo
 * @package cn.net.metadata.novelty.config.exception
 * @date 2018/7/25 11:58
 */
@Getter
@Setter
public class JwtTokenException extends RuntimeException {
	private static final long serialVersionUID = 9064218839820394235L;
	
	/**
	 * 默认0是通用异常
	 */
	private Integer code = 0;
	
	public JwtTokenException(String message) {
		super(message);
	}
	
	public JwtTokenException(int code, String message) {
		super(message);
		this.code = code;
	}
}
