import { Component, OnInit, Input ,Inject} from '@angular/core';
import { Router, ActivatedRouteSnapshot, NavigationEnd, RoutesRecognized } from '@angular/router';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
import {FileTemplatesService, JustJson, FinalJSONModel } from '../../../entities/file-templates';
import {SourceProfilesService,JSONModel} from '../../../entities/source-profiles';
import { NotificationsService } from 'angular2-notifications-lite';
import {BulkUploadService} from './bulk-upload.service';
import { FileTemplatesPostingData } from '../../../entities/file-templates/fileTemplatesPosting.model';
import { FileSelectDirective, FileDropDirective, FileUploader, FileItem, FileUploaderOptions } from 'ng2-file-upload/ng2-file-upload';
import { RuleGroupService, RuleGroupWithRulesAndConditions } from '../../../entities/rule-group';
import { RulesAndConditions } from '../../../entities/rules';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
declare var $: any;
declare var jQuery: any
const URL = '';

@Component({
    selector: 'bulk-upload-confirm-action-modal',
    templateUrl: 'bulk-upload-confirm-action-modal.html'
})
export class BulkUploadConfirmActionModalDialog {
    constructor(
        public dialogRef: MdDialogRef<BulkUploadConfirmActionModalDialog>,
        public dialog: MdDialog,
        @Inject(MD_DIALOG_DATA) public data: any) {
		dialogRef.disableClose= false;
    }

    onNoClick(): void {
        this.dialogRef.close();
    }
}

