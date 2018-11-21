import { Component, ElementRef, Input, Output, EventEmitter } from "@angular/core";

@Component({
    selector: "wbr-pivot",
    template: "<div><div class='wbr-ng-wrapper'></div></div>"
})
export class WebDataRocksPivot {
    // params
    @Input() toolbar: boolean;
    @Input() width: string | number;
    @Input() height: string | number;
    @Input() report: WebDataRocks.Report | string;
    @Input() global: WebDataRocks.Report;
    @Input() customizeCell: (cell: WebDataRocks.CellBuilder, data: WebDataRocks.Cell) => void;
    // events
    @Output() cellclick: EventEmitter<WebDataRocks.Cell> = new EventEmitter();
    @Output() celldoubleclick: EventEmitter<WebDataRocks.Cell> = new EventEmitter();
    @Output() dataerror: EventEmitter<Object> = new EventEmitter();
    @Output() datafilecancelled: EventEmitter<Object> = new EventEmitter();
    @Output() dataloaded: EventEmitter<Object> = new EventEmitter();
    @Output() datachanged: EventEmitter<Object> = new EventEmitter();
    @Output() fieldslistclose: EventEmitter<Object> = new EventEmitter();
    @Output() fieldslistopen: EventEmitter<Object> = new EventEmitter();
    @Output() filteropen: EventEmitter<Object> = new EventEmitter();
    @Output() fullscreen: EventEmitter<Object> = new EventEmitter();
    @Output() loadingdata: EventEmitter<Object> = new EventEmitter();
    @Output() loadinglocalization: EventEmitter<Object> = new EventEmitter();
    @Output() loadingreportfile: EventEmitter<Object> = new EventEmitter();
    @Output() localizationerror: EventEmitter<Object> = new EventEmitter();
    @Output() localizationloaded: EventEmitter<Object> = new EventEmitter();
    @Output() openingreportfile: EventEmitter<Object> = new EventEmitter();
    @Output() querycomplete: EventEmitter<Object> = new EventEmitter();
    @Output() queryerror: EventEmitter<Object> = new EventEmitter();
    @Output() ready: EventEmitter<WebDataRocks.Pivot> = new EventEmitter();
    @Output() reportchange: EventEmitter<Object> = new EventEmitter();
    @Output() reportcomplete: EventEmitter<Object> = new EventEmitter();
    @Output() reportfilecancelled: EventEmitter<Object> = new EventEmitter();
    @Output() reportfileerror: EventEmitter<Object> = new EventEmitter();
    @Output() reportfileloaded: EventEmitter<Object> = new EventEmitter();
    @Output() runningquery: EventEmitter<Object> = new EventEmitter();
    @Output() update: EventEmitter<Object> = new EventEmitter();
    @Output() beforetoolbarcreated: EventEmitter<Object> = new EventEmitter();
    @Output() aftergriddraw: EventEmitter<Object> = new EventEmitter();
    @Output() beforegriddraw: EventEmitter<Object> = new EventEmitter();
    // api
    public webDataRocks: WebDataRocks.Pivot;
    // private
    private root: HTMLElement;
    
    /* Additional Added By @RK START */
    public exportMyReport(type: string, exportOptions?: WebDataRocks.ExportOptions, callbackHandler?: Function | string){
        this.webDataRocks.exportTo(type,exportOptions,callbackHandler);
    }

    public refreshPivot(){
        this.webDataRocks.refresh();
    }

    /* Additional Added By @RK END */

    constructor(el: ElementRef) {
        this.root = <HTMLElement>el.nativeElement;
    }

    ngOnInit() {
        this.webDataRocks = window["WebDataRocks"]({
            container: this.root.getElementsByClassName("wbr-ng-wrapper")[0],
            width: this.width,
            height: this.height,
            toolbar: this.toolbar,
            report: this.report,
            global: this.global,
            customizeCell: this.customizeCell,
            cellclick: (cell: WebDataRocks.Cell) => this.cellclick.next(cell),
            celldoubleclick: (cell: WebDataRocks.Cell) => this.celldoubleclick.next(cell),
            dataerror: (event: Object) => this.dataerror.next(event),
            datafilecancelled: (event: Object) => this.datafilecancelled.next(event),
            dataloaded: (event: Object) => this.dataloaded.next(event),
            datachanged: (event: Object) => this.datachanged.next(event),
            fieldslistclose: (event: Object) => this.fieldslistclose.next(event),
            fieldslistopen: (event: Object) => this.fieldslistopen.next(event),
            filteropen: (event: Object) => this.filteropen.next(event),
            fullscreen: (event: Object) => this.fullscreen.next(event),
            loadingdata: (event: Object) => this.loadingdata.next(event),
            loadinglocalization: (event: Object) => this.loadinglocalization.next(event),
            loadingreportfile: (event: Object) => this.loadingreportfile.next(event),
            localizationerror: (event: Object) => this.localizationerror.next(event),
            localizationloaded: (event: Object) => this.localizationloaded.next(event),
            openingreportfile: (event: Object) => this.openingreportfile.next(event),
            querycomplete: (event: Object) => this.querycomplete.next(event),
            queryerror: (event: Object) => this.queryerror.next(event),
            ready: () => this.ready.next(this.webDataRocks),
            reportchange: (event: Object) => this.reportchange.next(event),
            reportcomplete: (event: Object) => this.reportcomplete.next(event),
            reportfilecancelled: (event: Object) => this.reportfilecancelled.next(event),
            reportfileerror: (event: Object) => this.reportfileerror.next(event),
            reportfileloaded: (event: Object) => this.reportfileloaded.next(event),
            runningquery: (event: Object) => this.runningquery.next(event),
            update: (event: Object) => this.update.next(event),
            beforetoolbarcreated: (toolbar: Object) => this.beforetoolbarcreated.next(toolbar),
            aftergriddraw: (event: Object) => this.aftergriddraw.next(event),
            beforegriddraw: (event: Object) => this.beforegriddraw.next(event)
        });
    }
}

