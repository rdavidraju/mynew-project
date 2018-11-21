import { Injectable } from '@angular/core';
import { ReconDashBoardObject, AcctDashBoardObject } from '../shared/constants/constants.values';
import { NotificationsService } from 'angular2-notifications-lite';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
declare function escape(s: string): string;
declare var $: any;
declare var j: number;
declare var a: number;
@Injectable()

export class CommonService {
    public recentBreadCrumbs: any[] = []; //@Rk: Added to handle reverse navigation 
    public tempVarNum = 0;
    public tempVarStr = '';
    public DYNAMIC_THEME: string;
    public DYNAMIC_FONT: string;
    public DYNAMIC_HEIGHT: any;
    public reconDashBoardObject = new ReconDashBoardObject();
    public acctDashBoardObject = new AcctDashBoardObject();
    public sidebarToggler = true;
    public showText = false;
    public currentAccount: any;
    public isRecAllData = true;
    public isAcctAllData = true;
    public reconToRule = false;
    public acctToRule = false;
    public showPaginator = true;
    public showErrorBlock = false;
    public editUserFromTenant = false;
    public editUserFromTenantId: any;
    isFullScreen = true;
    portlet: any;
    currencies = [{ "cc": "AED", "cv": "AED", "cs": "AED" },
    { "cc": "AFN", "cv": "AFN", "cs": "Af" },
    { "cc": "ALL", "cv": "ALL", "cs": "ALL" },
    { "cc": "AMD", "cv": "AMD", "cs": "AMD" },
    { "cc": "ARS", "cv": "ARS", "cs": "AR$" },
    { "cc": "AUD", "cv": "AUD", "cs": "AU$" },
    { "cc": "AZN", "cv": "AZN", "cs": "man." },
    { "cc": "BAM", "cv": "BAM", "cs": "KM" },
    { "cc": "BDT", "cv": "BDT", "cs": "Tk" },
    { "cc": "BGN", "cv": "BGN", "cs": "BGN" },
    { "cc": "BHD", "cv": "BHD", "cs": "BD" },
    { "cc": "BIF", "cv": "BIF", "cs": "FBu" },
    { "cc": "BND", "cv": "BND", "cs": "BN$" },
    { "cc": "BOB", "cv": "BOB", "cs": "Bs" },
    { "cc": "BRL", "cv": "BRL", "cs": "R$" },
    { "cc": "BWP", "cv": "BWP", "cs": "BWP" },
    { "cc": "BYR", "cv": "BYR", "cs": "BYR" },
    { "cc": "BZD", "cv": "BZD", "cs": "BZ$" },
    { "cc": "CAD", "cv": "CAD", "cs": "CA$" },
    { "cc": "CDF", "cv": "CDF", "cs": "CDF" },
    { "cc": "CHF", "cv": "CHF", "cs": "CHF" },
    { "cc": "CLP", "cv": "CLP", "cs": "CL$" },
    { "cc": "CNY", "cv": "CNY", "cs": "CN¥" },
    { "cc": "COP", "cv": "COP", "cs": "CO$" },
    { "cc": "CRC", "cv": "CRC", "cs": "₡" },
    { "cc": "CVE", "cv": "CVE", "cs": "CV$" },
    { "cc": "CZK", "cv": "CZK", "cs": "Kč" },
    { "cc": "DJF", "cv": "DJF", "cs": "Fdj" },
    { "cc": "DKK", "cv": "DKK", "cs": "Dkr" },
    { "cc": "DOP", "cv": "DOP", "cs": "RD$" },
    { "cc": "DZD", "cv": "DZD", "cs": "DA" },
    { "cc": "EEK", "cv": "EEK", "cs": "Ekr" },
    { "cc": "EGP", "cv": "EGP", "cs": "EGP" },
    { "cc": "ERN", "cv": "ERN", "cs": "Nfk" },
    { "cc": "ETB", "cv": "ETB", "cs": "Br" },
    { "cc": "EUR", "cv": "EUR", "cs": "€" },
    { "cc": "GBP", "cv": "GBP", "cs": "£" },
    { "cc": "GEL", "cv": "GEL", "cs": "GEL" },
    { "cc": "GHS", "cv": "GHS", "cs": "GH₵" },
    { "cc": "GNF", "cv": "GNF", "cs": "FG" },
    { "cc": "GTQ", "cv": "GTQ", "cs": "GTQ" },
    { "cc": "HKD", "cv": "HKD", "cs": "HK$" },
    { "cc": "HNL", "cv": "HNL", "cs": "HNL" },
    { "cc": "HRK", "cv": "HRK", "cs": "kn" },
    { "cc": "HUF", "cv": "HUF", "cs": "Ft" },
    { "cc": "IDR", "cv": "IDR", "cs": "Rp" },
    { "cc": "ILS", "cv": "ILS", "cs": "₪" },
    { "cc": "INR", "cv": "INR", "cs": "₹" },
    { "cc": "IQD", "cv": "IQD", "cs": "IQD" },
    { "cc": "IRR", "cv": "IRR", "cs": "IRR" },
    { "cc": "ISK", "cv": "ISK", "cs": "Ikr" },
    { "cc": "JMD", "cv": "JMD", "cs": "J$" },
    { "cc": "JOD", "cv": "JOD", "cs": "JD" },
    { "cc": "JPY", "cv": "JPY", "cs": "¥" },
    { "cc": "KES", "cv": "KES", "cs": "Ksh" },
    { "cc": "KHR", "cv": "KHR", "cs": "KHR" },
    { "cc": "KMF", "cv": "KMF", "cs": "CF" },
    { "cc": "KRW", "cv": "KRW", "cs": "₩" },
    { "cc": "KWD", "cv": "KWD", "cs": "KD" },
    { "cc": "KZT", "cv": "KZT", "cs": "KZT" },
    { "cc": "LBP", "cv": "LBP", "cs": "LB£" },
    { "cc": "LKR", "cv": "LKR", "cs": "SLRs" },
    { "cc": "LTL", "cv": "LTL", "cs": "Lt" },
    { "cc": "LVL", "cv": "LVL", "cs": "Ls" },
    { "cc": "LYD", "cv": "LYD", "cs": "LD" },
    { "cc": "MAD", "cv": "MAD", "cs": "MAD" },
    { "cc": "MDL", "cv": "MDL", "cs": "MDL" },
    { "cc": "MGA", "cv": "MGA", "cs": "MGA" },
    { "cc": "MKD", "cv": "MKD", "cs": "MKD" },
    { "cc": "MMK", "cv": "MMK", "cs": "MMK" },
    { "cc": "MOP", "cv": "MOP", "cs": "MOP$" },
    { "cc": "MUR", "cv": "MUR", "cs": "MURs" },
    { "cc": "MXN", "cv": "MXN", "cs": "MX$" },
    { "cc": "MYR", "cv": "MYR", "cs": "RM" },
    { "cc": "MZN", "cv": "MZN", "cs": "MTn" },
    { "cc": "NAD", "cv": "NAD", "cs": "N$" },
    { "cc": "NGN", "cv": "NGN", "cs": "₦" },
    { "cc": "NIO", "cv": "NIO", "cs": "C$" },
    { "cc": "NOK", "cv": "NOK", "cs": "Nkr" },
    { "cc": "NPR", "cv": "NPR", "cs": "NPRs" },
    { "cc": "NZD", "cv": "NZD", "cs": "NZ$" },
    { "cc": "OMR", "cv": "OMR", "cs": "OMR" },
    { "cc": "PAB", "cv": "PAB", "cs": "B/." },
    { "cc": "PEN", "cv": "PEN", "cs": "S/." },
    { "cc": "PHP", "cv": "PHP", "cs": "₱" },
    { "cc": "PKR", "cv": "PKR", "cs": "PKRs" },
    { "cc": "PLN", "cv": "PLN", "cs": "zł" },
    { "cc": "PYG", "cv": "PYG", "cs": "₲" },
    { "cc": "QAR", "cv": "QAR", "cs": "QR" },
    { "cc": "RON", "cv": "RON", "cs": "RON" },
    { "cc": "RSD", "cv": "RSD", "cs": "din." },
    { "cc": "RUB", "cv": "RUB", "cs": "RUB" },
    { "cc": "RWF", "cv": "RWF", "cs": "RWF" },
    { "cc": "SAR", "cv": "SAR", "cs": "SR" },
    { "cc": "SDG", "cv": "SDG", "cs": "SDG" },
    { "cc": "SEK", "cv": "SEK", "cs": "Skr" },
    { "cc": "SGD", "cv": "SGD", "cs": "S$" },
    { "cc": "SOS", "cv": "SOS", "cs": "Ssh" },
    { "cc": "SYP", "cv": "SYP", "cs": "SY£" },
    { "cc": "THB", "cv": "THB", "cs": "฿" },
    { "cc": "TND", "cv": "TND", "cs": "DT" },
    { "cc": "TOP", "cv": "TOP", "cs": "T$" },
    { "cc": "TRY", "cv": "TRY", "cs": "TL" },
    { "cc": "TTD", "cv": "TTD", "cs": "TT$" },
    { "cc": "TWD", "cv": "TWD", "cs": "NT$" },
    { "cc": "TZS", "cv": "TZS", "cs": "TSh" },
    { "cc": "UAH", "cv": "UAH", "cs": "₴" },
    { "cc": "UGX", "cv": "UGX", "cs": "USh" },
    { "cc": "USD", "cv": "USD", "cs": "$" },
    { "cc": "UYU", "cv": "UYU", "cs": "$U" },
    { "cc": "UZS", "cv": "UZS", "cs": "UZS" },
    { "cc": "VEF", "cv": "VEF", "cs": "Bs.F." },
    { "cc": "VND", "cv": "VND", "cs": "₫" },
    { "cc": "XAF", "cv": "XAF", "cs": "FCFA" },
    { "cc": "XOF", "cv": "XOF", "cs": "CFA" },
    { "cc": "YER", "cv": "YER", "cs": "YR" },
    { "cc": "ZAR", "cv": "ZAR", "cs": "R" },
    { "cc": "ZMK", "cv": "ZMK", "cs": "ZK" }];
    etlRandomColors = [{ backgroundColor: ['#03A9F4', '#689F38', '#DD4477', '#3B3EAC', '#990099', '#FF9900', '#3366CC', '#DC3912', '#316395', '#109618', '#994499', '#22AA99', '#AAAA11', '#6633CC', '#E67300', '#8B0707', '#329262', '#5574A6', '#3B3EAC'] }];
    randomColors = [{ backgroundColor: ['#689F38', '#03A9F4', '#3B3EAC', '#DD4477', '#990099', '#FF9900', '#3366CC', '#DC3912', '#316395', '#109618', '#994499', '#22AA99', '#AAAA11', '#6633CC', '#E67300', '#8B0707', '#329262', '#5574A6', '#3B3EAC'] }];
    stackedRandomColors = [{ backgroundColor: '#689F38' }, { backgroundColor: '#03A9F4' }, { backgroundColor: '#3B3EAC' }, { backgroundColor: '#DD4477' },
    { backgroundColor: '#990099' }, { backgroundColor: '#FF9900' }, { backgroundColor: '#3366CC' }, { backgroundColor: '#DC3912' },
    { backgroundColor: '#316395' }, { backgroundColor: '#109618' }, { backgroundColor: '#994499' }, { backgroundColor: '#22AA99' },
    { backgroundColor: '#AAAA11' }, { backgroundColor: '#6633CC' }, { backgroundColor: '#E67300' }, { backgroundColor: '#8B0707' },
    { backgroundColor: '#329262' }, { backgroundColor: '#5574A6' }, { backgroundColor: '#3B3EAC' }];
    successNotificationSettings = {
        clickToClose: true
    }
    infoNotificationSettings = {
        clickToClose: true,
        timeOut: 2000
    }
    errorNotificationSettings = {
        clickToClose: true,
        timeOut: 15000
    }

