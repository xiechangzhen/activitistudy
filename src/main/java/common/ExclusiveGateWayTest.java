package common;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExclusiveGateWayTest {

    ProcessEngine processEngine =  ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程定义
     */
    @Test//方式一addClasspathResource
    public void deploymentProcessDefinition(){
        Deployment deployment = (Deployment) processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
                .createDeployment()//创建一个部署对象
                .name("排他网关")//添加部署的名称
                .addClasspathResource("diagrams/exclusiveGate.bpmn")//从classpath的资源中加载，一次只能加载一个文件
                .addClasspathResource("diagrams/exclusiveGate.png")//从classpath的资源中加载，一次只能加载一个文件
                .deploy();//完成部署
//        System.out.println("部署ID"+deployment.getId());//1
//        System.out.println("部署名称"+deployment.getName());//helloworld入门程序
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance(){
        String processDefinitionKey = "exclusiveGate";
        ProcessInstance pi = processEngine.getRuntimeService()//与正在执行的流程实例和执行对象相关的service
                //使用流程定义的key启动流程实例，key对应helloworld.bpmn文件中id的属性，使用key值启动，默认是按照最新版本的流程定义启动
                .startProcessInstanceByKey(processDefinitionKey);
        System.out.println("流程实例ID"+pi.getId());//101
        System.out.println("流程定义ID"+pi.getProcessDefinitionId());//helloworld:1:4

    }

    /**
     *  查询当前个人任务
     */
    @Test
    public void findMyPersonalTask(){
        String assignee = "张三";
        List<Task> list = processEngine.getTaskService()
                .createTaskQuery()//创建任务对象
                //查询条件（where部分）
                .taskAssignee(assignee)//指定个人任务查询，指定办理人
//                .taskCandidateUser("")//组任务的办理人查询
//                .processDefinitionId("")//使用流程定义ID查询
//                .processInstanceId("")//使用流程实例ID查询
//                .executionId("")//使用执行对象ID查询
                /**排序*/
                .orderByTaskCreateTime().asc()
                .list();
        if(list != null && list.size() > 0){
            for(Task task:list){
                System.out.println("任务ID：" + task.getId());
                System.out.println("任务名称：" + task.getName());
                System.out.println("任务的创建时间：" + task.getCreateTime());
                System.out.println("任务的办理人：" + task.getAssignee());
                System.out.println("流程实例ID：" + task.getProcessInstanceId());
                System.out.println("执行对象ID：" + task.getExecutionId());
                System.out.println("流程定义ID：" + task.getProcessDefinitionId());
            }
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
        Map<String,Object> variables = new HashMap<String,Object>();
        variables.put("money",800);
        processEngine.getTaskService()//与正在执行任务管理相关的service
                .complete(taskId);
        System.out.println("完成任务：任务ID"+ taskId);
    }


}
