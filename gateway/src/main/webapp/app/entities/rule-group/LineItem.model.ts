import {LineDerivations} from'./line-derivations.model';
import {AccountingRuleConditions} from './accounting-rule-conditions.model';
export class LineItem {
    constructor(
        public id?: number,
        public lineItemType?: string,
        public sourceDataviewName?: string ,
        public sourceDataviewDisplayName?: string ,
        public sourceDataviewId?: string ,
        public columnsForDV ?: any[],
        public startDate?: any,
        public endDate?: any,
        public createdBy?: number,
        public lastUpdatedBy?: number,
        public creationDate?: any,
        public lastUpdatedDate?: any,
        public sameAsAbove ?:boolean,
        public accountingRuleConditions?: AccountingRuleConditions[],
        public accountingRuleDerivations ?: LineDerivations[]
    ) {
        this.columnsForDV = [];
        this.startDate = new Date();
    }
}
export class AccountingLine {
    constructor(
        public id?: number,    
        public ruleId?: number,    
        public lineType?: string,
        public lineTypeDetail?: string,
        public lineTypeDetailMeaning?: string,
        public enteredAmtColId?: any,
        public enteredAmtColName?: any,
        public startDate?: any,
        public endDate?: any,
        public createdBy?: number,
        public lastUpdatedBy?: number,
        public creationDate?: any,
        public lastUpdatedDate?: any    ,
        public accountingRuleConditions?: AccountingRuleConditions[],
        public accountingRuleDerivations ?: LineDerivations[],
        public lineSelectedIndex ?: number
        
    ) {
        this.id = null;
        this.lineType = null;
        this.lineTypeDetail = null;
        this.lineTypeDetailMeaning = null;
        this.enteredAmtColId =null;
        this.enteredAmtColName=null;
        this.endDate = null;
        this.startDate = new Date();
        this.createdBy = null;
        this.lastUpdatedBy = null;
        this.creationDate = null;
        this.lastUpdatedDate = null;
        this.accountingRuleConditions = [];
        this.lineSelectedIndex=0;
       // this.accountingRuleConditions.push(new AccountingRuleConditions());
        this.accountingRuleDerivations = [];
        this.accountingRuleDerivations.push(new LineDerivations());
    }
}