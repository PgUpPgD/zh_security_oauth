package com.zh.client.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zh.client.entity.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends BaseMapper<UserEntity> {
}
