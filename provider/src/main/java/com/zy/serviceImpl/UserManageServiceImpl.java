package com.zy.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zy.dao.RoleRepository;
import com.zy.dao.UserRepository;
import com.zy.entity.Role;
import com.zy.entity.User;
import com.zy.service.UserManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service(version = "1.4",interfaceClass = UserManageService.class)
@Component
@Transactional
public class UserManageServiceImpl implements UserManageService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    private static Lock lock = new ReentrantLock();
    @Override
    public Page<User> getUserList(int pageindex, int pagesize) {
        PageRequest pageRequest = getPageRequest(pageindex,pagesize);
        return userRepository.findAll(pageRequest);
    }
    @Async //springBoot异步的调用方法
    @Override
    public void addUser(User user, boolean isRole, List<Integer> roleid) {
        lock.lock();
        if(!isRole){
            //当用户注册时，没有对应的角色分配
            userRepository.save(user);
        }else {
            //当管理员新增时，分配角色
            List<Role> byId = roleRepository.findAllByIdIn(roleid);
            //关联角色对象
            user.setRoles(byId);
           /* for (Role role : byId) {
                role.getUser().add(user);
            }*/
            userRepository.save(user);
        }
            lock.unlock();
    }

    @Override
    public void delUser(int userId) {
            userRepository.deleteById(userId);
    }

    public  PageRequest getPageRequest(int pageindex,int pagesize){
        //偏移量=（页数-1）*3
        int page=pageindex/3+1;
        return new PageRequest(page-1,pagesize, Sort.Direction.DESC,"id");
    }
}