    constructor(
        private notificationsService: NotificationsService,
    ) {}

    callFunction() {
        const getViewPort = function() {
            return {
                j: window.innerWidth
                    || document.documentElement.clientWidth
                    || document.body.clientWidth,
                a: window.innerHeight
                    || document.documentElement.clientHeight
                    || document.body.clientHeight
            }
        }

        // handle portlet fullscreen
        $('body').on('click', '.portlet > .portlet-title .fullscreen', function(e) {
            e.preventDefault();
            this.portlet = $(this).closest(".portlet");
            if (this.portlet.hasClass('portlet-fullscreen')) {
                isFullScreen = true;
                $(this).removeClass('on');
                this.portlet.removeClass('portlet-fullscreen');
                $('body').removeClass('page-portlet-fullscreen');
                this.portlet.children('.portlet-body').css('height', 'auto');
                this.portlet.find('.table-responsive').css('max-height', '450px');
            } else {
                isFullScreen = false;
                fullHeight = getViewPort().a -
                    parseInt('', this.portlet.children('.portlet-title').outerHeight()) -
                    parseInt('', this.portlet.children('.portlet-body').css('padding-top')) -
                    parseInt('', this.portlet.children('.portlet-body').css('padding-bottom'));
                tempHeight = fullHeight - 325;
                $(this).addClass('on');
                this.portlet.addClass('portlet-fullscreen');
                $('body').addClass('page-portlet-fullscreen');
                this.portlet.children('.portlet-body').css('height', 'auto');
                this.portlet.find('.table-responsive').css('height', 'auto');
                this.portlet.find('.table-responsive').css('max-height', '');
                this.portlet.find('.scrollTempLines perfect-scrollbar').css('height', tempHeight);
            }
            if ($(this).attr("callback")) {
                const tempEval = eval;
                tempEval($(this).attr("callback"));
            }

        });


    }

