import { BaseEntity } from './../../shared';
import { JhiDateUtils } from 'ng-jhipster';
import { FormControl } from '@angular/forms';
import { FileTemplatesService } from './file-templates.service';
export class ExcelConditions implements BaseEntity {
    constructor(
        public ftlService?: FileTemplatesService,
        public id?: number,
        public formCntrl?: FormControl,
        public columnsList?: any[],
        public columnHeadings?: any[],
        public endConditionsList?: Condition[],
        public skipConditionsList?: Condition[]
    ) {
        this.columnsList = [];
        //  this.columnsList.push('');
        this.formCntrl = new FormControl('');
        if(this.ftlService == null)
        {
            this.columnHeadings = [{
                'class': 'col-md-3',
                'name': 'Column'
            },
            {
                'class': 'col-md-2',
                'name': 'Operator'
            },
            {
                'class': 'col-md-3',
                'name': 'Value'
            },
            {
                'class': 'col-md-2',
                'name': 'Logical Operator'
            }
            ];
        }
       
        this.endConditionsList = [];
        //  this.endConditionsList.push(new Condition('','',''));
        this.skipConditionsList = [];
        this.endConditionsList = [];
        if (ftlService != null) {
            let endCondition = new Condition(this.ftlService);
            this.endConditionsList.push(endCondition);
            let skipCondition = new Condition(this.ftlService);
            this.skipConditionsList.push(skipCondition);
            this.ftlService = null;
        }
        this.formCntrl = null;
        //this.skipConditionsList.push(new Condition('','',''));
    }
}
export class Condition {
    constructor(
        public ftlService?: FileTemplatesService,
        public columnName?: string,
        public type?: string,
        // public columnList ?: string[],
        public operator?: string,
        //  public OperatorsList ?: string[],
        public value?: string,
        public logicalOperator?: string
    ) {
        if (this.ftlService && this.ftlService.justJson.templateColumnName)
            this.columnName = this.ftlService.justJson.templateColumnName;
        if (this.ftlService && this.ftlService.justJson.type)
            this.type = this.ftlService.justJson.type;
        if (this.ftlService && this.ftlService.justJson.operator)
            this.operator = this.ftlService.justJson.operator;
        if (this.ftlService && this.ftlService.justJson.value)
            this.value = this.ftlService.justJson.value;
        if (this.ftlService && this.ftlService.justJson.logicalOperator)
            this.logicalOperator = this.ftlService.justJson.logicalOperator;
        this.ftlService = null;
    }
}
