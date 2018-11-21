package com.nspl.app.config;

import com.nspl.app.security.AuthoritiesConstants;
import com.nspl.app.security.jwt.JWTConfigurer;
import com.nspl.app.security.jwt.TokenProvider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.web.bind.annotation.GetMapping;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class MicroserviceSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;

    public MicroserviceSecurityConfiguration(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers(HttpMethod.OPTIONS, "/**")
            .antMatchers("/app/**/*.{js,html}")
            .antMatchers("/bower_components/**")
            .antMatchers("/i18n/**")
            .antMatchers("/content/**")
            .antMatchers("/swagger-ui/index.html")
            .antMatchers("/test/**")
            .antMatchers("/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
            .disable()
            .headers()
            .frameOptions()
            .disable()
        .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        	/*.httpBasic().and()*/
            .authorizeRequests()
            .antMatchers("/api/updateApprovalWhileInitiating").permitAll()
            .antMatchers("/api/updateActApprovalWhileInitiating").permitAll()
            .antMatchers("/api/websocket/**").authenticated()
            .antMatchers("/api/topic/**").authenticated()
            .antMatchers("/api/postingRecordsInAppModule").permitAll()
            //.antMatchers("/api/insertRecordInHierarchyTable").permitAll()
            .antMatchers("/api/initiateApprovals").permitAll()
            .antMatchers("/api/initiateAccApprovals").permitAll()
            .antMatchers("/api/tokenGenerationForEmailAprovalTask").permitAll()
            .antMatchers("/management/health").permitAll()
            //.antMatchers("/management/metrics").permitAll()
            /*File Templates*/
            .antMatchers("/api/fileTemplatesByTenantId").hasAuthority("FILE_TEMPLATES_LIST")
            .antMatchers("/api/FileTemplatesPostingDTO").hasAnyAuthority("FILE_TEMPLATE_CREATE","FILE_TEMPLATE_EDIT","FILE_TEMPLATE_COPY")
            .antMatchers("/api/file-templates/{id}").hasAuthority("FILE_TEMPLATE_VIEW")
            .antMatchers("/api/fetchIntermediateTablesByTemplateId").hasAuthority("FILE_TEMPLATE_VIEW")
            .antMatchers("/api/fetchTempLinesByTempIdOrIntermediateIds").hasAuthority("FILE_TEMPLATE_VIEW")
            .antMatchers("/api/SPAByTempId").hasAuthority("FILE_TEMPLATE_VIEW")
            .antMatchers("/api/fetchTempLinesByTempId/{templateId}").hasAuthority("FILE_TEMPLATE_VIEW")
            /*Source Profiles*/
            .antMatchers("/api/sourceProfilesByTenantIdwithSort").hasAuthority("SRC_PROFILES_LIST")
            .antMatchers("/api/sourceProfilesAndProfileAssignments").hasAnyAuthority("SRC_PROFILE_CREATE","SRC_PROFILE_EDIT","SRC_PROFILE_COPY")
            .antMatchers("/api/fetchSrcConnectionDetails").hasAuthority("SRC_PROFILE_CREATE")
            .antMatchers("/api/UnAssignedFileTemplatesList").hasAuthority("SRC_PROFILE_CREATE")
            .antMatchers("/api/connectionDetails").hasAuthority("SRC_PROFILE_VIEW")
            .antMatchers("/api/sorceProfilesFileAssignments").hasAuthority("SRC_PROFILE_VIEW")
            /*Source connection details*/
            .antMatchers("/api/sourceConnectionDetailsByTenantId").hasAuthority("SRC_CONNECTIONS_LIST")
            .antMatchers("/api/testSourceConnections").hasAuthority("SRC_CONNECTION_CREATE")
            .antMatchers("/api/connectionsAndDisplayColumns").hasAuthority("SRC_CONNECTION_CREATE")
            .antMatchers("/api/UnAssignedSourceProfiles").hasAuthority("SRC_CONNECTION_CREATE")
            .antMatchers("/api/source-connection-details/{id}").hasAuthority("SRC_CONNECTION_VIEW")
            /*ETL Work Queue*/
            .antMatchers("/api/sourceProfileswithDetailInfo").hasAuthority("ETL_WQ_VIEW")
            .antMatchers("/api/fileTemplateswithStatus").hasAuthority("ETL_WQ_VIEW")
            .antMatchers("/api/batchHeader").hasAuthority("ETL_WQ_VIEW")
            .antMatchers("/api/batchDetailsByBatchId").hasAuthority("ETL_WQ_VIEW")
            .antMatchers("/api/fetchIntermediateTablesByTemplateId").hasAuthority("ETL_WQ_VIEW")
            .antMatchers("/api/dataMasterLinesBySrcFileInb").hasAuthority("ETL_WQ_VIEW")
            .antMatchers("/api/dataStagingLinesBySrcFileInb").hasAuthority("ETL_WQ_VIEW")
            .antMatchers("/api/testOozieStatus").hasAnyAuthority("ETL_EXTRACT","ETL_TRANSFORM","ETL_MANUAL","ROLE_USER","REP_RUN_NEW","REP_RUN_EDIT")
            .antMatchers("/api/jobIntiateForAcctAndRec").hasAnyAuthority("ETL_EXTRACT","ETL_TRANSFORM","ETL_MANUAL")
            .antMatchers("/api/updateDataStagingRecords").hasAuthority("ETL_TRANSFORM")
            .antMatchers("/api/deleteStagingRecords").hasAuthority("ETL_TRANSFORM")
//            .antMatchers("/api/holdTemplate").hasAnyAuthority("ETL_EXTRACT_HOLD","ETL_TRANSFORM_HOLD")
            .antMatchers("/api/holdTemplate").hasAnyAuthority("ETL_HOLD")
            .antMatchers("/api/postingJobDetails").hasAnyAuthority("ETL_WQ_SCHEDULE","ROLE_USER")
            /*Reconciliation Setup Manager*/
            .antMatchers("/api/RuleGroupsWithMeaning/**").hasAnyAuthority("RCN_RG_LIST","ACCNT_RG_LIST")
            .antMatchers("/api/postingRuleGrpAndRulesAndRuleConditions")
            	.hasAnyAuthority("RCN_RG_CREATE","RCN_RG_EDIT","RCN_RG_COPY","RCN_R_CREATE","RCN_R_EDIT","RCN_R_COPY","RCN_R_DELETE")
            .antMatchers("/api/getRuleGroupDetailsObject").hasAnyAuthority("RCN_RG_VIEW","RCN_R_LIST","ACCNT_RG_VIEW","ACCNT_R_LIST")
            .antMatchers("/api/getRuleGrpAndRuleConditionsAndRuleGrpDetails").hasAnyAuthority("RCN_RG_VIEW","RCN_R_LIST")
            .antMatchers("/api/updatePriority").hasAnyAuthority("RCN_R_PRIORITY","ACCNT_R_PRIORITY")
            /*Reconciliation Process Manager*/
            .antMatchers("/api/getReconSummaryByRuleGroup").hasAuthority("RWQ_VIEW")
            .antMatchers("/api/getReconUnReconAmounts").hasAuthority("RWQ_VIEW")
            .antMatchers("/api/getReconColumnAlignmentInfo").hasAuthority("RWQ_VIEW")
            .antMatchers("/api/getReconCountAndAmounts").hasAuthority("RWQ_VIEW")
            .antMatchers("/api/getReconDataByViewIds").hasAuthority("RWQ_VIEW")
            .antMatchers("/api/jobIntiateForAcctAndRec").hasAnyAuthority("RWQ_SCH","RWQ_INIT_ACCNT","RWQ_INIT_APPR","RWQ_SUM_RCN")
            .antMatchers("/api/postManualReconData").hasAuthority("RWQ_DET_RCN")
            .antMatchers("/api/postDataChild").hasAnyAuthority("RWQ_ADD_ROW","RWQ_SPLIT_ROW")
            .antMatchers("/api/processManualUnReconDataAutoAcct").hasAnyAuthority("RWQ_SUMM_UNRCN","RWQ_DET_UNRCN")
            /*Accounting Setup Manager*/
            .antMatchers("/api/postAccountingRuleDefination")
            	.hasAnyAuthority("ACCNT_RG_CREATE","ACCNT_RG_EDIT","ACCNT_RG_COPY","ACCNT_R_CREATE","ACCNT_R_EDIT","ACCNT_R_COPY","ACCNT_R_DELETE")
            .antMatchers("/api/getAccountingRuleDerivation").hasAnyAuthority("ACCNT_RG_VIEW","ACCNT_R_LIST")
            /*Accounting Process Manager*/
            .antMatchers("/api/getAcctSummaryByRuleGroup").hasAnyAuthority("AWQ_VIEW")
            .antMatchers("/api/getAWQStatusesCountsNAmounts").hasAnyAuthority("AWQ_VIEW")
            .antMatchers("/api/getAWQGroupingSummaryInfo").hasAnyAuthority("AWQ_VIEW")
            .antMatchers("/api/getAccountingDetailInfo").hasAnyAuthority("AWQ_VIEW")
            .antMatchers("/api/getAccountedSummaryInfo").hasAnyAuthority("AWQ_VIEW")
            .antMatchers("/api/getAccountingColumnAlignmentInfo").hasAnyAuthority("AWQ_VIEW")
            .antMatchers("/api/getColumnAllignForAccountedSummary").hasAnyAuthority("AWQ_VIEW")
            .antMatchers("/api/jobIntiateForAcctAndRec").hasAnyAuthority("AWQ_SCH","AWQ_RUN_RECON","AWQ_RUN_ACCNT","AWQ_PREP_JE")
            .antMatchers("/api/postDataChild").hasAnyAuthority("AWQ_ADD_ROW","AWQ_SPLIT_ROW")
            .antMatchers("/api/postManualAccData").hasAnyAuthority("AWQ_MANUAL_ACCNT")
            .antMatchers("/api/manualUnAccDataAutoAcc").hasAnyAuthority("AWQ_REV_ACCNT","AWQ_UNDO_ACCNT")
            .antMatchers("/api/getTemplateIdAndNameByDataViewId").hasAnyAuthority("AWQ_PREP_JE")
             /*Reporting Setup Manager*/
            .antMatchers("/api/reportDefination").hasAuthority("REP_DEF_CREATE")
            .antMatchers("/api/getReportsByTenant").hasAuthority("REP_DEF_LIST")
            .antMatchers("/api/getReportDefinations").hasAuthority("REP_DEF_VIEW")
            /*Reporting Process Manager*/
            .antMatchers("/api/TabularViewReportGeneration").hasAnyAuthority("REP_RUN_NEW","REP_RUN_EDIT")
            .antMatchers("/api/TabularViewReportGenerationAsync").hasAnyAuthority("REP_RUN_NEW","REP_RUN_EDIT")
            .antMatchers("/api/reportOutPutExport").hasAnyAuthority("REP_EXPORT")
            .antMatchers("/api/reportRequestByTenantIdOrReqId").hasAnyAuthority("REP_RUN_HIST")
            /*Tenant Details*/
            .antMatchers("/api/createTenantAndDefaultUser").hasAuthority("TENANT_CREATE")
            .antMatchers("/api/tenant-details").hasAnyAuthority("TENANTS_LIST","TENANT_VIEW","TENANT_EDIT")
            /*User management*/
            .antMatchers("/api/usersListWithRolesAndFunctions").hasAuthority("USERS_LIST")
            .antMatchers("/api/userAndUserRoleAssignmentCreation").hasAnyAuthority("USER_CREATE","USER_EDIT")
            .antMatchers("/api/getUserRolesDetailsByUserId").hasAuthority("USER_VIEW")
            
            .antMatchers("/api/**").authenticated()
            .antMatchers("/management/**").hasAnyAuthority(AuthoritiesConstants.ADMIN,"APPL_ADMIN")
            .antMatchers("/swagger-resources/configuration/ui").permitAll()
        .and()
            .apply(securityConfigurerAdapter());
    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }
}
