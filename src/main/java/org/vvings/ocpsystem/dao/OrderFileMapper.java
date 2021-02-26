package org.vvings.ocpsystem.dao;

import org.vvings.ocpsystem.pojo.OrderFile;

import java.util.List;

public interface OrderFileMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderFile record);

    int insertSelective(OrderFile record);

    OrderFile selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderFile record);

    int updateByPrimaryKey(OrderFile record);

    List<String> selectPathByOrderId(Integer orderId);
}