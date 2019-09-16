package common;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class StartTest {

    ProcessEngine processEngine =  ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程定义
     */
    @Test//方式一addClasspathResource
    public void deploymentProcessDefinition(){
        Deployment deployment = (Deployment) processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
                .createDeployment()//创建一个部署对象
                .name("start")//添加部署的名称
                .addClasspathResource("diagrams/start.bpmn")//从classpath的资源中加载，一次只能加载一个文件
                .addClasspathResource("diagrams/start.png")//从classpath的资源中加载，一次只能加载一个文件
                .deploy();//完成部署
//        System.out.println("部署ID"+deployment.getId());//1
//        System.out.println("部署名称"+deployment.getName());//helloworld入门程序
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance(){
        String processDefinitionKey = "start";
        ProcessInstance pi = processEngine.getRuntimeService()//与正在执行的流程实例和执行对象相关的service
                //使用流程定义的key启动流程实例，key对应helloworld.bpmn文件中id的属性，使用key值启动，默认是按照最新版本的流程定义启动
                .startProcessInstanceByKey(processDefinitionKey);
        System.out.println("流程实例ID"+pi.getId());//101
        System.out.println("流程定义ID"+pi.getProcessDefinitionId());//helloworld:1:4

        /**判断流程是否结束，查询正在执行的执行对象表*/
        ProcessInstance rpi = processEngine.getRuntimeService()
                .createProcessInstanceQuery()
                .processInstanceId(pi.getId())
                .singleResult();

        //说明流程实例结束了
        if(rpi==null){
            /**查询历史，获取流程的相关信息*/
            HistoricProcessInstance hpi = processEngine.getHistoryService()
                    .createHistoricProcessInstanceQuery()
                    .processInstanceId(rpi.getId())
                    .singleResult();


            //System.out.println(hpi.getId())+" "+hpi.getStartTime()+" "+hpi.getEndTime()+" "+hpi.getDurationInMillis();
        }
    }

    /**
     * 完成我的任务
     */
    @Test
    public void completeMyPersonTask(){
        //任务ID
        String taskId = "3504";
        //完成任务的同时设置流程变量，使用流程变量来完成任务后，下一个连线，对应exclusiveGate.bpmn
        Map<String,Object> variables = new HashMap<String, Object>();
        variables.put("money",800);
        processEngine.getTaskService()//与正在执行任务管理相关的service
                .complete(taskId);
        System.out.println("完成任务：任务ID"+ taskId);
    }


}
