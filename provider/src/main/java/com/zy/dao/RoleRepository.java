package com.zy.dao;

import com.zy.entity.Role;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RoleRepository extends BaseRepository<Role,Integer> {
    List<Role> findAllByIdIn(List<Integer> id);
    //删除某个用户
    @Transactional
    void deleteById(int roleId);
}
