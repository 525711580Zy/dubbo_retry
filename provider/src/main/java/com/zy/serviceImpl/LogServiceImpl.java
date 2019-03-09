package com.zy.serviceImpl;

import com.zy.dao.LogRepository;
import com.zy.entity.Log;
import com.zy.service.LogService;
import com.zy.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service(version = "1.5",interfaceClass = LogService.class)
@Component
@Transactional
public class LogServiceImpl implements LogService {
    @Autowired
    private LogRepository repository;
    @Override
    public void addLog(Log logInfo) {
        repository.save(logInfo);
    }

    @Override
    public void delLog(int logId) {
        repository.deleteByLogid(logId);
    }

    @Override
    public Page<Log> LogList(String userName, Date begin, Date end, int pageindex, int pagesize) {
        //获取分页对象
        PageRequest pageRequest = this.getPageRequest(pageindex,pagesize);
        //使用Criteria实现动态查询
        Specification<Log> specification = new Specification<Log>() {
            @Override
            public Predicate toPredicate(Root<Log> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> pr = new ArrayList<>();
                if(userName!=null&&!userName.equals("")){
                    //按照操作人名模糊查询
                    pr.add(criteriaBuilder.like(root.get("username").as(String.class),"%"+userName+"%"));
                }
                if(begin!=null&&end!=null){
                    //按照操作时间查询
                    pr.add(criteriaBuilder.between(root.get("modify").as(Date.class),begin,end));
                }
                return criteriaBuilder.and(pr.toArray(new Predicate[pr.size()]));
            }
        };
        return repository.findAll(specification,pageRequest);
    }
    public PageRequest getPageRequest(int pageindex,int pagesize){
        //偏移量=(页数-1)*3
        int page = pageindex/pagesize+1;
        //spring boot2.x以上的写法
        return new PageRequest(page-1, pagesize,Sort.Direction.DESC,"logid");
    }
}


