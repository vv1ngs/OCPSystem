package org.vvings.ocpsystem.service;

import org.vvings.ocpsystem.common.ServerResponse;
import org.vvings.ocpsystem.pojo.Category;
import org.vvings.ocpsystem.pojo.DealerInfo;
import org.vvings.ocpsystem.pojo.ManagerDealerInfo;
import org.vvings.ocpsystem.pojo.User;

import java.util.List;
import java.util.Set;

/**
 * @Author vvings
 * @Date 2020/12/19 22:56
 * @Version 1.0
 */
public interface manageDealerService {
    ServerResponse<String> addManageDealer(ManagerDealerInfo managerDealerInfo,String loginName);

    ServerResponse<String> associatedDealer(Integer id, Integer mid);

    ServerResponse<List<DealerInfo>> getAllDealer();

    ServerResponse<String> removeAssociatedDealer(Integer id, Integer mid);

    ServerResponse<List<DealerInfo>> getAllAssociatedDealer(Integer mid);

    boolean updateStatus(Integer integer, Boolean aBoolean);

    boolean delUserByuid(Integer integer);

    ServerResponse<String> updateManageDealer(ManagerDealerInfo managerDealerInfo);

    ServerResponse<Set<ManagerDealerInfo>> searchManageDealer(String name, String username, Boolean status);



    ServerResponse<Set<DealerInfo>> searchDealer(String name, int category, Integer status);

    ServerResponse<List<Category>> getAllCategory();

    List<User> getUserByuid(Integer[] uid);

    ManagerDealerInfo getUserByUsername(String username);
}
