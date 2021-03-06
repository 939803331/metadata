package com.ph.securitytest.mapper;

import com.ph.securitytest.entity.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description:
 *
 * @Author: PiHao
 * Date: Created in 2019-08-10
 * Time: 10:27
 */
public interface PermissionMapper {
	List<Permission> findByRoleId(@Param("roleId") Integer roleId);
}
