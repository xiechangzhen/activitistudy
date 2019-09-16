package common;

import com.mchange.io.FileUtils;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Hello {
    ProcessEngine processEngine =  ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程定义
     */
    @Test//方式一addClasspathResource
    public void deploymentProcessDefinition(){
        Deployment deployment = (Deployment) processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
        .createDeployment()//创建一个部署对象
        .name("helloworld入门程序")//添加部署的名称
        .addClasspathResource("diagrams/helloworld.bpmn")//从classpath的资源中加载，一次只能加载一个文件
        .addClasspathResource("diagrams/helloworld.png")//从classpath的资源中加载，一次只能加载一个文件
        .deploy();//完成部署
        System.out.println("部署ID"+deployment.getId());//1
        System.out.println("部署名称"+deployment.getName());//helloworld入门程序
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance(){
        String processDefinitionKey = "helloworld";
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
        String taskId = "104";
        processEngine.getTaskService()//与正在执行任务管理相关的service
            .complete(taskId);
        System.out.println("完成任务：任务ID"+ taskId);
    }

    /**
     * 查询流程定义
     */
    @Test
    public void findProcessDefinition(){
        List<ProcessDefinition> list = processEngine.getRepositoryService()//与流程定义相关和部署对象相关的Service
        .createProcessDefinitionQuery()//创建一个流程定义的查询
        /**
         * 指定查询条件，where条件
         */
        .deploymentId("")//使用部署对象ID查询
        .processDefinitionKey("")//使用流程定义的key查询
        .processDefinitionNameLike("")//使用流程定义的名称模糊查询

        /**排序*/
        .orderByProcessDefinitionVersion().asc()//按照流程版本的升序排序
        .orderByProcessDefinitionName().desc() //按照流程定义的名称将序排列

        /**返回的结果集*/
        .list();//返回一个集合列表，封装流程定义
//        .singleResult();//返回唯一结果集
//        .count();//返回数量
//        .listPage(firstResult,maxResults);//分页查询

        if(list != null && list.size()>0){
            for (ProcessDefinition pd : list){
                System.out.println("流程定义ID："+pd.getId());//流程定义的key+版本+随机生成数
                System.out.println("流程定义的名称："+pd.getName());//对应helloworld.bpmn中的name属性值
                System.out.println("流程定义的key："+pd.getId());//对应helloworld.bpmn中的id属性值
                System.out.println("流程定义的版本："+pd.getVersion());//流程定义的key相同的情况下，版本升级
                System.out.println("资源名称bpmn文件："+pd.getResourceName());
                System.out.println("资源名称png文件："+pd.getDiagramResourceName());
                System.out.println("部署对象ID："+pd.getDeploymentId());
            }
        }
    }

    /**
     * 删除流程定义
     */
    @Test
    public void deleteProcessDefinition(){
        //使用部署ID，完成删除
        String deploymentId = "601";
        /**
         * 不带级联的删除
         *      只能删除没有启动的流程，如果流程启动，就会抛出异常
         */
//        processEngine.getRepositoryService()
//                .deleteDeployment(deploymentId);

        /**
         * 级联删除
         *      不管流程是否启动，都能删除
         */
        processEngine.getRepositoryService()
                .deleteDeployment(deploymentId,true);
        System.out.println("删除成功！");
    }

    /**
     * 查看流程图
     */
    @Test
    public void viewPic(){
        /***将生成图片放到文件夹下/
         */
        String deploymentId = "801";
        //获取图片资源名称
        List<String> list = processEngine.getRepositoryService().getDeploymentResourceNames(deploymentId);
        //定义流程图片资源的名称
        String resourceName = "";
        if(list != null && list.size() > 0){
            for(String name:list){
                if(name.indexOf(".png") >=0){
                    resourceName = name;
                }
            }
        }

        //获取图片的输入流
        InputStream in = processEngine.getRepositoryService().getResourceAsStream(deploymentId, resourceName);
        //将图片生成到D盘目录下
        File file = new File("D:/"+ resourceName);
        //将输入流的图片写到D盘下
        //FileUtils.copyInputStreamToFile(in,file);//?
    }

    /**
     * 附加功能：查询最新版本的流程定义
     */
    @Test
    public void findLastVersionProcessDefinition(){
        List<ProcessDefinition> list = processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                .orderByProcessDefinitionVersion().asc()
                .list();
        /**
         * Map<String,ProcessDefinition>
         *     key:流程定义的key
         *     value：流程定义的对象、
         *     当map集合key值相同的情况下，后一次的值将替换前一次的值
         */
        Map<String,ProcessDefinition> map = new LinkedHashMap<String,ProcessDefinition>();
        if (list != null && list.size() > 0){
            for(ProcessDefinition pd : list){
                map.put(pd.getKey(),pd);
            }
        }
        List<ProcessDefinition> pdList = new ArrayList<ProcessDefinition>(map.values());
        if(pdList != null && pdList.size()>0){
            for (ProcessDefinition pd : pdList){
                System.out.println("流程定义ID："+pd.getId());//流程定义的key+版本+随机生成数
                System.out.println("流程定义的名称："+pd.getName());//对应helloworld.bpmn中的name属性值
                System.out.println("流程定义的key："+pd.getId());//对应helloworld.bpmn中的id属性值
                System.out.println("流程定义的版本："+pd.getVersion());//流程定义的key相同的情况下，版本升级
                System.out.println("资源名称bpmn文件："+pd.getResourceName());
                System.out.println("资源名称png文件："+pd.getDiagramResourceName());
                System.out.println("部署对象ID："+pd.getDeploymentId());
            }
        }
    }

    /**
     * 附加功能：删除流程定义（删除key相同的所有不同版本的流程定义）
     */
    @Test
    public void deleteProcessDefinitionBykey(){
        //流程定义的key
        String processDefinitionKey = "helloworld";
        //先使用流程定义的key查询流程定义，查询所有的版本
        List<ProcessDefinition> list = processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey)//使用流程定义的key查询
                .list();
        //遍历，获取每个流程定义的部署ID
        if(list != null && list.size() > 0){
            for(ProcessDefinition pd : list){
                String deploymentId = pd.getDeploymentId();
                processEngine.getRepositoryService()
                        .deleteDeployment(deploymentId,true);

            }
        }
    }

}


