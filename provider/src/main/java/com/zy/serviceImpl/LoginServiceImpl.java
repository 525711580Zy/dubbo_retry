package com.zy.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zy.dao.UserRepository;
import com.zy.entity.User;

import com.zy.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service(version = "1.0",interfaceClass = LoginService.class)
@Component
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
public class LoginServiceImpl implements LoginService {
    @Autowired
    private UserRepository userRepository;

    static final Lock lock = new ReentrantLock();//定义lock锁
    @Async //异步调用方法
    @Override
    public void unlock(String loginname) {
        lock.lock();
        userRepository.unlock(loginname);
        lock.unlock();
    }
    @Async
    @Override
    public void addUser(User user){
        lock.lock();
        userRepository.save(user);
        lock.unlock();
    }

    @Override
    public User findUserByName(String userName) {
        lock.lock();
        User user =userRepository.findUserByName(userName);
        lock.unlock();
        return user;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
