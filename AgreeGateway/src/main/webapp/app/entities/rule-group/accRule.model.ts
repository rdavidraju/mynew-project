import {LineItem} from './LineItem.model';
export class AccRule {
    constructor(
        public id?: number,
        public tenantId?: number,
        public ruleName?: string,
        public ruleType?:string,
        public ruleCode?: any,
        public startDate?: any,
        public endDate?: any,
        public enabledFlag?: boolean,
        public createdBy?: number,
        public lastUpdatedBy?: number,
        public creationDate?: any,
        public lastUpdatedDate?: any,
        public priority?: any,
        public coa?: any,
        public ruleGroupAssignId?:number,
        public lineItems?: LineItem[],
        public assignmentFlag?: boolean,
        public purpose?: any,
    ) {
        this.enabledFlag = false;
        this.ruleCode ='';
        this.coa='';
        this.startDate = new Date();
    }
}



import {LineDerivations} from'./line-derivations.model';
import {AccountingLine} from './LineItem.model';
export class AccountingRule {
    constructor(
        public id?: number,
        public tenantId?: number,
        public ruleName?: string,
        public ruleType?:string,
        public ruleCode?: any,
        public startDate?: any,
        public endDate?: any,
        public enabledFlag?: boolean,
        public createdBy?: number,
        public creationDate?: any,
        public lastUpdatedDate?: any,
        public lastUpdatedBy?: number,
        public priority?: any,
        public coa?: any,
        public coaMeaning?:any,
        public sourceDataViewId ?: any,
        public sourceDVId ?: any,
        public reconDataSourceId?:any,
        public reconSourceDVId ?: any,
        public reconDataSourceName?:any,
        public sourceDataViewName ?: string,
        public enterCurrencyColId ?: any,
        public enterCurrencyColName?:any,
        public ruleGroupAssignId?:number,        
        public assignmentFlag?: boolean,
        public headerDerivationRules ?: LineDerivations[],
        public lineDerivationRules ?: AccountingLine[],
        public debitLines ?: AccountingLine[],
        public creditLines ?: AccountingLine[],
        public startDateChange ?:boolean,
            public endDateChange ?: boolean
    ) {
        this.id =null;
        this.tenantId =null;
        this.ruleName =null;
        this.ruleType =null;
        this.startDate = new Date();
        this.endDate= null;
        this.enabledFlag = false;
        this.createdBy = null;
        this.creationDate =null;
        this.lastUpdatedDate=null;
        this.lastUpdatedBy= null;
        this.priority =null;
        this.ruleCode =null;
        this.coa='';
        this.coaMeaning =null;
        this.reconSourceDVId=null;
        this.sourceDataViewId=null;
        this.reconDataSourceId=null;
        this.sourceDataViewName =null;
        this.enterCurrencyColId=null;
        this.enterCurrencyColName=null;
        this.ruleGroupAssignId =null;
        this.assignmentFlag =null;
        this.assignmentFlag =null;
        this.headerDerivationRules = [];
        this.startDateChange=false;
        this.endDateChange=false;
        //this.headerDerivationRules = null;
        this.lineDerivationRules=[];
        //this.lineDerivationRules.push(new AccountingLine()) ;
        this.debitLines= [];
        this.creditLines =[];
    }
}