@Component({
	selector: 'bulk-upload',
	templateUrl: './bulk-upload.component.html'
})
export class BulkUploadComponent implements OnInit {
	domainAndColumnsList: any = [];
	singleFile: any;
	public isClose: boolean = true;
	isVisibleA: boolean = false;
	public uploaderOptions: FileUploaderOptions;
    public hasBaseDropZoneOver: boolean = false;
    public uploader: FileUploader = new FileUploader({ url: URL });
	constructor(
		private router: Router,
		private fileTemplatesService :FileTemplatesService,
		private sourceProfilesService : SourceProfilesService,
		private notificationsService: NotificationsService,
		private bulkUploadService : BulkUploadService,
		private ruleGroupService:RuleGroupService,
		public dialog: MdDialog
	) {
		this.isClose = true;
	}
	ngOnInit() { 
		console.log('in bulk upload comp')
		
;	}
public fileOverBase(file: File): void {
	if (file)
		this.hasBaseDropZoneOver = true;
	//console.log('hasBaseDropZoneOver'+this.hasBaseDropZoneOver);
	//console.log('uploader?.queue?.length'+this.uploader.queue.length);
}
	domainNameToTableNameMap: any;
	file : String;
	public onFileDrop(filelist: FileList, form?: string): any {
		form='reconprocess';
		// this.fileTemplatesService.bulkUploadTemplates(this.data).map(( res: Response ) => {
		//     let jsonResponse = res.json();
		//    return jsonResponse;
		// } );
		//header validation
		this.domainAndColumnsList = [];
		
		if(form && (form.toLowerCase() === 'reconprocess'))
		{
			if (filelist && filelist.length > 0) {
				let file: File = filelist[0];
				this.file = file.name;
				this.convertFile(file, form);
			}
		}
		else
		if (form && (form.toLowerCase() === 'filetemplates') ){

			this.domainNameToTableNameMap = {};
			this.domainAndColumnsList = [];
			this.domainAndColumnsList.push(this.fileTemplatesService.fileTemplateJSON);
			let domainName = Object.keys(this.fileTemplatesService.fileTemplateJSON)[0];
			this.bulkUploadService.getDomainNameToTableNameMap(domainName).subscribe((res: any) => {
				this.domainNameToTableNameMap[domainName] = res;
				if (this.domainNameToTableNameMap[domainName]) {
					this.domainAndColumnsList.push(this.fileTemplatesService.intermediateJSON);
					domainName = Object.keys(this.fileTemplatesService.intermediateJSON)[0];
					this.bulkUploadService.getDomainNameToTableNameMap(domainName).subscribe((res: any) => {
						this.domainNameToTableNameMap[domainName] = res;
						if (this.domainNameToTableNameMap[domainName]) {
							this.domainAndColumnsList.push(this.fileTemplatesService.rowconditionsJSON);
							domainName = Object.keys(this.fileTemplatesService.rowconditionsJSON)[0];
							this.bulkUploadService.getDomainNameToTableNameMap(domainName).subscribe((res: any) => {
								this.domainNameToTableNameMap[domainName] = res;
								if (this.domainNameToTableNameMap[domainName]) {
									this.domainAndColumnsList.push(this.fileTemplatesService.fileTemplateLinesJSON);
									domainName = Object.keys(this.fileTemplatesService.fileTemplateLinesJSON)[0];
									this.bulkUploadService.getDomainNameToTableNameMap(domainName).subscribe((res: any) => {
										this.domainNameToTableNameMap[domainName] = res;
										if (this.domainNameToTableNameMap[domainName]) {

											if (filelist && filelist.length > 0) {
												let file: File = filelist[0];
												var json = this.convertFile(file, form);
												return json;
											}
										}

									});
								}

							});
						}

					});
				}

			});



		}
		else if (form && (form.toLowerCase() === 'sourceprofiles')) {

			this.domainAndColumnsList = [];
			this.domainNameToTableNameMap = {};
			this.domainAndColumnsList.push(this.sourceProfilesService.sourceProfileJSON);
			// console.log('Object.keys(this.sourceProfilesService.sourceProfileJSON)' + Object.keys(this.sourceProfilesService.sourceProfileJSON));
			let domainName = Object.keys(this.sourceProfilesService.sourceProfileJSON)[0];
			this.bulkUploadService.getDomainNameToTableNameMap(domainName).subscribe((res: any) => {
				this.domainNameToTableNameMap[domainName] = res;
				if (this.domainNameToTableNameMap[domainName]) {
					this.domainAndColumnsList.push(this.sourceProfilesService.sourceProfileFileAssignmentsJSON);
					domainName = Object.keys(this.sourceProfilesService.sourceProfileFileAssignmentsJSON)[0];
					this.bulkUploadService.getDomainNameToTableNameMap(domainName).subscribe((res: any) => {
						this.domainNameToTableNameMap[domainName] = res;
						if (this.domainNameToTableNameMap[domainName]) {
							domainName = Object.keys(this.sourceProfilesService.sourceConnectionDetailsJSON)[0];
							this.bulkUploadService.getDomainNameToTableNameMap(domainName).subscribe((res: any) => {
								this.domainNameToTableNameMap[domainName] = res;
								if (this.domainNameToTableNameMap[domainName]) {
									if (filelist && filelist.length > 0) {
										let file: File = filelist[0];
										this.convertFile(file, form);
									}
								}

							});

						}

					});
				}

			});

		}


		//console.log('domainAndColumnsList'+JSON.stringify(this.domainAndColumnsList));        


	}
	result = [];
	headers = [];
	extractedLinesFromCSV = [];
	convertFile = (file: File, form: string): any => {
		//   const input = document.getElementById('fileInput');

		const reader = new FileReader();
		reader.onload = () => {
			let text = reader.result;
			var json = this.csvJSON(text, form, file);
			return json;
		};
		reader.readAsText(file);
	};

