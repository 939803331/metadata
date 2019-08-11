package cn.net.metadata.base.service;

import cn.net.metadata.base.model.BaseModel;
import cn.net.metadata.base.service.core.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

/**
 * @author xiaopo
 * @package cn.net.metadata.novelty.service.core
 * @date 2018/7/20 13:19
 */
public abstract class BaseService {
	
	@Autowired
	protected UserService userService;
	
	@Autowired
	protected EntityManager entityManager;
	
	/**
	 * 统一进行逻辑删除操作
	 *  @param baseModel
	 *
	 */
	protected void deleteByFlag(BaseModel baseModel) {
		baseModel.setDelFlag(1);
		entityManager.merge(baseModel);
	}

	public void deleteInDB(BaseModel baseModel) {
		entityManager.remove(baseModel);
	}
}
