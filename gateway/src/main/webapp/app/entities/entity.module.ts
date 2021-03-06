import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { AgreeGatewayV1TenantDetailsModule } from './tenant-details/tenant-details.module';
import { AgreeGatewayV1UserMgmtModule } from '../admin/user-management/user-management.module';
import { AgreeGatewayV1RolesModule } from './roles/roles.module';
import { AgreeGatewayV1FunctionalityModule } from './functionality/functionality.module';
import { AgreeGatewayV1FileTemplatesModule } from './file-templates/file-templates.module';
import { AgreeGatewayV1FileTemplateLinesModule } from './file-template-lines/file-template-lines.module';
import { AgreeGatewayV1SourceProfilesModule } from './source-profiles/source-profiles.module';
import { AgreeGatewayV1SourceConnectionDetailsModule } from './source-connection-details/source-connection-details.module';
import { AgreeGatewayV1SourceProfileFileAssignmentsModule } from './source-profile-file-assignments/source-profile-file-assignments.module';
//import { AgreeGatewayV1AccountingDataMappingModule } from './accounting-data-mapping/accounting-data-mapping.module';
import { AgreeGatewayV1AccountingLineTypesModule } from './accounting-line-types/accounting-line-types.module';
import { AgreeGatewayV1DataViewsModule } from './data-views/data-views.module';
import { AgreeGatewayV1DataViewsColumnsModule } from './data-views-columns/data-views-columns.module';
import { AgreeGatewayV1JobsModule } from './jobs/jobs.module';
//import { AgreeGatewayV1LinkedDataViewsModule } from './linked-data-views/linked-data-views.module';
import { AgreeGatewayV1ProcessModule } from './process/process.module';
import { AgreeGatewayV1ProjectModule } from './project/project.module';
import { AgreeGatewayV1RuleConditionsModule } from './rule-conditions/rule-conditions.module';
import { AgreeGatewayV1RuleGroupModule } from './rule-group/rule-group.module';
//import { AgreeGatewayV1RuleGroupDetailsModule } from './rule-group-details/rule-group-details.module';
import { AgreeGatewayV1RulesModule } from './rules/rules.module';
//import { AgreeGatewayV1RuleUsagesModule } from './rule-usages/rule-usages.module';
import { AgreeGatewayV1SourceFileInbHistoryModule } from './source-file-inb-history/source-file-inb-history.module';
//import { AgreeGatewayV1SourceProfileUsagesModule } from './source-profile-usages/source-profile-usages.module';
import { AgreeGatewayV1ReconcileModule } from './reconcile/reconcile.module';
import { AgreeGatewayV1LookUpCodeModule } from './look-up-code/look-up-code.module';
import { AgreeGatewayJobDetailsModule } from './job-details/job-details.module';
import { AgreeGatewaySchedulerDetailsModule } from './scheduler-details/scheduler-details.module';
import { AgreeGatewayApplicationProgramsModule } from './application-programs/application-programs.module';
import { AgreeGatewayV1AcctRuleConditionsModule } from './acct-rule-conditions/acct-rule-conditions.module';
import { AgreeGatewayV1AcctRuleDerivationsModule } from './acct-rule-derivations/acct-rule-derivations.module';
import { AgreeGatewayV1MappingSetModule } from './mapping-set/mapping-set.module';
//import { AgreeGatewayV1MappingSetValuesModule } from './mapping-set-values/mapping-set-values.module';
import { AgreeGatewayV1TemplateDetailsModule } from './template-details/template-details.module';
import { AgreeGatewayV1TemplAttributeMappingModule } from './templ-attribute-mapping/templ-attribute-mapping.module';
import { AgreeGatewayV1LineCriteriaModule } from './line-criteria/line-criteria.module';
import { AgreeGatewayV1JournalsHeaderDataModule } from './journals-header-data/journals-header-data.module';
import { AgreeGatewayV1JeLinesModule } from './je-lines/je-lines.module';
//import { AgreeGatewayV1DataViewsSrcMappingsModule } from './data-views-src-mappings/data-views-src-mappings.module';
import { AgreeGatewayV1ReportsModule } from './reports/reports.module';
//import { AgreeGatewayV1DataViewUnionModule } from './data-view-union/data-view-union.module';
import { AgreeGatewayV1AccountingDataModule } from './accounting-data/accounting-data.module';
import { AgreeGatewayV1JobActionsModule } from './job-actions/job-actions.module';
//import { AgreeGatewayV1ApprovalRuleAssignmentModule } from './approval-rule-assignment/approval-rule-assignment.module';
import { AgreeGatewayV1ApprovalGroupsModule } from './approval-groups/approval-groups.module';
import { AgreeGatewayV1ApprovalGroupMembersModule } from './approval-group-members/approval-group-members.module';
import { AgreeGatewayV1AppRuleConditionsModule } from './app-rule-conditions/app-rule-conditions.module';
import { AgreeGatewayV1NotificationBatchModule } from './notification-batch/notification-batch.module';
import { AgreeGatewayV1FxRatesModule } from './fx-rates/fx-rates.module';
import { AgreeGatewayV1CalendarModule } from './calendar/calendar.module';
//import { AgreeGatewayV1FxRatesDetailsModule } from './fx-rates-details/fx-rates-details.module';
import { AgreeGatewayV1LedgerDefinitionModule } from './ledger-definition/ledger-definition.module';
import { AgreeGatewayV1PeriodsModule } from './periods/periods.module';
import { AgreeGatewayV1ChartOfAccountModule } from './chart-of-account/chart-of-account.module';
import { AgreeGatewayV1SegmentsModule } from './segments/segments.module';
import { AgreeGatewayV1JeLdrDetailsModule } from './je-ldr-details/je-ldr-details.module';
import { AgreeGatewayV1IntermediateTableModule } from './intermediate-table/intermediate-table.module';
//import { AgreeGatewayV1BatchHeaderModule } from './batch-header/batch-header.module';
import {AgreeGatewayV1DataManagementWqModule} from './data-management-wq/data-management-wq.module';
import { AgreeGatewayV1HierarchyModule } from './hierarchy/hierarchy.module';
import { AgreeGatewayV1ExpressModeModule } from './express-mode/express-mode.module';
import { AgreeGatewayV1ProcessesModule } from './processes/processes.module';
import { AgreeGatewayV1DashboardverModule } from './dashboardv2/dashboardver.module'
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        AgreeGatewayV1TenantDetailsModule,
        AgreeGatewayV1UserMgmtModule,
        AgreeGatewayV1RolesModule,
        AgreeGatewayV1FunctionalityModule,
        AgreeGatewayV1FileTemplatesModule,
        AgreeGatewayV1FileTemplateLinesModule,
        AgreeGatewayV1SourceProfilesModule,
        AgreeGatewayV1SourceConnectionDetailsModule,
        AgreeGatewayV1SourceProfileFileAssignmentsModule,
     //   AgreeGatewayV1AccountingDataMappingModule,
        AgreeGatewayV1AccountingLineTypesModule,
        AgreeGatewayV1DataViewsModule,
        AgreeGatewayV1DataViewsColumnsModule,
        AgreeGatewayV1JobsModule,
       // AgreeGatewayV1LinkedDataViewsModule,
        AgreeGatewayV1ProcessModule,
        AgreeGatewayV1ProjectModule,
        AgreeGatewayV1RuleConditionsModule,
        AgreeGatewayV1RuleGroupModule,
       // AgreeGatewayV1RuleGroupDetailsModule,
        AgreeGatewayV1RulesModule,
       // AgreeGatewayV1RuleUsagesModule,
        AgreeGatewayV1SourceFileInbHistoryModule,
        //AgreeGatewayV1SourceProfileUsagesModule,
        AgreeGatewayV1ReconcileModule,
        AgreeGatewayV1LookUpCodeModule,
        AgreeGatewayJobDetailsModule,
        AgreeGatewaySchedulerDetailsModule,
        AgreeGatewayApplicationProgramsModule,
        AgreeGatewayV1AcctRuleConditionsModule,
        AgreeGatewayV1AcctRuleDerivationsModule,
        AgreeGatewayV1MappingSetModule,
        //AgreeGatewayV1MappingSetValuesModule,
        AgreeGatewayV1TemplateDetailsModule,
        AgreeGatewayV1TemplAttributeMappingModule,
        AgreeGatewayV1LineCriteriaModule,
        AgreeGatewayV1JournalsHeaderDataModule,
        AgreeGatewayV1JeLinesModule,
       // AgreeGatewayV1DataViewsSrcMappingsModule,
        AgreeGatewayV1ReportsModule,
      //  AgreeGatewayV1DataViewUnionModule,
        AgreeGatewayV1AccountingDataModule,
        AgreeGatewayV1JobActionsModule,
        //AgreeGatewayV1ApprovalRuleAssignmentModule,
        AgreeGatewayV1ApprovalGroupsModule,
        AgreeGatewayV1ApprovalGroupMembersModule,
        AgreeGatewayV1AppRuleConditionsModule,
        AgreeGatewayV1NotificationBatchModule,
        AgreeGatewayV1FxRatesModule, 
        AgreeGatewayV1CalendarModule,
        //AgreeGatewayV1FxRatesDetailsModule,
        AgreeGatewayV1LedgerDefinitionModule,
        AgreeGatewayV1PeriodsModule,
        AgreeGatewayV1ChartOfAccountModule,
        AgreeGatewayV1SegmentsModule,
        AgreeGatewayV1JeLdrDetailsModule,
        AgreeGatewayV1IntermediateTableModule,
        //AgreeGatewayV1BatchHeaderModule,
        AgreeGatewayV1DataManagementWqModule,
        AgreeGatewayV1HierarchyModule,
        AgreeGatewayV1ExpressModeModule,
        AgreeGatewayV1ProcessesModule, 
        AgreeGatewayV1DashboardverModule
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1EntityModule {}
