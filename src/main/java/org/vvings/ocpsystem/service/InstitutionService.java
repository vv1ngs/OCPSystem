package org.vvings.ocpsystem.service;

import org.vvings.ocpsystem.common.ServerResponse;
import org.vvings.ocpsystem.pojo.Institution;

import java.util.List;

/**
 * @Author vvings
 * @Date 2020/12/18 21:30
 * @Version 1.0
 */
public interface InstitutionService {
    ServerResponse<List<Institution>> getChildrenParallelInstitution(Integer institutionId);

    ServerResponse<List<Integer>> getInstitutionAndChildren(Integer institutionId);
}
