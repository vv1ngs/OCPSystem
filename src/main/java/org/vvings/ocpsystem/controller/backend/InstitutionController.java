package org.vvings.ocpsystem.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.vvings.ocpsystem.common.ServerResponse;
import org.vvings.ocpsystem.pojo.Institution;
import org.vvings.ocpsystem.service.InstitutionService;

import java.util.List;

/**
 * @Author vvings
 * @Date 2020/12/18 21:05
 * @Version 1.0
 */
@Controller("/institution")
@RequestMapping("/institution")
@CrossOrigin(origins = {"*"},allowCredentials = "true")
public class InstitutionController {
    @Autowired
    private InstitutionService institutionService;
    @RequestMapping("/getInstitution")
    @ResponseBody
    public ServerResponse<List<Institution>> getInstitution(@RequestParam(value = "institutionId",defaultValue = "0") Integer institutionId){
        return institutionService.getChildrenParallelInstitution(institutionId);
    }
    @RequestMapping("/getDeepInstitution")
    @ResponseBody
    public ServerResponse<List<Integer>> getDeepInstitution(@RequestParam(value = "institutionId",defaultValue = "0") Integer institutionId){
        return institutionService.getInstitutionAndChildren(institutionId);
    }


}
