package com.zy.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zy.dao.MenuRepository;
import com.zy.dao.RoleRepository;
import com.zy.entity.Menu;
import com.zy.entity.Role;
import com.zy.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(version = "1.2",interfaceClass = RoleService.class)
@Component
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> getALL() {
        return roleRepository.findAll();
    }

    @Override
    public Page<Role> roleList(int pageindex, int pagesize) {
        return roleRepository.findAll(getPageRequest(pageindex,pagesize));
    }

    @Override
    public void addRole(Role role, boolean isMenu, List<Integer> menus) {
        if(isMenu){
            //无权限分配
            roleRepository.save(role);
        }else {
            //存在权限分配
            List<Menu> allById = menuRepository.findAllByIdIn(menus);
            role.setMenus(allById);
            roleRepository.save(role);
        }
    }

    public PageRequest getPageRequest(int pageindex,int pagesize){
        //偏移量=（页数-1）*3
        int page = pageindex/3+1;
        //spring Boot 2.x以上的写法
        return new PageRequest(page-1,pagesize,Sort.Direction.DESC,"id");
    }
    public Role getRole(int roleId){
        Role one = roleRepository.findOne(roleId);
        return one;
    }

    @Override
    public void removeRole(int roleId) {
        roleRepository.deleteById(roleId);
    }
}
