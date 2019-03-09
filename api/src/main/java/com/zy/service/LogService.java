package com.zy.service;

import com.zy.entity.Log;
import org.springframework.data.domain.Page;

import java.util.Date;

public interface LogService {
    //插入日志
    void addLog(Log logInfo);
    //定时删除日志
    void delLog(int logId);
    //分页的方法
    Page<Log> LogList(String userName, Date begin, Date end, int pageindex, int pagesize);
}
