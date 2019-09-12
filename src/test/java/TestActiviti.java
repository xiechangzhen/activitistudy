import org.activiti.engine.*;
import org.junit.Test;

public class TestActiviti {
    @Test
    public void TestActiviti(){
//        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
//        //连接数据库的配置
//        processEngineConfiguration.setJdbcDriver("com.mysql.jdbc.Driver");
//        System.out.println("driver");
//        processEngineConfiguration.setJdbcUrl("jdbc:mysql://localhost:3306/mp?useUnicode=true&useSSL=false&characterEncoding=utf8");
//        System.out.println("setUrl");
//        processEngineConfiguration.setJdbcUsername("root");
//        processEngineConfiguration.setJdbcPassword("root");
//        //useSSL=false&serverTimezone=GMT%2B8
//        processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
//        //工作的核心对象，ProcessEngine对象
//        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
//        System.out.println("processEngine = " + processEngine);


    }

    /**
     * 使用配置文件创建工作流需要的23张表
     */
    @Test
    public void createTable(){

        /**
         *
         * RepositoryService 管理流程定义
         * RuntimeService 执行管理，包括启动、推进、删除流程实例等操作，
         * TaskService 任务管理
         * HistoryService 历史管理（执行完的数据的管理）
         * IdentityService 组织机构管理
         * FormService 一个可选服务，任务表单管理
         * ManagerService
         */
        //ProcessEngineConfiguration processEngineConfigurationFromResource = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("act.xml");
        //ProcessEngine processEngine = processEngineConfigurationFromResource.buildProcessEngine();
       // System.out.println("processEngine = " + processEngine);

    }

}
