package com.ch.dataclean.common.kettle;

import com.ch.dataclean.common.exception.KettleDcException;
import org.apache.log4j.Logger;
import org.pentaho.di.cluster.SlaveServer;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.Result;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.core.logging.LoggingBuffer;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobExecutionConfiguration;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.filerep.KettleFileRepository;
import org.pentaho.di.repository.filerep.KettleFileRepositoryMeta;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.pentaho.di.repository.kdr.KettleDatabaseRepositoryMeta;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.www.SlaveServerJobStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;

/**
 * kettle
 */
@Component
public class KettleManager {
    private Logger logger = Logger.getLogger(KettleManager.class);

    @Value("${kettle.filerepository.path}")
    private String KETTLE_REPO_PATH;  //文件资源库绝对路径
    @Value("${kettle.filerepository.id}")
    private String KETTLE_REPO_ID;
    @Value("${kettle.filerepository.name}")
    private String KETTLE_REPO_NAME;
    @Value("${kettle.filerepository.description}")
    private String KETTLE_REPO_DESC;

    @Value("${kettle.log.level}")
    private String KETTLE_LOG_LEVEL; //日志级别
    @Value("${kettle.log.path}")
    private String KETTLE_LOG_PATH;

    //private LogLevel kettle_LogLevel;

    /**
     * 执行文件资源库转换
     * @param transPath 转换路径（相对于资源库）
     * @param transName 转换名称（不需要后缀）
     * @param namedParams 命名参数
     * @param clParams 命令行参数
     */
    public void callTrans(String transPath, String transName, Map<String,String> namedParams, String[] clParams) throws Exception {
        String msg;
        KettleFileRepository repo = this.fileRepositoryCon();
        TransMeta transMeta = this.loadTrans(repo, transPath, transName);
        //转换
        Trans trans = new Trans(transMeta);
        //设置命名参数
        if(null != namedParams) {
            //namedParams.forEach(trans::setParameterValue);
            /*for (Map.Entry<String, String> entry : namedParams.entrySet()) {
                trans.setParameterValue(entry.getKey(), entry.getValue());
            }*/
            for(Iterator<Map.Entry<String, String>> it = namedParams.entrySet().iterator(); it.hasNext();){
                Map.Entry<String, String> entry = it.next();
                trans.setParameterValue(entry.getKey(), entry.getValue());
            }
        }
        trans.setLogLevel(this.getLogerLevel(KETTLE_LOG_LEVEL));
        //执行
        trans.execute(clParams);
        trans.waitUntilFinished();
        //记录日志
        String logChannelId = trans.getLogChannelId();
        LoggingBuffer appender = KettleLogStore.getAppender();
        String logText = appender.getBuffer(logChannelId, true).toString();
        logger.info(logText);
        //抛出异常
        if (trans.getErrors() > 0) {
            msg = "There are errors during transformation exception!(转换过程中发生异常)";
            logger.error(msg);
            throw new KettleDcException(msg);
        }
    }

    /**
     * 执行文件资源库job
     * @param jobName
     * @throws Exception
     */
    public boolean callJob(String jobPath, String jobName, Map<String,String> variables, String[] clParams) throws Exception {
        String msg;
        KettleFileRepository repo = this.fileRepositoryCon();
        JobMeta jobMeta = this.loadJob(repo, jobPath, jobName);
        Job job = new Job(repo, jobMeta);
        //向Job 脚本传递参数，脚本中获取参数值：${参数名}
        if(null != variables) {
            for(Iterator<Map.Entry<String, String>> it = variables.entrySet().iterator(); it.hasNext();){
                Map.Entry<String, String> entry = it.next();
                job.setVariable(entry.getKey(), entry.getValue());
            }
        }
        //设置日志级别
        job.setLogLevel(this.getLogerLevel(KETTLE_LOG_LEVEL));
        job.setArguments(clParams);
        job.start();
        job.waitUntilFinished();
        //记录日志
        String logChannelId = job.getLogChannelId();
        LoggingBuffer appender = KettleLogStore.getAppender();
        String logText = appender.getBuffer(logChannelId, true).toString();
        logger.info(logText);
        if (job.getErrors() > 0) {
            msg = "There are errors during job exception!(执行job发生异常)";
            logger.error(msg);
            throw new KettleDcException(msg);
        }
        return true;
    }

