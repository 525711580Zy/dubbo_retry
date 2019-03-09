package com.zy.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="sys_log")
public class Log implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int logid;
    private String title;
    private Date modify;
    private String action;
    private String username;

    public Log() {
    }

    public Log(String title, Date modify, String action, String username) {
        this.title = title;
        this.modify = modify;
        this.action = action;
        this.username = username;
    }

    public int getLogid() {
        return logid;
    }

    public void setLogid(int logid) {
        this.logid = logid;
    }

    public Date getModify() {
        return modify;
    }

    public void setModify(Date modify) {
        this.modify = modify;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
