package org.vvings.ocpsystem.dao;

import org.vvings.ocpsystem.pojo.DealerInstitution;

public interface DealerInstitutionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DealerInstitution record);

    int insertSelective(DealerInstitution record);

    DealerInstitution selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DealerInstitution record);

    int updateByPrimaryKey(DealerInstitution record);
}