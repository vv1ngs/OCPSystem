package org.vvings.ocpsystem.dao;

import org.vvings.ocpsystem.pojo.DealerChildCategory;

import java.util.List;

public interface DealerChildCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DealerChildCategory record);

    int insertSelective(DealerChildCategory record);

    DealerChildCategory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DealerChildCategory record);

    int updateByPrimaryKey(DealerChildCategory record);

    int deleteByuid(Integer integer);

    List<DealerChildCategory> selectByDealerId(Integer childId);
}