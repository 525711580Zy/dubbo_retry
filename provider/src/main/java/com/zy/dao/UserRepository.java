package com.zy.dao;

import com.zy.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface UserRepository extends BaseRepository<User,Integer> {
    //save方法自动存在无需手动弄出来，这格式方法是用来更改注册后的标识状态0的
    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update sys_user set isactive=1 where login_name=?1")
    void unlock(String loginName);

    public User findUserByName(String userName);

    //删除某个用户
    @Transactional
    public void deleteById(int userId);
}
