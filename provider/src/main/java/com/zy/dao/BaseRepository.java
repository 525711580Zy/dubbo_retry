package com.zy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

/*@NoRepositoryBean spring-data-jpa不会在启动的时候实例化BaseRepository
PagingAndSortingRepository 这接口封装了所有的分页方法
JpaRepository 此接口封装了增删改查方法*/
@NoRepositoryBean
public interface BaseRepository<T,I extends Serializable> extends PagingAndSortingRepository<T,I>,
        JpaRepository<T,I> {
}