    /**
     * 加载转换
     * @param repo kettle文件资源库
     * @param transPath 相对路径
     * @param transName 转换名称
     */
    private TransMeta loadTrans(KettleFileRepository repo, String transPath, String transName) throws Exception{
        String msg;
        RepositoryDirectoryInterface dir = repo.findDirectory(transPath);//根据指定的字符串路径找到目录
        if(null == dir){
            msg = "kettle资源库转换路径不存在【"+repo.getRepositoryMeta().getBaseDirectory()+transPath+"】！";
            throw new KettleDcException(msg);
        }
        TransMeta transMeta = repo.loadTransformation(repo.getTransformationID(transName, dir), null);
        if(null == transMeta){
            msg = "kettle资源库【"+dir.getPath()+"】不存在该转换【"+transName+"】！";
            throw new KettleDcException(msg);
        }
        return transMeta;
    }

    /**
     * 加载job
     * @param repo kettle文件资源库
     * @param jobPath 相对路径
     * @param jobName job名称
     */
    private JobMeta loadJob(KettleFileRepository repo, String jobPath, String jobName) throws Exception{
        String msg;
        RepositoryDirectoryInterface dir = repo.findDirectory(jobPath);//根据指定的字符串路径找到目录
        if(null == dir){
            msg = "kettle资源库Job路径不存在【"+repo.getRepositoryMeta().getBaseDirectory()+jobPath+"】！";
            throw new KettleDcException(msg);
        }
        JobMeta jobMeta = repo.loadJob(repo.getJobId(jobName, dir), null);
        if(null == jobMeta){
            msg = "kettle资源库【"+dir.getPath()+"】不存在该转换【"+jobName+"】！";
            throw new KettleDcException(msg);
        }
        return jobMeta;
    }

    /**
     * 调用trans文件 带参数的
     */
    public void callNativeTransWithParams(String[] params, String transName) throws Exception {
        /*// 初始化
        KettleEnvironment.init();
        EnvUtil.environmentInit();*/
        TransMeta transMeta = new TransMeta(transName);
        //转换
        Trans trans = new Trans(transMeta);
        //执行
        trans.execute(params);
        //等待结束
        trans.waitUntilFinished();
        //抛出异常
        if (trans.getErrors() > 0) {
            throw new Exception("There are errors during transformation exception!(传输过程中发生异常)");
        }
    }

    /**
     * 调用job文件
     * @param jobName
     * @throws Exception
     */
    public void callNativeJob(String jobName) throws Exception {
        // 初始化
        /*KettleEnvironment.init();*/

        JobMeta jobMeta = new JobMeta(jobName, null);
        Job job = new Job(null, jobMeta);
        //向Job 脚本传递参数，脚本中获取参数值：${参数名}
        //job.setVariable(paraname, paravalue);
        //设置日志级别
        job.setLogLevel(this.getLogerLevel(KETTLE_LOG_LEVEL));
        job.start();
        job.waitUntilFinished();
        if (job.getErrors() > 0) {
            throw new Exception("There are errors during job exception!(执行job发生异常)");
        }
    }

    /**
     * 取得kettle的日志级别
     */
    private LogLevel getLogerLevel(String level) {
        LogLevel logLevel;
        if ("basic".equals(level)) {
            logLevel = LogLevel.BASIC;
        } else if ("detail".equals(level)) {
            logLevel = LogLevel.DETAILED;
        } else if ("error".equals(level)) {
            logLevel = LogLevel.ERROR;
        } else if ("debug".equals(level)) {
            logLevel = LogLevel.DEBUG;
        } else if ("minimal".equals(level)) {
            logLevel = LogLevel.MINIMAL;
        } else if ("rowlevel".equals(level)) {
            logLevel = LogLevel.ROWLEVEL;
        } else if ("nothing".endsWith(level)){
            logLevel = LogLevel.NOTHING;
        }else {
            logLevel = null;
        }
        return logLevel;
    }

