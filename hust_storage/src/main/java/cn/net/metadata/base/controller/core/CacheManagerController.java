package cn.net.metadata.base.controller.core;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * @author xiaopo
 * @package cn.net.metadata.novelty.controller.core
 * @date 2018/8/20 14:32
 */
@RestController
@RequestMapping("cache")
@Api(value = "缓存管理")
public class CacheManagerController {
	
	@Autowired
	private CacheManager cacheManager;
	
	
	@GetMapping("cache")
	@PreAuthorize("hasRole('ADMIN')")
	@ApiOperation(value = "获取所有缓存名称")
	public Collection<String> list() {
		return cacheManager.getCacheNames();
	}
	
	@DeleteMapping("cache/{cacheName}")
	@PreAuthorize("hasRole('ADMIN')")
	@ApiOperation(value = "清理缓存", tags = "根据缓存名称清理对应缓存")
	public boolean cleanCache(@PathVariable String cacheName) {
		Cache cache = cacheManager.getCache(cacheName);
		if (cache == null)
			throw new RuntimeException(cacheName + "对应的缓存不存在。");
		cache.clear();
		return true;
	}
	
	@DeleteMapping("cache")
	@PreAuthorize("hasRole('ADMIN')")
	@ApiOperation(value = "清理所有缓存")
	public boolean cleanCache() {
		cacheManager.getCacheNames().forEach(s -> {
			Cache cache = cacheManager.getCache(s);
			if (cache != null)
				cache.clear();
		});
		return true;
	}
	
}
