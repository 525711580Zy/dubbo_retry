package com.zy.controller;

import com.zy.entity.Log;
import com.zy.service.LogService;
import com.zy.utils.TableResult;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/log")
public class LogController {

    @Reference(version="1.5")
    private LogService service;
    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception{
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        CustomDateEditor editor = new CustomDateEditor(df,true);
        binder.registerCustomEditor(Date.class,editor);
    }
    @RequestMapping("/toLogList")
    public String getLogHtml(){
        System.out.println("进入到了LogList");
        return "logList";
    }
    @RequestMapping("/getLogDetail")
    @ResponseBody
    public Object getLogDetail(int cp, int ps,
                               @RequestParam(value = "userName",required = false)String userName,
                               @RequestParam(name = "begin",required = false)Date begin,
                               @RequestParam(name="end",required = false)Date end){
        //动态sql查询：根据操作人姓名，开始时间和结束日期检索信息
        Page<Log> loginfo = service.LogList(userName,begin,end,cp,ps);
        TableResult<List<Log>> tr = new TableResult<>(cp,loginfo.getTotalElements(),loginfo.getContent());
        return tr;
    }
}