	public csvJSON(csv, form: string, file: File) {
		this.result = [];
		var lines = csv.split("\n");
		this.extractedLinesFromCSV = [];
		var headers = lines[0].split(",");
		// headers.push('refId');
		let linesList: any = [];
		this.headers = headers;
		let previousHeader: string = '';
		var occured: boolean = false;
		let duplicateLineFound: boolean = false;
		let refInd = 0;
		for (var i = 1; i < lines.length;) {
			var currentline = lines[i].split(",");
			var previousline = lines[i - 1].split(",");
			var obj: any = {};
			var line = {};
			line = lines[i].split(",");

			if (i > 1 && previousline && (currentline[0] == previousline[0])) {
				duplicateLineFound = true;
			}
			else {
				duplicateLineFound = false;
				refInd = refInd + 1;

			}
			obj = this.buildJSONFromLine(headers, currentline, previousline, refInd, form);
			//obj['refId']=refInd;
			this.result.push(obj);
			i++;
		}
		this.emptyAll();
		 console.log('this.extractedLinesFromCSV' + JSON.stringify(this.extractedLinesFromCSV));
		// this.buildQueries();
		// console.log('this.result' + JSON.stringify(this.result));
		if (form.toLowerCase() === 'filetemplates') {
			console.log('')
			let tempName = '';
			let count: number = 0;
			if (this.extractedLinesFromCSV[0] && this.extractedLinesFromCSV[0].fileTempDTO && this.extractedLinesFromCSV[0].fileTempDTO.templateName) {
				tempName = this.extractedLinesFromCSV[0].fileTempDTO.templateName;
				count = count + 1;
			}

			for (var i = 1; i < this.extractedLinesFromCSV.length; i++) {
				//console.log('this.extractedLinesFromCSV[i].fileTempDTO.templateName'+this.extractedLinesFromCSV[i].fileTempDTO.templateName);
				//console.log('same=>'+i+'=>'+ this.extractedLinesFromCSV[i].fileTempDTO.templateName.search(tempName));
				if (this.extractedLinesFromCSV[i].fileTempDTO && this.extractedLinesFromCSV[i].fileTempDTO.templateName) {
					if (this.extractedLinesFromCSV[i].fileTempDTO.templateName.toLowerCase().search(tempName.toLowerCase()) != -1) {
						if (this.extractedLinesFromCSV[i].fileTempDTO && this.extractedLinesFromCSV[i].fileTempDTO.excelConditions && this.extractedLinesFromCSV[i].fileTempDTO.excelConditions.skipConditionsList &&
							this.extractedLinesFromCSV[i].fileTempDTO.excelConditions.skipConditionsList.length > 0) {
							if (this.extractedLinesFromCSV[i - 1].skipRowConditionsList && this.extractedLinesFromCSV[i - 1].skipRowConditionsList.length > 0) {
								this.extractedLinesFromCSV[i - 1].skipRowConditionsList.push(this.extractedLinesFromCSV[i].fileTempDTO.excelConditions.skipConditionsList[0]);
							}
							else {
								this.extractedLinesFromCSV[i - 1].skipRowConditionsList = [];
								this.extractedLinesFromCSV[i - 1].skipRowConditionsList.push(this.extractedLinesFromCSV[i].fileTempDTO.excelConditions.skipConditionsList[0]);
							}
						}

						if (this.extractedLinesFromCSV[i].fileTempDTO && this.extractedLinesFromCSV[i].fileTempDTO.excelConditions && this.extractedLinesFromCSV[i].fileTempDTO.excelConditions.endConditionsList && this.extractedLinesFromCSV[i].fileTempDTO.excelConditions.endConditionsList.length > 0) {
							if (this.extractedLinesFromCSV[i - 1].endRowConditionsList && this.extractedLinesFromCSV[i - 1].endRowConditionsList.length > 0) {
								this.extractedLinesFromCSV[i - 1].endRowConditionsList.push(this.extractedLinesFromCSV[i].fileTempDTO.excelConditions.endConditionsList[0]);
							}
							else {
								this.extractedLinesFromCSV[i - 1].endRowConditionsList = [];
								this.extractedLinesFromCSV[i - 1].endRowConditionsList.push(this.extractedLinesFromCSV[i].fileTempDTO.excelConditions.endConditionsList[0]);
							}
						}

						if (this.extractedLinesFromCSV[i].multipleRIList && this.extractedLinesFromCSV[i].multipleRIList.length > 0) {
							if (this.extractedLinesFromCSV[i - 1].multipleRIList && this.extractedLinesFromCSV[i - 1].multipleRIList.length > 0) {
								this.extractedLinesFromCSV[i - 1].multipleRIList.push(this.extractedLinesFromCSV[i].multipleRIList[0]);
							}
							else {
								this.extractedLinesFromCSV[i - 1].multipleRIList = [];
								this.extractedLinesFromCSV[i - 1].multipleRIList.push(this.extractedLinesFromCSV[i].multipleRIList[0]);
							}
						}

						if (this.extractedLinesFromCSV[i].fileTemplateLinesListDTO && this.extractedLinesFromCSV[i].fileTemplateLinesListDTO.length > 0 && this.extractedLinesFromCSV[i].fileTemplateLinesListDTO[0]
							&& this.extractedLinesFromCSV[i].fileTemplateLinesListDTO[0].length > 0) {

							//set record identifier
							if (this.extractedLinesFromCSV[i].fileTemplateLinesListDTO[0]
								&& this.extractedLinesFromCSV[i].fileTemplateLinesListDTO[0][0]
								&& this.extractedLinesFromCSV[i].multipleRIList && this.extractedLinesFromCSV[i].multipleRIList.length > 0 && this.extractedLinesFromCSV[i].multipleRIList[0] && this.extractedLinesFromCSV[i].multipleRIList[0].rowIdentifier) {

								this.extractedLinesFromCSV[i].fileTemplateLinesListDTO[0][0].interRI = this.extractedLinesFromCSV[i].multipleRIList[0].rowIdentifier;


							}
							if (this.extractedLinesFromCSV[i - 1].fileTemplateLinesListDTO && this.extractedLinesFromCSV[i - 1].fileTemplateLinesListDTO.length > 0
								&& this.extractedLinesFromCSV[i - 1].fileTemplateLinesListDTO[0] && this.extractedLinesFromCSV[i - 1].fileTemplateLinesListDTO[0].length > 0) {
								this.extractedLinesFromCSV[i - 1].fileTemplateLinesListDTO[0].push(this.extractedLinesFromCSV[i].fileTemplateLinesListDTO[0][0]);
							}
							else {
								this.extractedLinesFromCSV[i - 1].fileTemplateLinesListDTO = [];
								this.extractedLinesFromCSV[i - 1].fileTemplateLinesListDTO[0].push(this.extractedLinesFromCSV[i].fileTemplateLinesListDTO[0][0]);
							}
						}
						//indexToRemove.push(i);
						this.extractedLinesFromCSV.splice(i, 1);
						i = i - 1;
					}
					else {
						tempName = this.extractedLinesFromCSV[i].fileTempDTO.templateName;
						count = count + 1;
					}

				}
				else {
					this.extractedLinesFromCSV.splice(i, 1);
					i = i - 1;
				}

			}

			// for(var i =1;i<indexToRemove.length;i++)
			// {
			//     this.extractedLinesFromCSV.splice(indexToRemove[i],1);
			// }
			this.notificationsService.info(count + '', ' templates identified');
			this.fileTemplatesService.bulkUploadTemplates(this.extractedLinesFromCSV, file).subscribe((res) => {
				let jsonResponse = res;
				this.bulkUploadResponse = jsonResponse;
				console.log('bulk upload response' + JSON.stringify(this.bulkUploadResponse));
				return jsonResponse //JSON
			});
		}
		else
			
			if(form && (form.toLowerCase() === 'reconprocess'))
			{
				let ruleGRoupName = '';
				let ruleName = '';
				let count: number = 0;
				if (this.extractedLinesFromCSV[0] && this.extractedLinesFromCSV[0].name) {
					ruleGRoupName = this.extractedLinesFromCSV[0].name;
					ruleName =this.extractedLinesFromCSV[0].rules[0].rule.ruleCode;
					count = count + 1;
				}
				//clear object
				for (var i = 1; i < this.extractedLinesFromCSV.length; i++) {
					if (this.extractedLinesFromCSV[i].name) {
						if (this.extractedLinesFromCSV[i].name.toLowerCase()===(ruleGRoupName.toLowerCase()) ) {
							let ruleGroupObject = new RuleGroupWithRulesAndConditions();
							ruleGroupObject = this.extractedLinesFromCSV[i];
							//if rule matches push to rule conditions else push rule
							//console.log("ruleGroupObject.rules[0].rule.ruleCode"+ruleGroupObject.rules[0].rule.ruleCode.toString().toLowerCase().trim()+"ruleName"+ruleName.toString().toLowerCase().trim());
							//console.log("ruleGroupObject.rules[0].rule.ruleCodesearch(ruleName)"+
							ruleGroupObject.rules[0].rule.ruleCode.toString().toLowerCase().trim().search(ruleName.toString().toLowerCase().trim());
							if(ruleGroupObject.rules && ruleGroupObject.rules.length>0 && ruleGroupObject.rules[0].rule.ruleCode.toString().toLowerCase()===(ruleName.toString().toLowerCase()) )
							{
							
								this.extractedLinesFromCSV[i-1].rules[this.extractedLinesFromCSV[i-1].rules.length-1].ruleConditions.push( this.extractedLinesFromCSV[i].rules[0].ruleConditions[0] ) ;
							}
							else
							{
								let rule = new RulesAndConditions();
								rule.rule=this.extractedLinesFromCSV[i].rules[0].rule ;
								rule.ruleConditions=[];
								rule.ruleConditions.push(this.extractedLinesFromCSV[i].rules[0].ruleConditions [0]);
								this.extractedLinesFromCSV[i-1].rules[this.extractedLinesFromCSV[i-1].rules.length] =  rule;
								ruleName =rule.rule.ruleCode;
							}
							this.extractedLinesFromCSV.splice(i, 1);
							i = i - 1;
						}
						else {
							ruleGRoupName = this.extractedLinesFromCSV[i].name;
							ruleName =this.extractedLinesFromCSV[i].rules[0].rule.ruleCode;
							count = count + 1;
						}
	
					}
					else {
						this.extractedLinesFromCSV.splice(i, 1);
						i = i - 1;
					}
					}
					console.log('this.extractedLinesFromCSV ready to save'+JSON.stringify(this.extractedLinesFromCSV));
					this.ruleGroupService.bulkUploadReconcileRules(this.extractedLinesFromCSV,file).subscribe((res) => {
						let jsonResponse = res;
						this.bulkUploadResponse =null;
						this.bulkUploadResponse = jsonResponse;
						console.log('this.bulkUploadResponse '+JSON.stringify(this.bulkUploadResponse));

						//refresh rules list.

						// var dialogRef = this.dialog.open(BulkUploadConfirmActionModalDialog, {
						// 	width: '400px',
						// 	data: { 'errorReports':this.bulkUploadResponse }
						// });
						// dialogRef.afterClosed().subscribe(result => {

						// });
					});
					//call for bulk upload

			}

	}
	bulkUploadResponse: any;
	
