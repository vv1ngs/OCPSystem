package org.vvings.ocpsystem.controller.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.vvings.ocpsystem.common.ResponseCode;
import org.vvings.ocpsystem.common.ServerResponse;
import org.vvings.ocpsystem.pojo.*;
import org.vvings.ocpsystem.service.dealerOrderService;
import org.vvings.ocpsystem.service.dealerService;
import org.vvings.ocpsystem.service.loginService;
import org.vvings.ocpsystem.vo.OrderVo;

import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @Author vvings
 * @Date 2020/12/21 23:47
 * @Version 1.0
 */
@Controller("/dealerOrder")
@RequestMapping("/dealerOrder")
@CrossOrigin(origins = {"*"},allowCredentials = "true")
public class DealerOrderController {
    @Autowired
    private dealerOrderService dealerOrderService;
    @Autowired
    private loginService loginService;
    @Autowired
    private org.vvings.ocpsystem.service.dealerService dealerService;
    @RequestMapping("/addOrder")
    @ResponseBody
    public ServerResponse<String> addOrder(OrderInfo orderInfo, @RequestParam(value="pid[]") Integer[] productId, @RequestParam(value="cid[]") Integer[] counts,Principal principal,Integer status){
        String username=principal.getName();
        User user=loginService.getUserByUsername(username);
        if (user.getCategoryId()!=2){
            return ServerResponse.createByErrorCode(ResponseCode.PERMISSION_DENIED.getCode(), ResponseCode.PERMISSION_DENIED.getDesc());
        }
        if (productId.length > counts.length){
            return ServerResponse.createByErrorCode(ResponseCode.CREATE_ORDER_FAIL.getCode(), "请填写需求数量");
        }
        if (productId.length==0){
            return ServerResponse.createByErrorCode(ResponseCode.CREATE_ORDER_FAIL.getCode(), "请至少填写一条产品信息");
        }
        return dealerOrderService.addOrder(orderInfo,productId,counts,user.getId(), status);
    }

    @ResponseBody
    @RequestMapping("/getAllDealerId")
    public ServerResponse<List<DealerInfo>>getAllDealerId(Principal principal){
        String username=principal.getName();
        User user=loginService.getUserByUsername(username);
        if (user.getCategoryId()!=2){
            return ServerResponse.createByErrorCode(ResponseCode.PERMISSION_DENIED.getCode(), ResponseCode.PERMISSION_DENIED.getDesc());
        }
        DealerChild dealerChild = dealerService.getUserByusername(username);
        return dealerOrderService.getAllDealerIdByChild(dealerChild);
    }


    @RequestMapping("/getAllOrder")
    @ResponseBody
    public ServerResponse<List<OrderVo>> getAllOrder(Principal principal) {
        String username=principal.getName();
        User user=loginService.getUserByUsername(username);
        if (user.getCategoryId()!=2){
            return ServerResponse.createByErrorCode(ResponseCode.PERMISSION_DENIED.getCode(), ResponseCode.PERMISSION_DENIED.getDesc());
        }
        return dealerOrderService.getAllOrder(user.getId());
    }

    @RequestMapping("/getAllProduct")
    @ResponseBody
    public ServerResponse<List<ProductInfo>> getAllProduct(Principal principal,Integer dealerId){
        String username=principal.getName();
        User user=loginService.getUserByUsername(username);
        if (user.getCategoryId()!=2){
            return ServerResponse.createByErrorCode(ResponseCode.PERMISSION_DENIED.getCode(), ResponseCode.PERMISSION_DENIED.getDesc());
        }
        DealerChild dealerChild = dealerService.getUserByusername(username);
        return dealerOrderService.getAllProduct(dealerChild.getId(),dealerId);
    }

    @RequestMapping("/searchProduct")
    @ResponseBody
    public ServerResponse<Set<ProductInfo>> searchProduct(Integer productID, String productModel, Integer categoryId){
        return dealerOrderService.searchProduct(productID,productModel,categoryId);
    }

    @RequestMapping("/submitOrCancelOrder")
    @ResponseBody
    public ServerResponse<String> submitOrCancelOrder(Integer orderId,Integer status){
        if (status!=0&&status!=2&&status!=1){
            return ServerResponse.createByErrorCode(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return dealerOrderService.updateOrderStatus(orderId,status);
    }
    @RequestMapping("/uploadFile")
    @ResponseBody
    @Transactional()
    public ServerResponse<String> uploadFile(Integer orderId, @RequestParam("files") MultipartFile[] files, HttpServletRequest req){
        if (files.length==0){
            return ServerResponse.createByErrorCode(ResponseCode.UPLOAD_FILE_FAIL.getCode(), ResponseCode.UPLOAD_FILE_FAIL.getDesc());
        }
        Object savePoint= TransactionAspectSupport.currentTransactionStatus().createSavepoint();
        for (MultipartFile file :files){
            if (file.isEmpty()){
                return ServerResponse.createByErrorCode(ResponseCode.UPLOAD_FILE_FAIL.getCode(), ResponseCode.UPLOAD_FILE_FAIL.getDesc());
            }
            String path = req.getSession().getServletContext().getRealPath("upload");
            if (!dealerOrderService.uploadFile(file,orderId,path)){
                TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
                return ServerResponse.createByErrorCode(ResponseCode.UPLOAD_FILE_FAIL.getCode(), ResponseCode.UPLOAD_FILE_FAIL.getDesc());
            }

        }
        return ServerResponse.createBySuccess("成功上传成功");
    }

}

