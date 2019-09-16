package common;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.Deployment;
import org.junit.Test;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipInputStream;

public class ProcessVariableTest {
    ProcessEngine processEngine =  ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程定义
     */
    @Test //方式三addZipInputStream
    public void deploymentProcessDefinition(){
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("diagrams/helloworld.zip");
        ZipInputStream zipInputStream = new ZipInputStream(resourceAsStream);

        Deployment deployment = (Deployment) processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
                .createDeployment()//创建一个部署对象
                .name("流程定义")//添加部署的名称
                .addZipInputStream(zipInputStream)//从classpath的资源中加载，一次只能加载一个文件
                .deploy();//完成部署
        //System.out.println("部署ID"+deployment.getId());//1
        //System.out.println("部署名称"+deployment.getName());//helloworld入门程序
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance(){
        String processDefinitionKey = "processVariable";
        ProcessInstance pi = processEngine.getRuntimeService()//与正在执行的流程实例和执行对象相关的service
                //使用流程定义的key启动流程实例，key对应helloworld.bpmn文件中id的属性，使用key值启动，默认是按照最新版本的流程定义启动
                .startProcessInstanceByKey(processDefinitionKey);
        System.out.println("流程实例ID"+pi.getId());//101
        System.out.println("流程定义ID"+pi.getProcessDefinitionId());//helloworld:1:4
    }
    /**设置流程变量*/
    @Test
    public void setVariable(){
        /**与任务（正在执行）*/
        TaskService taskService = processEngine.getTaskService();
        //任务ID
        String taskID = "1504";
        /**一：设置流程变量，使用基本数据类型*/
//        taskService.setVariableLocal(taskId,"请假天数",3);/与任务id绑定
//        taskService.setVariable(taskId,"请假日期",new Date());
//        taskService.setVariable(taskId,"请假原因","回家探亲");
        /**二：设置流程变量，使用JavaBean类型
         * 当一个javabean（实现序列号）放置到流程变量中，要求javabean的属性不能再发生变化
         * 如果发生变化，再获取的时候，抛出异常
         */
        Person p = new Person();
        p.setId(10);
        p.setName("翠花");
        //taskService.setVariable(taskId,"人员信息",p);


        System.out.println("设置流程变量成功！");
    }


    /**获取流程变量*/
    @Test
    public void getVariable(){

        TaskService taskService = processEngine.getTaskService();
        /**获取流程变量，使用基本数据类型*/
        String taskId = "1504";
        Integer days = (Integer)taskService.getVariable(taskId, "请假天数");
        Date date = (Date)taskService.getVariable(taskId,"请假日期");
       /**二：获取流程变量，使用Javabean类型*/
       Person p = (Person)taskService.getVariable(taskId,"人员信息");

        String reason = (String)taskService.getVariable(taskId,"请假原因");
    }

    /**模拟设置和获取流程变量的场景**/
    public void setAndGetVariables(){
        /**流程实例，执行对象（正在执行）*/
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //与任务（正在执行）
        TaskService taskService = processEngine.getTaskService();
        //设置流程变量

        //表示使用执行对象的ID，和流程变量的名称，设置流程变量的值（一次只能设置一个值）
//        runtimeService.setVariable(executionId,variableName,value);

        //表示使用执行对象ID，和Map集合设置流程变量，map集合的key就是流程变量的名称，
        // map集合的value就是流程变量的值（一次可以设置多个值）
//        runtimeService.setVariables(executionId,variables);

//        runtimeService.startProcessInstanceByKey(processDefinitionKey,variables);//启动流程实例的同时，可以设置流程变量，用Map集合
//        taskService.complete(taskId,variables);//完成任务的同时，设置流程变量，用Map集合

        /**获取流程变量*/
//        runtimeService.getVariable(executionId,variableName);//使用执行对象ID和流程变量的名称，获取流程变量的值
//        runtimeService.getVariable(executionId);//使用执行对象的ID，获取所有的流程变量，将流程变量放置到Map集合中，map集合的key就是流程变量的名称，map集合的value就是流程变量的值
//        runtimeService.getVariable(executionId,variableNames);//使用执行对象ID，获取流程变量的值，通过设置流程变量的名称存放到集合中，获取指定流程变量名称的值，值存放到Map集合中
    }

    /**
     * 完成我的任务
     */
    @Test
    public void completeMyPersonTask(){
        //任务ID
        String taskId = "1504";
        processEngine.getTaskService()//与正在执行任务管理相关的service
                .complete(taskId);
        System.out.println("完成任务：任务ID"+ taskId);
    }

    /**查询流程变量的历史表*/
    @Test
    public void findHistoryProcessVariable(){
        List<HistoricVariableInstance> list = processEngine.getHistoryService()
                .createHistoricVariableInstanceQuery()//创建一个历史的流程变量查询对象
                .variableName("请假天数")
                .list();
        if(list != null && list.size()>0){
            for(HistoricVariableInstance hvi: list){
                System.out.println(hvi.getId()+" "+ hvi.getProcessInstanceId()+" "+hvi.getVariableName()+" "+hvi.getVariableTypeName());
            }

        }

    }



}