    /**
     * 配置kettle文件库资源库环境
     **/
    public KettleFileRepository fileRepositoryCon() throws KettleException {
        String msg;
        //初始化
        /*EnvUtil.environmentInit();
        KettleEnvironment.init();*/

        //资源库元对象
        KettleFileRepositoryMeta fileRepositoryMeta = new KettleFileRepositoryMeta(this.KETTLE_REPO_ID, this.KETTLE_REPO_NAME, this.KETTLE_REPO_DESC, this.KETTLE_REPO_PATH);
        // 文件形式的资源库
        KettleFileRepository repo = new KettleFileRepository();
        repo.init(fileRepositoryMeta);
        //连接到资源库
        repo.connect("", "");//默认的连接资源库的用户名和密码

        if (repo.isConnected()) {
            msg = "kettle文件库资源库【" + KETTLE_REPO_PATH + "】连接成功";
            logger.info(msg);
            return repo;
        } else {
            msg = "kettle文件库资源库【" + KETTLE_REPO_PATH + "】连接失败";
            logger.error(msg);
            throw new KettleDcException(msg);
        }
    }

    /**
     * 配置数据库资源库环境
     *  // TODO 需要用到的时候再完善
     **/
    public KettleDatabaseRepository DBRepositoryCon() throws KettleException {
        //初始化
        /*EnvUtil.environmentInit();
        KettleEnvironment.init();*/
        //数据库连接元对象
        DatabaseMeta dataMeta = new DatabaseMeta("kettle", "MS SQL Server", "Native(JDBC)", "127.0.0.1", "ETL", "1433", "sa", "sa");
        //数据库形式的资源库元对象
        KettleDatabaseRepositoryMeta repInfo = new KettleDatabaseRepositoryMeta();
        //
        repInfo.setConnection(dataMeta);
        //数据库形式的资源库对象
        KettleDatabaseRepository rep = new KettleDatabaseRepository();
        //用资源库元对象初始化资源库对象
        rep.init(repInfo);
        //连接到资源库
        rep.connect("admin", "admin");//默认的连接资源库的用户名和密码
        if (rep.isConnected()) {
            System.out.println("数据库资源库连接成功");
            return rep;
        } else {
            System.out.println("数据库资源库连接失败");
            return null;
        }
    }

    /**
     * 远程执行demo
     * // TODO 需要用到的时候再完善
     */
    public void callRemoteJob(){
        String jobPath = "E:\\ws0815\\xnol-reporting-app-trunk\\etl\\kettle\\jobs\\jb_current_account_order_latest5.kjb";
        try {
            KettleEnvironment.init();

            SlaveServer remoteSlaveServer = new SlaveServer();
            remoteSlaveServer.setHostname("172.20.17.113");// 设置远程IP
            remoteSlaveServer.setPort("8081");// 端口
            remoteSlaveServer.setUsername("cluster");
            remoteSlaveServer.setPassword("cluster");
            FileSystemResource r = new FileSystemResource(jobPath);
            // jobname 是Job脚本的路径及名称
            JobMeta jobMeta = new JobMeta(r.getInputStream(), null, null);

            JobExecutionConfiguration jobExecutionConfiguration = new JobExecutionConfiguration();
            jobExecutionConfiguration.setRemoteServer(remoteSlaveServer);// 配置远程服务

            String lastCarteObjectId = Job.sendToSlaveServer(jobMeta, jobExecutionConfiguration, null, null);
            System.out.println("lastCarteObjectId=" + lastCarteObjectId);
            SlaveServerJobStatus jobStatus = null;
            do {
                Thread.sleep(5000);
                jobStatus = remoteSlaveServer.getJobStatus(jobMeta.getName(), lastCarteObjectId, 0);
            } while (jobStatus != null && jobStatus.isRunning());
            Result oneResult = new Result();
            System.out.println(jobStatus);
            if (jobStatus.getResult() != null) {
                // 流程完成，得到结果
                oneResult = jobStatus.getResult();
                System.out.println("Result:" + oneResult);
            } else {
                System.out.println("取到空了");
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
