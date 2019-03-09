package com.zy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zy.entity.Role;
import com.zy.service.RoleService;
import com.zy.utils.TableResult;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/main")
public class RoleController {
    @Reference(version = "1.2")
    private RoleService roleService;

    @RequestMapping("/toRoleList")
    public String toRoleList(){
        System.out.println("进入roleList");
        return "roleList";
    }

    @RequestMapping("/getRolePage")
    @ResponseBody
    public Object getRolePage(int cp,int ps){
        Page<Role> roles = roleService.roleList(cp,ps);
        return new TableResult<List<Role>>(cp,((Page) roles).getTotalElements(),roles.getContent());
    }

    @RequestMapping(value = "/addRole",method = RequestMethod.POST)
    @ResponseBody
    public Object addRole(Role role, @RequestParam(name="status",required = false)List<Integer> menus){
        System.out.println("进入到addRole的Controller");
        try{
            if(menus==null){
                //仅仅添加角色不分配权限
                roleService.addRole(role,true,null);
            }else {
                //分配权限 需要创建role与角色之间的关系
                System.out.println("进入分配权限的添加界面了");
                int id=role.getId();
                Role role1 = roleService.getRole(id);
                roleService.addRole(role1,false,menus);
//                roleService.addRole(roleService.getRole(role.getId()),false,menus);
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    //删除某个角色
    @RequestMapping(value = "/removeRole")
    @ResponseBody
    public Object removeRole(int id){
        System.out.println("进入了删除角色controller");
        try{
            System.out.println(id);
            roleService.removeRole(id);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
