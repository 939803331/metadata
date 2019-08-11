package com.ph.securitytest.mapper;

import com.ph.securitytest.entity.Permission;
import com.ph.securitytest.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description:
 *
 * @Author: PiHao
 * Date: Created in 2019-08-10
 * Time: 10:09
 */
public interface UserMapper {
	List<User> findByUserName(@Param("userName") String userName);
}
