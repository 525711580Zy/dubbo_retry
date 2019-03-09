package com.zy.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sys_role")
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String role;
    private String name;
    private String description;

    //@JsonIgnore
    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.REFRESH},fetch = FetchType.EAGER)
    @JoinTable(name="sys_user_role",joinColumns = {@JoinColumn(name="roleId")},inverseJoinColumns = {@JoinColumn(name="userId")})
    private transient List<User> user=new ArrayList<>();

    //@JsonIgnore
    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.REFRESH},fetch =FetchType.EAGER)
    @JoinTable(name="sys_role_menu",joinColumns = {@JoinColumn(name="roleId")},inverseJoinColumns = {@JoinColumn(name="menuId")})
    private  List<Menu> menus = new ArrayList<>();


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

}
