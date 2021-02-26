package org.vvings.ocpsystem.dao;

import org.vvings.ocpsystem.pojo.ProductInfo;

import java.util.List;

public interface ProductInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProductInfo record);

    int insertSelective(ProductInfo record);

    ProductInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProductInfo record);

    int updateByPrimaryKey(ProductInfo record);

    List<ProductInfo> selectBydealerId(Integer dealerId);


    List<ProductInfo> searchProductByKeyWord(Integer productID, String productModel, Integer categoryId);

    List<ProductInfo> searchProductByPingYingFirst(Integer productID, String productModel, Integer categoryId);

    List<ProductInfo> searchProductByPingYing(Integer productID, String productModel, Integer categoryId);
}