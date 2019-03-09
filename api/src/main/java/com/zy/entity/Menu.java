package com.zy.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sys_menu")
public class Menu implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String url;
    private String icon;
    private int parent_id;

    //@JsonIgnore//在序列化和反序列化的时候忽略掉javabean中的某些属性，在这里就是忽略掉了role的属性，解决了无限嵌套
    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.REFRESH},fetch = FetchType.EAGER)
    @JoinTable(name="sys_role_menu",joinColumns = {@JoinColumn(name="menuId")},inverseJoinColumns = {@JoinColumn(name="roleId")})
    private transient List<Role> role = new ArrayList<>();
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public List<Role> getRole() {
        return role;
    }

    public void setRole(List<Role> role) {
        this.role = role;
    }
}
