package com.fmbank.remote_call.flinkweb.dto;

/**
 * @ClassName: JobAddReq
 * @Description:
 * @author: jc
 * @date: 2021/12/25 18:56
 */
public class JobAddReq {
    private int jobGroup = 1;
    private String jobCron = "0 0 8 * * ?";
    private String jobDesc = "ttttt111";
    private String executorRouteStrategy = "RANDOM";
    private String executorBlockStrategy = "SERIAL_EXECUTION";
    private String childJobId = "";
    private int executorFailRetryCount = 0;
    private String alarmEmail = "";
    private int executorTimeout = 500;
    private int userId = 0;
    private String jobConfigId = "";
    private String executorHandler = "executorJobHandler";
    private String glueType = "BEAN";
    private String glueSource = "";
    private String jobJson = "";
    private String executorParam = "";
    private String replaceParam = "";
    private String replaceParamType = "Timestamp";
    private String jvmParam = "";
    private String incStartTime = "";
    private String partitionInfo = "";
    private int incrementType = 0;
    private String incStartId = "";
    private String primaryKey = "";
    private int projectId = 1;
    private String datasourceId = "";
    private String readerTable = "";

    public int getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(int jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobCron() {
        return jobCron;
    }

    public void setJobCron(String jobCron) {
        this.jobCron = jobCron;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public String getExecutorRouteStrategy() {
        return executorRouteStrategy;
    }

    public void setExecutorRouteStrategy(String executorRouteStrategy) {
        this.executorRouteStrategy = executorRouteStrategy;
    }

    public String getExecutorBlockStrategy() {
        return executorBlockStrategy;
    }

    public void setExecutorBlockStrategy(String executorBlockStrategy) {
        this.executorBlockStrategy = executorBlockStrategy;
    }

    public String getChildJobId() {
        return childJobId;
    }

    public void setChildJobId(String childJobId) {
        this.childJobId = childJobId;
    }

    public int getExecutorFailRetryCount() {
        return executorFailRetryCount;
    }

    public void setExecutorFailRetryCount(int executorFailRetryCount) {
        this.executorFailRetryCount = executorFailRetryCount;
    }

    public String getAlarmEmail() {
        return alarmEmail;
    }

    public void setAlarmEmail(String alarmEmail) {
        this.alarmEmail = alarmEmail;
    }

    public int getExecutorTimeout() {
        return executorTimeout;
    }

    public void setExecutorTimeout(int executorTimeout) {
        this.executorTimeout = executorTimeout;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getJobConfigId() {
        return jobConfigId;
    }

    public void setJobConfigId(String jobConfigId) {
        this.jobConfigId = jobConfigId;
    }

    public String getExecutorHandler() {
        return executorHandler;
    }

    public void setExecutorHandler(String executorHandler) {
        this.executorHandler = executorHandler;
    }

    public String getGlueType() {
        return glueType;
    }

    public void setGlueType(String glueType) {
        this.glueType = glueType;
    }

    public String getGlueSource() {
        return glueSource;
    }

    public void setGlueSource(String glueSource) {
        this.glueSource = glueSource;
    }

    public String getJobJson() {
        return jobJson;
    }

    public void setJobJson(String jobJson) {
        this.jobJson = jobJson;
    }

    public String getExecutorParam() {
        return executorParam;
    }

    public void setExecutorParam(String executorParam) {
        this.executorParam = executorParam;
    }

    public String getReplaceParam() {
        return replaceParam;
    }

    public void setReplaceParam(String replaceParam) {
        this.replaceParam = replaceParam;
    }

    public String getReplaceParamType() {
        return replaceParamType;
    }

    public void setReplaceParamType(String replaceParamType) {
        this.replaceParamType = replaceParamType;
    }

    public String getJvmParam() {
        return jvmParam;
    }

    public void setJvmParam(String jvmParam) {
        this.jvmParam = jvmParam;
    }

    public String getIncStartTime() {
        return incStartTime;
    }

    public void setIncStartTime(String incStartTime) {
        this.incStartTime = incStartTime;
    }

    public String getPartitionInfo() {
        return partitionInfo;
    }

    public void setPartitionInfo(String partitionInfo) {
        this.partitionInfo = partitionInfo;
    }

    public int getIncrementType() {
        return incrementType;
    }

    public void setIncrementType(int incrementType) {
        this.incrementType = incrementType;
    }

    public String getIncStartId() {
        return incStartId;
    }

    public void setIncStartId(String incStartId) {
        this.incStartId = incStartId;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId;
    }

    public String getReaderTable() {
        return readerTable;
    }

    public void setReaderTable(String readerTable) {
        this.readerTable = readerTable;
    }
}