    getRouteAnimation(outlet) {
        return outlet.activatedRouteData['breadcrumb'];
    }

    screensize() {
        return {
            width: window.innerWidth
                || document.documentElement.clientWidth
                || document.body.clientWidth,
            height: window.innerHeight
                || document.documentElement.clientHeight
                || document.body.clientHeight
        }
    }

    /**
     * @author Sameer
     * @description export to csv/excel
     * @param data e.g: res
     * @param type e.g: csv/excel
     * @param filename
     */
    exportData(data, type, filename) {
        if (data) {
            const url = window.URL.createObjectURL(data.blob());
            if (navigator.msSaveOrOpenBlob) {
                navigator.msSaveBlob(data, `${filename}.${type == 'excel' ? 'xlsx' : type}`);
            } else {
                const anchor = document.createElement('a');
                anchor.href = url;
                anchor.download = `${filename}.${type == 'excel' ? 'xlsx' : type}`;
                document.body.appendChild(anchor);
                anchor.click();
                document.body.removeChild(anchor);
            }
            window.URL.revokeObjectURL(url);
        }
    }

    JSONToCSVConverter(JSONData, ReportTitle, ShowLabel) {
        //If JSONData is not an object then JSON.parse will parse the JSON string in an Object
        const arrData = typeof JSONData != 'object' ? JSON.parse(JSONData) : JSONData;

        let CSV = '';
        //Set Report title in first row or line

        /* CSV += ReportTitle + '\r\n\n'; */

        //This condition will generate the Label/Header
        if (ShowLabel) {
            let row = "";

            //This loop will extract the label from 1st index of on array
            for (const index in arrData[0]) {
                //Now convert each value to string and comma-seprated
                if (index) {
                    row += index + ',';
                }
            }

            row = row.slice(0, -1);

            //append Label row with line break
            CSV += row + '\r\n';
        }

        //1st loop is to extract each row
        for (let i = 0; i < arrData.length; i++) {
            let row = "";

            //2nd loop will extract each column and convert it in string comma-seprated
            for (const index in arrData[i]) {
                if (index) {
                    row += '"' + arrData[i][index] + '",';
                }
            }

            row.slice(0, row.length - 1);

            //add a line break after each row
            CSV += row + '\r\n';
        }

        if (CSV == '') {
            alert("Invalid data");
            return;
        }

        //Generate a file name
        let fileName = "";
        //this will remove the blank-spaces from the title and replace it with an underscore
        fileName += ReportTitle.replace(/ /g, "_");

        //Initialize file format you want csv or xls
        const uri = 'data:text/csv;charset=utf-8,' + escape(CSV);

        // Now the little tricky part.
        // you can use either>> window.open(uri);
        // but this will not work in some browsers
        // or you will not get the correct file extension    

        //this trick will generate a temp <a /> tag
        const link = document.createElement("a");
        link.href = uri;

        //set the visibility hidden so it will not effect on your web-layout
        // link.style = "visibility:hidden";
        link.download = fileName + ".csv";

        //this part will append the anchor tag and remove it after automatic click
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    }

