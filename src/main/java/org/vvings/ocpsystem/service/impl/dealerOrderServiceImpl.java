package org.vvings.ocpsystem.service.impl;

import ch.qos.logback.core.BasicStatusManager;
import lombok.extern.slf4j.Slf4j;
import net.schmizz.sshj.sftp.SFTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.web.multipart.MultipartFile;
import org.vvings.ocpsystem.common.ResponseCode;
import org.vvings.ocpsystem.common.ServerResponse;
import org.vvings.ocpsystem.dao.*;
import org.vvings.ocpsystem.pojo.*;
import org.vvings.ocpsystem.service.dealerOrderService;
import org.vvings.ocpsystem.service.dealerService;
import org.vvings.ocpsystem.util.DateTimeUtil;
import org.vvings.ocpsystem.util.SftpUtil;
import org.vvings.ocpsystem.util.UUIDUtil;
import org.vvings.ocpsystem.validator.ValidationResult;
import org.vvings.ocpsystem.validator.ValidatorImpl;
import org.vvings.ocpsystem.vo.OrderVo;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author vvings
 * @Date 2020/12/21 23:50
 * @Version 1.0
 */
@Slf4j
@Service
public class dealerOrderServiceImpl implements dealerOrderService {
    @Autowired
    private DealerInfoMapper dealerInfoMapper;
    @Autowired
    private ProductInfoMapper productInfoMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private OrderProductMapper orderProductMapper;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private ValidatorImpl validator;
    @Autowired
    private OrderFileMapper orderFileMapper;
    @Autowired
    private ManagerDealerInfoMapper managerDealerInfoMapper;
    @Autowired
    private DealerChildCategoryMapper dealerChildCategoryMapper;
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerResponse<String> addOrder(OrderInfo orderInfo, Integer[] productId, Integer[] counts,Integer cid,Integer status) {

        ValidationResult result=validator.validate(orderInfo);

        if (result.isHasErrors()){
            return ServerResponse.createByErrorCode(ResponseCode.CREATE_ORDER_FAIL.getCode(), result.getErrMsg());
        }

        List<ProductInfo> productInfoList=new ArrayList<>();
        if (Arrays.stream(productId).anyMatch(n -> {
            ProductInfo productInfo;
            if ((productInfo = productInfoMapper.selectByPrimaryKey(n)) == null) {
                return true;
            } else {
                productInfoList.add(productInfo);
                return false;
            }
        })){
            return ServerResponse.createByErrorCode(ResponseCode.CREATE_ORDER_FAIL.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        if (productInfoList.stream().anyMatch(productInfo -> {
            if (productInfo.getDealerId()!=orderInfo.getDealerId()){
                return true;
            }
            return false;
        })){
            return ServerResponse.createByErrorCode(ResponseCode.CREATE_ORDER_FAIL.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        int totalVolume=productInfoList.stream().mapToInt(ProductInfo::getProductVolume).sum();
        int totalCount= Arrays.stream(counts).mapToInt(s->s) .sum();
        BigDecimal totalAmount=new BigDecimal(0);
        for (int i=0;i<productInfoList.size();i++){
            BigDecimal test=productInfoList.get(i).getProductOpenFare().multiply(BigDecimal.valueOf(counts[i]));
            totalAmount=totalAmount.add(test);
        }
        if (totalVolume <5){
            return ServerResponse.createByErrorCode(ResponseCode.CREATE_ORDER_FAIL.getCode(), "采购量小于5m³，请重新填写");
        }


        DealerInfo dealerInfo=dealerInfoMapper.selectByPrimaryKey(orderInfo.getDealerId());

        Category category=categoryMapper.selectByPrimaryKey(dealerInfo.getCategoryId());

        if (category==null){
            return ServerResponse.createByErrorCode(ResponseCode.CREATE_ORDER_FAIL.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        if (status!=1&&status!=2){
            return ServerResponse.createByErrorCode(ResponseCode.CREATE_ORDER_FAIL.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        orderInfo.setArea(category.getName());
        orderInfo.setOrderId(UUIDUtil.getUUID8());
        orderInfo.setTotalVolume(totalVolume);
        orderInfo.setTotalCount(totalCount);
        orderInfo.setTotalAmount(totalAmount);
        orderInfo.setCreateTime(new Date());
        orderInfo.setUpdateTime(new Date());
        orderInfo.setChildId(cid);
        orderInfo.setStatus(status);
        Object savePoint=TransactionAspectSupport.currentTransactionStatus().createSavepoint();
        if (orderInfoMapper.insertSelective(orderInfo)!=1){
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
            return ServerResponse.createByErrorCode(ResponseCode.CREATE_ORDER_FAIL.getCode(), ResponseCode.CREATE_ORDER_FAIL.getDesc());
        }
        OrderProduct orderProduct=new OrderProduct();
        orderProduct.setOrderId(orderInfo.getId());
        for (int i=0;i<productInfoList.size();i++){
            orderProduct.setProductId(productInfoList.get(i).getId());
            orderProduct.setCount(counts[i]);
            orderProduct.setAmount(productInfoList.get(i).getProductOpenFare());
            orderProduct.setVolume(productInfoList.get(i).getProductVolume());
            if (orderProductMapper.insertSelective(orderProduct)!=1){
                TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
                return ServerResponse.createByErrorCode(ResponseCode.CREATE_ORDER_FAIL.getCode(), ResponseCode.CREATE_ORDER_FAIL.getDesc());
            }
        }


        return ServerResponse.createBySuccess("添加成功");
    }

    @Override
    public ServerResponse<List<ProductInfo>> getAllProduct(Integer cid, Integer dealerId) {
        List<ProductInfo> productInfos=productInfoMapper.selectBydealerId(dealerId);
        return ServerResponse.createBySuccess(productInfos);
    }

    @Override
    public ServerResponse<Set<ProductInfo>> searchProduct(Integer productID, String productModel, Integer categoryId) {
        Set<ProductInfo> set=new HashSet<>();
        List<ProductInfo> list=productInfoMapper.searchProductByKeyWord(productID,productModel,categoryId);
        list.stream().forEach(n ->{
            if (!set.contains(n)){
                set.add(n);
            }
        });
        list=productInfoMapper.searchProductByPingYingFirst(productID,productModel,categoryId);
        list.stream().forEach(n ->{
            if (!set.contains(n)){
                set.add(n);
            }
        });
        list=productInfoMapper.searchProductByPingYing(productID,productModel,categoryId);
        list.forEach(n ->{
            if (!set.contains(n)){
                set.add(n);
            }
        });
        return ServerResponse.createBySuccess(set);
    }

    @Override
    public ServerResponse<String> updateOrderStatus(Integer orderId,Integer status) {
        OrderInfo orderInfo=orderInfoMapper.selectByPrimaryKey(orderId);
        if (orderInfo==null){
            return ServerResponse.createByErrorCode(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        orderInfo.setStatus(status);
        orderInfo.setId(orderId);
        if (orderInfoMapper.updateByPrimaryKeySelective(orderInfo)==1){
            return ServerResponse.createBySuccess("更新成功");
        }
        return ServerResponse.createByErrorCode(ResponseCode.UPDATE_FAIL.getCode(), ResponseCode.REGISTER_FAIL.getDesc());
    }

    @Override
    public boolean uploadFile(MultipartFile file, Integer orderId,String path) {

            String fileName=file.getOriginalFilename();
            String FileExtionName=fileName.substring(fileName.lastIndexOf(".")+1);
            String uploadFileName=UUIDUtil.getUUID8()+"."+FileExtionName;
            File fileDir = new File(path);
            OrderInfo orderInfo=orderInfoMapper.selectByPrimaryKey(orderId);
            if (orderInfo.getStatus()!=3 && orderInfo.getStatus()!=4){
                return false;
            }
            if (!fileDir.exists()) {
                fileDir.setWritable(true);
                fileDir.mkdirs();
            }
            File targetFile = new File(path, uploadFileName);
            try {
                file.transferTo(targetFile);
                SftpUtil.uploadFile(targetFile);
                targetFile.delete();
                OrderFile orderFile=new OrderFile();
                orderFile.setFilePath(uploadFileName);
                orderFile.setOrderId(orderId);
                if (orderFileMapper.insert(orderFile)!=1){
                    return false;
                }
                orderInfo.setStatus(4);
                orderInfoMapper.updateByPrimaryKeySelective(orderInfo);
            }catch (Exception IO){
                log.error("上传文件异常");
                return false;
            }
            return true;

    }

    @Override
    public ServerResponse<List<OrderVo>> getAllOrder(Integer id) {
        List<OrderInfo> orderInfos=orderInfoMapper.selectById(id);
        List<OrderVo> orderVoList=new ArrayList<>();
        orderInfos.forEach(i -> orderVoList.add(assembleOrderVo(i)));
        return ServerResponse.createBySuccess(orderVoList);
    }

    @Override
    public ServerResponse<List<DealerInfo>> getAllDealerIdByChild(DealerChild dealerChild) {
        ManagerDealerInfo managerDealerInfos= managerDealerInfoMapper.selectByPrimaryKey(dealerChild.getParentId());
        List<DealerInfo> dealerInfos=dealerInfoMapper.selectByManageId(managerDealerInfos.getId());
        List<DealerChildCategory> dealerChildCategories=dealerChildCategoryMapper.selectByDealerId(dealerChild.getId());
        List<DealerInfo> dealerInfoList=new ArrayList<>();
        dealerInfos.forEach(i->{
            if (i.getManagerId()==managerDealerInfos.getId()){
                dealerChildCategories.forEach(j ->{
                    if (i.getCategoryId().equals(j.getCategoryId())){
                        dealerInfoList.add(i);
                    }
                });
            }
        });
        return ServerResponse.createBySuccess(dealerInfoList);
    }

    private OrderVo assembleOrderVo(OrderInfo orderInfo){
        OrderVo orderVo=new OrderVo();
        orderVo.setArea(orderInfo.getArea());
        orderVo.setOrderId(orderInfo.getOrderId());
        orderVo.setStatus(orderInfo.getStatus());
        orderVo.setId(orderInfo.getId());
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
