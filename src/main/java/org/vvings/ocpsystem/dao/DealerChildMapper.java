package org.vvings.ocpsystem.dao;

import org.vvings.ocpsystem.pojo.DealerChild;
import org.vvings.ocpsystem.pojo.ManagerDealerInfo;

import java.util.List;

public interface DealerChildMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DealerChild record);

    int insertSelective(DealerChild record);

    DealerChild selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DealerChild record);

    int updateByPrimaryKey(DealerChild record);

    DealerChild selectByUsername(String username);

    int  selectName(String username);

    List<DealerChild> searchRoleByKeyWord(DealerChild dealerChild);

    List<DealerChild> searchRoleByPingYingFirst(DealerChild dealerChild);

    List<DealerChild> searchRoleByPingYing(DealerChild dealerChild);

    List<DealerChild> selectAll();

    List<DealerChild> selectByparentID(Integer id);
}