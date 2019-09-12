
        一：定义工作流
                使用插件完成
                LeaveProcess.bpmn（给计算机进行流程的执行和信息的传递使用的）
                LeaveProcess.png（给用户看的）
        二：执行管理流程工作流
                调用Activiti的API
                activiti.cfg.xml和log4j.properties文件
        *流程引擎ProcessEngine对象（所有的操作都离不开引擎对象
             ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();）
        *由流程引擎创建各个Service，这些Service是调用工作流23张表服务
            3）可以产生RepositoryService
                 RepositoryService repositoryService = processEngine.getRepositoryService();
             4) 可以产生RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
            5)可以产生TaskService
        TaskService taskService = processEngine.getTaskService();

        数据库：
        activiti用来存放流程数据一共使用了23张表都是ACT_
        底层操作默认使用mybatis操作
        总结：工作流Activiti的表使用用来存放流程数据的二业务的数据都需要用户自己创建和维护，
        一定需要业务去关联流程，才能开发工作流系统。

流程定义是不能修改的
控制修改（使用流程定义的key相同的情况下，版本升级，以前的流程走完就不会用了，会用最新的版本）

存在2个流程
    流程1：key=helloworld
         version：1
         version：2
    流程2：key=leaveProcess
        version：1
        version：2
        version：3


##总结：
    Deployment部署对象
    1.一次部署的多个文件的信息，对于不需要的流程可以删除和修改。
    2.对应的表：
    act_re_deployment:部署对象表
    act_re_procdef:流程定义表
    act_ge_bytearray:资源文件表
    act_ge_property：主键生成策略表
    ProcessDefinition流程定义
    1.解析.bpmn后得到的流程定义规则的信息，工作流系统就是按照流程定义的规则执行的
    
    SELECT * FROM act_re_procdef #流程定义表
    SELECT * FROM act_ge_bytearray #资源文件表
    SELECT * FROM act_ge_property #主键生成策略表
#流程实例，执行对象，任务

    SELECT * FROM act_ru_execution #正在执行的执行对象表
    SELECT * FROM act_hi_procinst #流程实例的历史表
    SELECT * FROM act_ru_task #正在执行的任务表（只有节点是UserTask的时候，该表中存在数据）
    SELECT * FROM act_hi_taskinst #任务历史表（只有节点是UserTask的时候，该表中存在数据）
    SELECT * FROM act_hi_actinst #所有活动节点的历史表

#说明：
1）因为是任务查询，所以从processEngine中得到TaskService
2）使用TaskService获取到任务查询对象TaskQuery
3）为查询对象添加过滤条件，使用taskAssignee指定任务的办理者（即查询指定用户
的代办任务），同时可以添加分页排序等过滤条件
4）调用list方法执行查询，返回办理者为指定用户的任务列表
5）任务ID、名称、办理人、创建时间可以从act_ru_task表中得到
6）Execution与ProcessInstance见5.6和5.7章节的介绍。在这种情况下，ProcessInstance相当于Execution
流程实例就是表示一个流程从开始到结束的最大的流程分支。
Exceution：Activiti用这个对象去描述流程执行的每一个节点。在没有并发的情况下，Exceution就是ProcessInstance。
流程按照流程定义的规则执行一次的过程，就可以表示执行对象Exceution
public interface ProcessInstance extends Exceution
流程实例（ProcessInstance）只有一个，但是若内部有分支，并发等，执行对象可以有多个（Execution），
7）如果assignee属性为部门经理，结果为空，因为现在流程只到了“填写请假申请”阶段，
后面的任务还没有执行，即在数据库中没有部门经理可以办理的任务，所以查询不到。
8）一个Task节点和Execution节点是1对1的情况，在task对象中使用Execution_来表示他们之间的关系
9）任务ID在数据库act_ru_task中对应“ID_”列