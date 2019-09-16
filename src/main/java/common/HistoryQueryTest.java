package common;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.junit.Test;

public class HistoryQueryTest {
    ProcessEngine processEngine =  ProcessEngines.getDefaultProcessEngine();

    /**查询历史流程实例**/
    @Test
    public void findHistoryProcessInstance(){
        String processHistoryId = "2101";
        processEngine.getHistoryService()
                .createHistoricProcessInstanceQuery()//创建历史流程实例查询
                .processInstanceId(processHistoryId)//创建流程实例id
                .orderByProcessInstanceStartTime().asc()
                .singleResult();
    }

    /**查询历史活动*/
    @Test
    public void findHistoryProcessInstance1(){
        String processHistoryId = "2101";
//        processEngine.getHistoryService()
//                .createHistoricActivityInstanceQuery()//
        ///
    }


}
