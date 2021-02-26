package org.vvings.ocpsystem.service;

import org.vvings.ocpsystem.common.ServerResponse;
import org.vvings.ocpsystem.pojo.*;

import java.util.List;
import java.util.Set;

/**
 * @Author vvings
 * @Date 2020/12/20 18:35
 * @Version 1.0
 */
public interface dealerService {



    ServerResponse<String> addChildDealer(DealerChild dealerChild,String username);

    boolean updateStatus(Integer integer, Boolean aBoolean);

    boolean delDealerByid(Integer integer);

    ServerResponse<String> updateDealer(DealerChild dealerChild);

    ServerResponse<Set<Category>> getAllCategory(Integer id);


    boolean allocationRole(Integer integer, Integer uid);

    ServerResponse<Set<DealerChild>> searchChildDealer(DealerChild dealerChild);

    boolean removeAllCategory(Integer uid);

    ServerResponse<List<Category>> getChildCategory(Integer childId);

    List<User> getUserByuid(Integer[] uid);

    DealerChild getUserByusername(String username);

    ServerResponse<List<DealerChild>> getAllChildDealer(String username );
}
