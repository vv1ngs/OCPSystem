package org.vvings.ocpsystem.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.vvings.ocpsystem.common.ResponseCode;
import org.vvings.ocpsystem.common.ServerResponse;
import org.vvings.ocpsystem.dao.*;
import org.vvings.ocpsystem.pojo.*;
import org.vvings.ocpsystem.service.orderService;
import org.vvings.ocpsystem.util.DateTimeUtil;
import org.vvings.ocpsystem.vo.OrderVo;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Author vvings
 * @Date 2020/12/22 16:32
 * @Version 1.0
 */
@Service
class OrderServiceImpl implements orderService {
    @Value("${sftp.prefix}")
    private String pathPrefix;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private DealerInfoMapper dealerInfoMapper;
    @Autowired
    private OrderProductMapper orderProductMapper;
    @Autowired
    private ProductInfoMapper productInfoMapper;
    @Autowired
    private OrderFileMapper orderFileMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public ServerResponse<List<OrderVo>> getAllFirstOrder(Integer status) {
       List<OrderInfo> list= orderInfoMapper.selectAllBystatus(status);
       List<OrderVo> orderVoList=new ArrayList<>();
       list.forEach(i->orderVoList.add(assembleOrderVo(i)));
       return ServerResponse.createBySuccess(orderVoList);
    }

    @Override
    public ServerResponse<String> verifyOrder(Integer orderId,Integer status,String reason,String username) {

        OrderInfo orderInfo=orderInfoMapper.selectByOrderId(orderId);
        if (orderInfo!=null){
            if (status==3) {
                orderInfo.setStatus(status);
                orderInfo.setFirstVerifyName(username);
                orderInfo.setFirstVerifyTime(new Date());
            }
            else if (status==6){
                orderInfo.setStatus(status);
                orderInfo.setReviewVerifyName(username);
                orderInfo.setReviewVerifyTime(new Date());
            }
            else if ((status==5 || status==9) && !StringUtils.isBlank(reason)) {
                orderInfo.setStatus(status);
                if (orderInfo.getFirstVerifyName()==null){
                    orderInfo.setFirstVerifyName(username);
                    orderInfo.setFirstVerifyTime(new Date());
                }
                else {
                    orderInfo.setReviewVerifyName(username);
                    orderInfo.setReviewVerifyTime(new Date());
                }
            }
            else
                return ServerResponse.createByErrorCode(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        if (orderInfoMapper.updateByPrimaryKeySelective(orderInfo)==1){
            return ServerResponse.createBySuccess("更新成功");
        }
        return ServerResponse.createByErrorCode(ResponseCode.FIRST_VERIFY_FAIL.getCode(), ResponseCode.FIRST_VERIFY_FAIL.getDesc());
    }

    @Override
    public ServerResponse<List<String>> getFile(Integer orderId) {
        List<String> list=orderFileMapper.selectPathByOrderId(orderId);
        List<String> strings=new ArrayList<>();
        list.stream().forEach(i -> strings.add(pathPrefix+i));
        return ServerResponse.createBySuccess(strings);
    }

    @Override
    public ServerResponse<Set<OrderVo>> searchOrder(String name, Integer categoryId, String dealerId, Integer orderId, Integer status, Integer startyear, Integer endyear, Integer startMonth, Integer endMonth, Integer startxun, Integer endxun, BigDecimal startAmount, BigDecimal endAmount) {
        Set<OrderVo> orderVoSet=new HashSet<>();
        Set<OrderInfo>orderInfos=new HashSet<>();
        List<DealerInfo> dealerInfos=new ArrayList<>();
        Category category=new Category();
        if (categoryId!=null) {
            category = categoryMapper.selectByPrimaryKey(categoryId);
        }

        String  categoryName =category.getName();
        Set<DealerInfo> set = new HashSet<>();
        if (categoryId !=null||StringUtils.isNotBlank(name)) {

            dealerInfos = dealerInfoMapper.searchDealerByKeyWord(name, dealerId);
            set.addAll(dealerInfos);
            dealerInfos = dealerInfoMapper.searchDealerByPingYingFirst(name, dealerId);
            set.addAll(dealerInfos);
            dealerInfos = dealerInfoMapper.searchDealerByPingYing(name, dealerId);
            set.addAll(dealerInfos);
        }
        if (set.size()==0){
            orderInfos.addAll(orderInfoMapper.searchOrder(orderId,status,null, categoryName,startyear,endyear,startMonth,endMonth,startxun,endxun,startAmount,endAmount));
        }

        set.forEach(i -> orderInfos.addAll(orderInfoMapper.searchOrder(orderId,status,i.getId(),categoryName,startyear,endyear,startMonth,endMonth,startxun,endxun,startAmount,endAmount)));

        orderInfos.forEach(orderInfo -> orderVoSet.add(assembleOrderVo(orderInfo)));
        return ServerResponse.createBySuccess(orderVoSet);
    }

    @Override
    public ServerResponse<OrderVo> getAllOrderByID(Integer orderID) {
        OrderInfo o= orderInfoMapper.selectByPrimaryKey(orderID);
        OrderVo orderVoList=assembleOrderVo(o);
        return ServerResponse.createBySuccess(orderVoList);
    }


    private OrderVo assembleOrderVo(OrderInfo orderInfo){
        OrderVo orderVo=new OrderVo();
        orderVo.setArea(orderInfo.getArea());
        orderVo.setOrderId(orderInfo.getOrderId());
        orderVo.setId(orderInfo.getId());
        orderVo.setStatus(orderInfo.getStatus());
        orderVo.setTotalAmount(orderInfo.getTotalAmount());
        orderVo.setCreateTime(DateTimeUtil.DateToString(orderInfo.getCreateTime()));
        orderVo.setTotalCount(orderInfo.getTotalCount());
        orderVo.setTotalVolume(orderInfo.getTotalVolume());
        orderVo.setRejectReason(orderInfo.getRejectReason());
        if (orderInfo.getFirstVerifyName()!=null){
            orderVo.setFirstVerifier(orderInfo.getFirstVerifyName());
        }
        if (orderInfo.getReviewVerifyName()!=null){
            orderVo.setLastVerifier(orderInfo.getReviewVerifyName());
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(orderInfo.getGetProductYear());
        stringBuilder.append("/");
        stringBuilder.append(orderInfo.getGetProductMonth());
        stringBuilder.append("/第");
        stringBuilder.append(orderInfo.getGetProductMonthDetailed());
        stringBuilder.append("周");
        orderVo.setDataTime(stringBuilder.toString());
        DealerInfo dealerInfo=dealerInfoMapper.selectByPrimaryKey(orderInfo.getDealerId());
        orderVo.setDealerName(dealerInfo.getName());
        orderVo.setDealerId(dealerInfo.getUuid());
        List<OrderProduct> list=orderProductMapper.selectByOrderId(orderInfo.getId());
        list.forEach(i -> i.setProductInfo(productInfoMapper.selectByPrimaryKey(i.getProductId())));
        orderVo.setList(list);
        return orderVo;
    }
}