interface JQuery {
    WebDataRocks(params: WebDataRocks.Params): WebDataRocks.Pivot;
}

declare namespace WebDataRocks {
    interface Params {
        // params
        toolbar?: boolean;
        width?: string | number;
        height?: string | number;
        report?: Report | string;
        global?: Report;
        customizeCell?: (cell: WebDataRocks.CellBuilder, data: WebDataRocks.Cell) => void;
        // events
        cellclick?: Function;
        celldoubleclick?: Function;
        dataerror?: Function;
        datafilecancelled?: Function;
        dataloaded?: Function;
        datachanged?: Function;
        fieldslistclose?: Function;
        fieldslistopen?: Function;
        filteropen?: Function;
        fullscreen?: Function;
        loadingdata?: Function;
        loadinglocalization?: Function;
        loadingreportfile?: Function;
        localizationerror?: Function;
        localizationloaded?: Function;
        openingreportfile?: Function;
        querycomplete?: Function;
        queryerror?: Function;
        ready?: Function;
        reportchange?: Function;
        reportcomplete?: Function;
        reportfilecancelled?: Function;
        reportfileerror?: Function;
        reportfileloaded?: Function;
        runningquery?: Function;
        update?: Function;
        beforetoolbarcreated?: Function;
        aftergriddraw?: Function;
        beforegriddraw?: Function;
        // other
        container?: Element | string;
    }

    interface Pivot {
        addCalculatedMeasure(measure: Measure): void;
        addCondition(condition: ConditionalFormat): void;
        addJSON(json: Object[]): void;
        clear(): void;
        clearFilter(hierarchyName: string): void;
        closeFieldsList(): void;
        collapseAllData(): void;
        collapseData(hierarchyName: string): void;
        connectTo(object: DataSourceParams, callbackHandler: Function | string): void;
        dispose(): void;
        expandAllData(withAllChildren?: boolean): void;
        expandData(hierarchyName: string): void;
        exportTo(type: string, exportOptions?: ExportOptions, callbackHandler?: Function | string): void;
        getAllConditions(): ConditionalFormat[];
        getAllMeasures(): Measure[];
        getAllHierarchies(): Hierarchy[];
        getCell(rowIdx: number, colIdx: number): Cell;
        getColumns(): Hierarchy[];
        getCondition(id: string): ConditionalFormat;
        getData(options: { slice?: Slice }, callbackHandler: Function | string, updateHandler?: Function | string): void;
        getFilter(hierarchyName: string): FilterItem[];
        getFilterProperties(hierarchyName: string): FilterProperties;
        getFormat(measureName: string): Format;
        getMeasures(): Measure[];
        getMembers(hierarchyName: string, memberName: string, callbackHandler: Function | string): Member[];
        getOptions(): Options;
        getReportFilters(): Hierarchy[];
        getReport(format?: string): Report | string;
        getRows(): Hierarchy[];
        getSelectedCell(): Cell;
        getSort(hierarchyName: string): string;
        load(url: string): void;
        on(eventType: string, handler: Function | string): void;
        off(eventType: string, handler?: Function | string): void;
        open(): void;
        openFieldsList(): void;
        print(options?: PrintOptions): void;
        refresh(): void;
        removeAllCalculatedMeasures(): void;
        removeAllConditions(): void;
        removeCondition(id: string): void;
        removeCalculatedMeasure(uniqueName: string): void;
        removeSelection(): void;
        runQuery(slice: Slice): void;
        save(filename: string, destination: string, callbackHandler?: Function | string, url?: string, embedData?: boolean): string;
        setBottomX(hierarchyName: string, num: number, measureName: string): void;
        setFilter(hierarchyName: string, items: string[], negation?: boolean): void;
        setFormat(format: Format, measureName: string): void;
        setOptions(options: Options): void;
        setReport(report: Report): void;
        setSort(hierarchyName: string, sortName: string, customSorting?: string[]): void;
        setTopX(hierarchyName: string, num: number, measureName: string): void;
        sortValues(axisName: string, type: string, tuple: number[], measureName: string): void;
        updateData(object: DataSourceParams | Object[]): void;
        version: number;
        customizeCell(customizeCellFunction: (cell: CellBuilder, data: Cell) => void): void
    }

