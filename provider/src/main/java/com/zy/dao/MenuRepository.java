package com.zy.dao;

import com.zy.entity.Menu;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends BaseRepository<Menu,Integer> {
    //通过权限id集合获取menus对象集合
    List<Menu> findAllByIdIn(List<Integer> menus);

    //根据角色获取权限,联合查询 返回Objuce[],只检索id和name
    @Query(nativeQuery=true,value="select sm.id,sm.name from sys_menu as sm,sys_role_menu as srm where sm.id=srm.menu_id and srm.role_id=?1")
    Object[] getMenuByRole(int roleid);
}