    /* 
        @Author: Amit
        @Description: Paginator Floating
    */
    addPaginator() {
        this.showPaginator = !this.showPaginator;
        $(".paginator-class").css("background-color", '#efeeee');
        $(".paginator-class").css("white-space", "nowrap");
    }

    /**
     * @author sameer               
     * @description Adding current time to date
     * @param date 
     */
    convertDate(date) {
        if (!date) return;
        const d = new Date();
        d.setDate(date.getDate());
        d.setMonth(date.getMonth());
        d.setFullYear(date.getFullYear());
        return d;
    }

    success(title, content) {
        this.notificationsService.success(title, content, this.successNotificationSettings);
        setTimeout(() => {
            $(".simple-notification").append( "<i class=\"fa fa-close notiCloseIcon\"></i>" )                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
        }, 0);
    }

    error(title, content) {
        this.notificationsService.error(title, content, this.errorNotificationSettings);
        setTimeout(() => {
            $(".simple-notification").append( "<i class=\"fa fa-close notiCloseIcon\"></i>" )                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
        }, 0);
        /*  id=\"notiIconSymbol\" */
        /* $(document).on('click','#notiIconSymbol',function(){
            co
        }) */
        /* setTimeout(() => {
            this.notificationsService.remove();
        }, 2000); */
    }

    info(title, content) {
        this.notificationsService.info(title, content, this.infoNotificationSettings);
        setTimeout(() => {
            $(".simple-notification").append( "<i class=\"fa fa-close notiCloseIcon\"></i>" )                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
        }, 0);
    }
    alert(title, content) {
        this.notificationsService.info(title, content, this.infoNotificationSettings);
        setTimeout(() => {
            $(".simple-notification").append( "<i class=\"fa fa-close notiCloseIcon\"></i>" )                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
        }, 0);
    }

    destroyNotification(){
        this.notificationsService.remove();
    }
    /* testTRE(){
        console.log('called ');
        setTimeout(() => {
            this.notificationsService.remove();
        })
    } */
    
}
export let isFullScreen: boolean;
export let fullHeight: number;
export let tempHeight: number;
export let getViewPort: any;