    interface Report {
        dataSource?: DataSourceParams;
        slice?: Slice;
        options?: Options;
        conditions?: ConditionalFormat[];
        formats?: Format[];
        tableSizes?: {
            columns?: ColumnSize[],
            rows?: RowSize[]
        }
        localization?: Object | string;
    }

    interface DataSourceParams {
        browseForFile?: boolean;
        data?: Object[];
        dataSourceType?: string;
        fieldSeparator?: string;
        filename?: string;
        ignoreQuotedLineBreaks?: boolean;
        recordsetDelimiter?: string;
        customData?: string;
        hash?: string;
        username?: string;
        password?: string;
    }

    interface Slice {
        columns?: Hierarchy[];
        measures?: Measure[];
        reportFilters?: Hierarchy[];
        rows?: Hierarchy[];
        drills?: {
            drillAll?: boolean,
            columns?: Object[],
            rows?: Object[],
        };
        expands?: {
            expandAll?: boolean,
            columns?: Object[],
            rows?: Object[]
        };
        sorting?: {
            column?: Object,
            row?: Object
        };
    }

    interface Options {
        grid?: {
            showFilter?: boolean,
            showGrandTotals?: string,
            showHeaders?: boolean,
            showHierarchies?: boolean,
            showHierarchyCaptions?: boolean,
            showReportFiltersArea?: boolean,
            showTotals?: boolean,
            title?: string,
            type?: string
        };
        configuratorActive?: boolean;
        configuratorButton?: boolean;
        datePattern?: string;
        dateTimePattern?: string;
        defaultHierarchySortName?: string;
        drillThrough?: boolean;
        editing?: boolean;
        selectEmptyCells?: boolean;
        showAggregations?: boolean;
        showCalculatedValuesButton?: boolean;
        showDefaultSlice?: boolean;
        sorting?: string;
        showAggregationLabels?: boolean;
    }

    interface PrintOptions {
        header?: string;
        footer?: string;
    }

    interface Member {
        caption?: string;
        uniqueName?: string;
        hierarchyName?: string;
        children?: Member[];
        isLeaf?: boolean;
        parentMember?: string;
    }

    interface FilterProperties {
        type?: string;
        members?: FilterItem[];
        quantity?: number;
        measure?: string;
    }

    interface FilterItem {
        caption?: string;
        uniqueName?: string;
        hierarchyName?: string;
    }

    interface Cell {
        columnIndex?: number;
        columns?: any[];
        height?: number;
        hierarchy?: Hierarchy;
        isClassicTotalRow?: boolean;
        isDrillThrough?: boolean;
        isGrandTotal?: boolean;
        isGrandTotalColumn?: boolean;
        isGrandTotalRow?: boolean;
        isTotal?: boolean;
        isTotalColumn?: boolean;
        isTotalRow?: boolean;
        member?: Member;
        width?: number;
        x?: number;
        y?: number;
        label?: string;
        measure?: string;
        rowIndex?: number;
        rows?: any[];
        type?: string;
        value?: number;
    }

    interface ExportOptions {
        filename?: string;
        destinationType?: string;
        excelSheetName?: string;
        footer?: string;
        header?: string;
        pageOrientation?: string;
        showFilters?: boolean;
        url?: string;
    }

    interface Hierarchy {
        caption?: string;
        dimensionName?: string;
        filter?: {
            members?: string[],
            negation?: boolean,
            measure?: string,
            quantity?: number,
            type?: string
        };
        sortName?: string;
        uniqueName?: string;
    }

    interface Measure {
        uniqueName?: string;
        active?: boolean;
        aggregation?: string;
        availableAggregations?: string[];
        caption?: string;
        formula?: string;
        format?: string;
        grandTotalCaption?: string;
    }

    interface ConditionalFormat {
        formula?: string;
        format?: Style;
        formatCSS?: string;
        row?: number;
        column?: number;
        measure?: string;
        hierarchy?: string;
        member?: string;
        isTotal?: number;
    }

    interface Style {
        color?: string;
        backgroundColor?: string;
        backgroundImage?: string;
        borderColor?: string;
        fontSize?: string;
        fontWeight?: string;
        fill?: string;
        textAlign?: string;
        fontFamily?: string;
        width?: number;
        maxWidth?: number;
        height?: number;
        maxHeight?: number;
    }

    interface Format {
        name?: string;
        thousandsSeparator?: string;
        decimalSeparator?: string;
        decimalPlaces?: number;
        maxDecimalPlaces?: number;
        maxSymbols?: number;
        currencySymbol?: string;
        currencySymbolAlign?: string;
        nullValue?: string;
        infinityValue?: string;
        divideByZeroValue?: string;
        textAlign?: string;
    }

    interface ColumnSize {
        width?: number;
        idx?: number;
        tuple?: string[];
        measure?: string;
    }

    interface RowSize {
        height?: number;
        idx?: number;
        tuple?: string[];
        measure?: string;
    }

    interface CellBuilder {
        attr?: Object;
        classes?: string[];
        style?: Object;
        tag?: string;
        text?: string;
        addClass(value?: string): void;
        toHtml(): string;
    }
}