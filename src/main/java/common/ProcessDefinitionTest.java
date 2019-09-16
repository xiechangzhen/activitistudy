package common;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.test.Deployment;
import org.junit.Test;

import java.io.InputStream;
import java.util.zip.ZipInputStream;

public class ProcessDefinitionTest {
    ProcessEngine processEngine =  ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程定义
     */
    @Test //方式二addInputStream
    public void deploymentProcessDefinition(){
        InputStream inputStreambpmn = this.getClass().getResourceAsStream("/diagrams/processVariables.bpmn");
        InputStream inputStreampng = this.getClass().getResourceAsStream("/diagrams/processVariables.png");

        Deployment deployment = (Deployment) processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
                .createDeployment()//创建一个部署对象
                .name("流程定义")//添加部署的名称
                .addInputStream("processVariables.bpmn",inputStreambpmn)//使用资源文件的名称（要求：与资源文件的名称要求一致），和输入流完成
                .addInputStream("processVariables.png",inputStreampng)//使用资源文件的名称（要求：与资源文件的名称要求一致），和输入流完成
                .deploy();//完成部署
        //System.out.println("部署ID"+deployment.getId());//1
        //System.out.println("部署名称"+deployment.getName());//helloworld入门程序
    }




}