	emptyAll() {
		this.level = 0;
		this.queriesList = [];
	}
	queriesList: any = [];
	queryStr = 'INSERT INTO ';
	colsStr = '';
	valuesStr = '';

	buildQueries() {
		this.queriesList = [];
		if (this.extractedLinesFromCSV) {
			for (var i = 0; i < this.extractedLinesFromCSV.length; i++) {
				let obj = this.extractedLinesFromCSV[i];
				this.level = 0;
				this.eachRecursive(obj, i);
			}
		}

		//console.log(' this.queriesList'+JSON.stringify( this.queriesList));
		this.bulkUploadService.postRecords(this.queriesList).subscribe((res: any) => {
		});
	}
	level: number = 0;


	domainToTableNamesMap = {
		"com.nspl.app.domain.SourceProfiles": 't_source_profiles',
		"com.nspl.app.domain.SourceProfileFileAssignments": 't_source_profiles',
		"com.nspl.app.domain.FileTemplates": 't_file_templates',
		"com.nspl.app.domain.FileTemplateLines": 't_file_template_lines',
		"com.nspl.app.domain.RowConditions": 't_row_conditions',
		"com.nspl.app.domain.IntermediateTable": 't_intermediate_table',
		"com.nspl.app.domain.SourceConnectionDetails": 't_source_connection_details'
	}
	eachRecursive(obj, ind) {

		let tableName = '';

		for (var k in obj) {

			if (typeof obj[k] == "object" && obj[k] && obj[k].hasOwnProperty('fetchColumnName')) {
				// domainNameToTableNameMap
				this.colsStr = this.colsStr + this.domainNameToTableNameMap[obj['domainName']][obj[k]['childColumnName']] + ',';
				this.valuesStr = this.valuesStr +
					'(SELECT ' + obj[k]['fetchColumnName']
					+ ' FROM '
					+ this.domainToTableNamesMap[obj[k]['domainName']]
					+ ' WHERE '
					+ this.domainNameToTableNameMap[obj[k]['domainName']][obj[k]['parentColumnName']]
					+ ' = '
					+ "'" + obj[k]['columnValue'] + "'"
					+ ' )' + ',';
			}
			else if (typeof obj[k] == "object" && !obj.hasOwnProperty('fetchColumnName') && obj[k] && obj[k] != null) {

				this.colsStr = this.colsStr.substring(0, this.colsStr.length - 1);
				this.valuesStr = this.valuesStr.substring(0, this.valuesStr.length - 1);
				// console.log('tableName'+tableName);


				if (this.valuesStr && this.valuesStr.length > 0 && this.colsStr && this.colsStr != '' && this.colsStr != ' ' && this.valuesStr != '' && this.valuesStr != ' ') {

					this.queriesList.push(this.queryStr + ' ' + obj['tableName'] + ' (' + this.colsStr + ' ) VALUES ' + '(' + this.valuesStr + ')');

				}
				// skipKeysIfDupFound=false;
				this.valuesStr = '';
				//tableName='';
				this.colsStr = '';
				this.level = this.level + 1;
				this.eachRecursive(obj[k], ind);
			}

			else if (typeof obj[k] != "object" && obj[k] && obj[k] != null && obj[k] != '' && obj[k] != undefined) {
				if (this.level == 0 && this.extractedLinesFromCSV[ind] && this.extractedLinesFromCSV[ind].hasOwnProperty('duplicateWith') && this.extractedLinesFromCSV[ind - 1] &&
					(this.extractedLinesFromCSV[ind - 1][this.extractedLinesFromCSV[ind - 1]['duplicateWith']] === this.extractedLinesFromCSV[ind][this.extractedLinesFromCSV[ind]['duplicateWith']])) {

				}
				else {
					if (k.toLowerCase().search('tablename') != -1) {

						tableName = obj[k];
					}
					else if (k.toLowerCase().search('parenttable') != -1) {
						if (this.domainNameToTableNameMap && this.domainNameToTableNameMap[obj['parentTable']]) {
							let map = this.domainNameToTableNameMap[obj['parentTable']];

							this.colsStr = this.colsStr + this.domainNameToTableNameMap[obj['domainName']][obj['childColumn']] + ',';
							this.valuesStr = this.valuesStr +
								'(SELECT id '
								+ ' FROM '
								+ this.domainToTableNamesMap[obj['parentTable']]
								+ ' WHERE '
								+ this.domainNameToTableNameMap[obj['parentTable']][obj['parentColumn']]
								+ ' = '
								+ "'" + obj['parentColumnValue'] + "'"
								+ ' )' + ',';
						}
					}
					else if (k.toLowerCase().search('refid') == -1
						&& k.toLowerCase().search('domainname') == -1
						&& k.toLowerCase().search('parenttable') == -1
						&& k.toLowerCase().search('parentcolumn') == -1
						&& k.toLowerCase().search('parentcolumnvalue') == -1
						&& k.toLowerCase().search('parentColumnName') == -1
						&& k.toLowerCase().search('fetchColumnName') == -1
						&& k.toLowerCase().search('columnValue') == -1
						&& k.toLowerCase().search('childColumnName') == -1
					) {

						if (this.domainNameToTableNameMap && this.domainNameToTableNameMap[obj['domainName']]) {
							let map = this.domainNameToTableNameMap[obj['domainName']];
							if (map && map[k]) {
								this.colsStr = this.colsStr + map[k] + ',';
								this.valuesStr = this.valuesStr + "'" + obj[k] + "'" + ',';
							}
						}
					}
					if (Object.keys(obj)[Object.keys(obj).length - 1].toLowerCase().search(k.toLowerCase()) != -1) {
						this.colsStr = this.colsStr.substring(0, this.colsStr.length - 1);
						this.valuesStr = this.valuesStr.substring(0, this.valuesStr.length - 1);
						if (this.valuesStr && this.valuesStr.length > 0 && this.colsStr && this.colsStr != '' && this.colsStr != ' ' && this.valuesStr != '' && this.valuesStr != ' ') {
							this.queriesList.push(this.queryStr + ' ' + obj['tableName'] + ' (' + this.colsStr + ' ) VALUES ' + '(' + this.valuesStr + ')');
						}
						this.valuesStr = '';
						this.colsStr = '';
					}
					else {

					}
				}

			}

		}
	}
	recursiveFunctionToBuildChild(obj) {
		//let query = 'INSERT INTO ';
		// query = query + obj.domainName+' (';
		if (typeof obj == 'object') {
			for (var j = 0; j < Object.keys(obj).length; j++) {

				//console.log('type of '+Object.keys(obj)[j]+' is '+ (typeof obj[Object.keys(obj)[j]]));
			}
		}

	}

