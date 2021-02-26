package org.vvings.ocpsystem.service;

import org.springframework.web.multipart.MultipartFile;
import org.vvings.ocpsystem.common.ServerResponse;
import org.vvings.ocpsystem.pojo.DealerChild;
import org.vvings.ocpsystem.pojo.DealerInfo;
import org.vvings.ocpsystem.pojo.OrderInfo;
import org.vvings.ocpsystem.pojo.ProductInfo;
import org.vvings.ocpsystem.vo.OrderVo;

import java.util.List;
import java.util.Set;

/**
 * @Author vvings
 * @Date 2020/12/21 23:49
 * @Version 1.0
 */
public interface dealerOrderService {

    ServerResponse<String> addOrder(OrderInfo orderInfo, Integer[] productId, Integer[] counts,Integer cid,Integer status);

    ServerResponse<List<ProductInfo>> getAllProduct(Integer cid, Integer dealerId);

    ServerResponse<Set<ProductInfo>> searchProduct(Integer productID, String productModel, Integer categoryId);

    ServerResponse<String> updateOrderStatus(Integer orderId,Integer status);

    boolean uploadFile(MultipartFile file, Integer orderId,String path);

    ServerResponse<List<OrderVo>> getAllOrder(Integer id);

    ServerResponse<List<DealerInfo>> getAllDealerIdByChild(DealerChild dealerChild);
}
