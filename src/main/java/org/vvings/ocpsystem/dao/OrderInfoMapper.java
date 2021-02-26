package org.vvings.ocpsystem.dao;

import org.vvings.ocpsystem.pojo.OrderInfo;

import java.math.BigDecimal;
import java.util.List;

public interface OrderInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderInfo record);

    int insertSelective(OrderInfo record);

    OrderInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderInfo record);

    int updateByPrimaryKey(OrderInfo record);

    List<OrderInfo> selectAllBystatus(Integer i);

    OrderInfo selectByOrderId(Integer id);

    List<OrderInfo> searchOrder(Integer orderId, Integer status, Integer id, String name, Integer startyear, Integer endyear, Integer startMonth, Integer endMonth, Integer startxun, Integer endxun,BigDecimal startAmount, BigDecimal endAmount);

    List<OrderInfo> selectById(Integer id);
}