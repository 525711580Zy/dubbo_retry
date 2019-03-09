package com.zy.dao;

import com.zy.entity.Log;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

//实现动态查询
public interface LogRepository extends BaseRepository<Log,Integer>,JpaSpecificationExecutor {
    void deleteByLogid(int id);
}
