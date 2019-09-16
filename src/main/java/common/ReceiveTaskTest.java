package common;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.Deployment;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ReceiveTaskTest {

    ProcessEngine processEngine =  ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程定义
     */
    @Test//方式一addClasspathResource
    public void deploymentProcessDefinition(){
        Deployment deployment = (Deployment) processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
                .createDeployment()//创建一个部署对象
                .name("receiveTask")//添加部署的名称
                .addClasspathResource("diagrams/receiveTask.bpmn")//从classpath的资源中加载，一次只能加载一个文件
                .addClasspathResource("diagrams/receiveTask.png")//从classpath的资源中加载，一次只能加载一个文件
                .deploy();//完成部署
        //System.out.println("部署ID"+deployment.getId());//1
        //System.out.println("部署名称"+deployment.getName());//helloworld入门程序
    }

    /**
     * 启动流程实例+判断流程是否结束+查询历史
     */
    @Test
    public void startProcessInstance(){
        //流程定义的key
        String processDefinitionKey = "receiveTask";
        ProcessInstance pi = processEngine.getRuntimeService()//与正在执行的流程实例和执行对象相关的service
                //使用流程定义的key启动流程实例，key对应helloworld.bpmn文件中id的属性，使用key值启动，默认是按照最新版本的流程定义启动
                .startProcessInstanceByKey(processDefinitionKey);
        System.out.println("流程实例ID"+pi.getId());//101
        System.out.println("流程定义ID"+pi.getProcessDefinitionId());//helloworld:1:4

        /**判断流程是否结束，查询正在执行的执行对象表*/
        Execution ex = processEngine.getRuntimeService()
                .createExecutionQuery()//创建执行对象查询
                .processInstanceId(pi.getId())//使用流程实例ID查询
                .activityId("receivetask1")//当前活动的id，对应receiveTask.bpmn文件中活动节点id的属性值
                .singleResult();

        /**使用流程变量设置当日销售额，用来传递业务蚕参数*/
//        processEngine.getRuntimeService()
//                .setVariables(ex.getId(),"汇总当日销售额",21000);


           // System.out.println(hpi.getId())+" "+hpi.getStartTime()+" "+hpi.getEndTime()+" "+hpi.getDurationInMillis();
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