	buildJSONFromLine(headers, currentline, previousline, refInd, form: string): any {
		var obj = {};
		for (var j = 0; j < headers.length; j++) {
			if (!headers[j] || headers[j] != undefined || headers[j] != null || headers[j] != '') {
				if(form && (form.toLowerCase() === 'reconprocess'))
				{
					let ruleJson = this.ruleGroupService.ruleFinalJson;
					if(ruleJson.hasOwnProperty(headers[j]))
					{
						obj[ruleJson[headers[j]]]= currentline[j];
					}
				}
				// for (var i = 0; i < this.domainAndColumnsList.length; i++) {
				// 	for (var key of Object.keys(this.domainAndColumnsList[i])) {
				// 		let domainObj = this.domainAndColumnsList[i][key][0];
				// 		for (var tempKey in domainObj) {
				// 			if (domainObj.hasOwnProperty(tempKey) && headers[j] == tempKey) {
				// 				if (!obj[domainObj[tempKey]]) {
				// 					obj[domainObj[tempKey]] = currentline[j];
				// 				}
				// 				else {

				// 				}
				// 			}
				// 		}
				// 	}

				// }

			}

		}
		let finalObj: any;
		//console.log('obj'+JSON.stringify(obj));
		if (form.toLowerCase() === 'filetemplates') {
			this.fileTemplatesService.justJson = new JustJson();
			this.fileTemplatesService.justJson = obj;
			//  console.log('obj is '+JSON.stringify( this.fileTemplatesService.justJson));
			finalObj = new FileTemplatesPostingData(this.fileTemplatesService, refInd);
		}
		else if (form.toLowerCase() === 'sourceprofiles') {
			this.sourceProfilesService.justJson = new JustJson();
			this.sourceProfilesService.justJson = obj;
			finalObj = new JSONModel(this.sourceProfilesService, refInd);
		}
		else if(form && (form.toLowerCase() === 'reconprocess')){
			finalObj=obj;	
			this.ruleGroupService.reconcileRuleJSON = finalObj;
			finalObj = new RuleGroupWithRulesAndConditions(this.ruleGroupService);
		}
		//  finalObj['refId']=refInd;
		this.extractedLinesFromCSV.push(finalObj);
		return obj;
	}
	fileChange(file: any) {
        this.onFileDrop(file.files);
    }
}