import { Component, Input, OnInit, OnDestroy,ViewChild,Inject } from '@angular/core';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
import { RuleGroupService } from './rule-group.service';
import {RuleGroupAccountingNewComponent} from './rule-group-accounting-new.component';

@Component({
    selector: 'rule-group-recon-confirm-action-modal',
    templateUrl: 'rule-group-recon-confirm-action-modal.html'
})
export class ReconConfirmActionModalDialog {
    selectedTab:number=0;
    constructor(
        public dialogRef: MdDialogRef<ReconConfirmActionModalDialog>,
        public dialog: MdDialog,
        private ruleGroupService :RuleGroupService,
        @Inject(MD_DIALOG_DATA) public data: any) {
            if(this.data.addFilter  == 'source')
            {
                this.data.fromOperands.forEach(op => {
                    if(op.code==this.data.sToleranceOperatorFrom)
                    op.value=true;
                    else
                    op.value=false;
                });   
                this.data.toOperands.forEach(op => {
                    if(op.code==this.data.sToleranceOperatorTo)
                    op.value=true;
                    else
                    op.value=false;
                });  
            }
            else if(this.data.addFilter  == 'target')
            {

                this.data.tFromOperands.forEach(op => {
                    if(op.code==this.data.tToleranceOperatorFrom)
                    op.value=true;
                    else
                    op.value=false;
                });   
                this.data.tToOperands.forEach(op => {
                    if(op.code==this.data.tToleranceOperatorTo)
                    op.value=true;
                    else
                    op.value=false;
                }); 
            }         
    }

    onNoClick(): void {
        this.dialogRef.close();
    }

    selectedFromOperand(operand)
    {
        if(this.data.addFilter  == 'source')
        {
            this.data.fromOperands.forEach(op => {
                
                            if (op.code == operand.code)
                            {
                                //ruleType.value = ruleType.value ? false : true;
                                if(op.value == true)
                                {
                
                                }
                                else{
                                        op.value=true;
                                        this.data.sToleranceOperatorFrom=op.code;
                                }
                            }
                                
                            else
                            {   
                                op.value = false;
                            }
                                
                        });
        }
        else  if(this.data.addFilter  == 'target')
        {
            this.data.tFromOperands.forEach(op => {
                
                            if (op.code == operand.code)
                            {
                                //ruleType.value = ruleType.value ? false : true;
                                if(op.value == true)
                                {
                
                                }
                                else{
                                        op.value=true;
                                        this.data.tToleranceOperatorFrom=op.code;
                                }
                            }
                                
                            else
                            {   
                                op.value = false;
                            }
                                
                        });
        }
       
        
    }
    selectedToOperand(operand)
    {
        if(this.data.addFilter  == 'source')
        {
            this.data.toOperands.forEach(op => {
                
                            if (op.code == operand.code)
                            {
                                //ruleType.value = ruleType.value ? false : true;
                                if(op.value == true)
                                {
                
                                }
                                else{
                                        op.value=true;
                                        this.data.sToleranceOperatorTo=op.code;
                                }
                            }
                                
                            else
                            {   
                                op.value = false;
                            }
                                
                        });
        }
        else  if(this.data.addFilter  == 'target')
        {
            this.data.tToOperands.forEach(op => {
                
                            if (op.code == operand.code)
                            {
                                //ruleType.value = ruleType.value ? false : true;
                                if(op.value == true)
                                {
                
                                }
                                else{
                                        op.value=true;
                                        this.data.tToleranceOperatorTo=op.code;
                                }
                            }
                                
                            else
                            {   
                                op.value = false;
                            }
                                
                        });
        }
      
        
    }
    setPercentile(event)
    {
        console.log('current data object has'+JSON.stringify(this.data));
    }
    blockSpecialChar(e)
    {
        var k = (e.which) ? e.which : e.keyCode;
        if(k >37 || k <37)
        return true;
        else
        return false;
    }
    
}
