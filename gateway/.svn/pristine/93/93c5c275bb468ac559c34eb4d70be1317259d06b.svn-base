import { Component, OnInit,ElementRef,ViewChild,Renderer } from '@angular/core';
import { trigger, style, animate, transition } from '@angular/animations';
import { Router, ActivatedRoute } from '@angular/router';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiLanguageService } from 'ng-jhipster';
import { Response } from '@angular/http';
import { NotificationsService } from 'angular2-notifications-lite';
import { OverlayContainer} from '@angular/cdk/overlay';

import { ProfileService } from '../profiles/profile.service';
import { JhiLanguageHelper, Principal, LoginModalService, LoginService } from '../../shared';
import { NavBarService } from './navbar.service';
import { CommonService } from '../../entities/common.service';
import { VERSION, DEBUG_INFO_ENABLED } from '../../app.constants';
import { SessionStorageService, LocalStorageService } from 'ng2-webstorage';
import { JobDetailsService } from '../../entities/jobs/job-details.service';
import { BaseEntity } from './../../shared';
import { JhiTrackerService,ChatMessage } from './../../shared/tracker/tracker.service';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
declare var $: any;
declare var jQuery: any;

export class FormConfig implements BaseEntity {
    constructor(
        public id?: any,
        public tenantId?: number,
        public formConfig?: string,
        public formLevel?: string,
        public value?: any,
        public createdDate?: any,
        public createdBy?: number,
        public lastUpdatedDate?: any,
        public lastUpdatedBy?: number,
        public userId?: any
    ) {
    }
}

export class formConfigValue {
    constructor(
        public theme?: string,
        public themeMode?: string,
        public font?: string
    ) {

    }
}

@Component({
    selector: 'jhi-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['navbar.scss'],
    animations: [
        trigger('slideInOut', [
            transition(':enter', [
                style({
                    transform: 'translateY(-110%)'
                }),
                animate('300ms')
            ]),
            transition(':leave', [
                animate('300ms', style({
                    transform: 'translateY(-110%)'
                }))
            ])
        ])
    ]
})
export class NavbarComponent implements OnInit {
    @ViewChild('newCommentClick') newCommentClick:ElementRef;
    inProduction: boolean;
    isNavbarCollapsed: boolean;
    languages: any = [];
    swaggerEnabled: boolean;
    modalRef: NgbModalRef;
    isDark: boolean = false;
    version: string;
    schedularList: any = [];
    notificationlist: any = [];
    // currentAccount: any;
    formConfig: string = 'global';
    formLevel: string = 'userPreference';
    type: string = 'user';
    saveConfigurtion: boolean = false;
    saveConfigurtionDontShowAgain: boolean = false;
    themeSidebar:boolean = false;
    notificationSidebar:boolean = false;
    selectedTab:any = 0;
    fonts:any[]=[];
    me:any = {};
    you:any = {};
    avatar:any;
    sendtext: any;
    replyText:any;
    sendmessage : ChatMessage;
    messages : any[]=[];
    isCommentBox:boolean = false;
    isReplyBox:boolean = false;
    isReplyActions:boolean = false;
    isReplyMessages:boolean = false;
    selectedIndex:any;
    commentsSidebar:boolean = false;
    currentColor =  'green';
    firstLetter:any;
    randomColor = ["#d32f2f","#c2185b","#7b1fa2","#512da8","#303f9f","#1976d2","#00796b","#827717","#f57f17","#bf360c","#6d4c41","#455a64","#00c853","#afb42b","#0d47a1","#4a148c","#01579b","#006064","#b71c1c","#880e4f","#304ffe"];
    constructor(
        private loginService: LoginService,
        private languageService: JhiLanguageService,
        public navbarService: NavBarService,
        private languageHelper: JhiLanguageHelper,
        private principal: Principal,
        private loginModalService: LoginModalService,
        private profileService: ProfileService,
        private router: Router,
        private route: ActivatedRoute,
        private $sessionStorage: SessionStorageService,
        private $localStorage: LocalStorageService,
        public cs: CommonService,
        private jobDetailsService: JobDetailsService,
        private notificationsService: NotificationsService,
        public trackerService : JhiTrackerService,
        private renderer: Renderer,
        public overlayContainer: OverlayContainer
    ) {
        this.refreshAccount();
        this.getRandomColor();
        this.selectedTab = 0;
        this.isAuthenticated();
        this.version = VERSION ? 'v' + VERSION : '';
        this.isNavbarCollapsed = true;
        const url = this.route.snapshot.url.map((segment) => segment.path).join('/');
        // console.log('-----------> : '+url);
        // if (!url || (url.trim()).length < 1) {
        //     this.router.navigate(['kpiboard']);
        // }
        /**
         * Execute After Login Successful
         */
        this.navbarService.loginSuccessVar$.subscribe(() => {
            this.getUserPreference();
            this.getAllComments();
            this.loadschedularList();
            setTimeout(() => {
                if(this.cs.currentAccount){
                    if(this.cs.currentAccount['image']){
                        $('.account-picture').css({
                            'background-image': 'url(data:image/png;base64,' + this.cs.currentAccount['image'] + ')',
                            'background-position': '-15px',
                            'background-size' : '125px'
                        });
                    }else if(this.cs.currentAccount.firstName && this.cs.currentAccount.firstName.length>0){
                        this.firstLetter = this.cs.currentAccount.firstName.charAt(0);
                    }
                }
            }, 100);
        });

        /**
         * Execute When font or theme change from user management
         */
        this.navbarService.preferenceChangeVar$.subscribe((data) => {
            this.lastTheme = this.cs.DYNAMIC_THEME;
            if (!data) {
                this.postUserPreference();
            }
        });
        this.fonts = [
            { value: 'josefin-sans', label: 'Josefin Sans', fontFamily: 'josefinOption' },
            { value: 'artifika-serif', label: 'Artifika serif', fontFamily: 'artifikaOption' },
            { value: 'gentium-basic', label: 'Gentium Basic', fontFamily: 'gentiumOption' },
            { value: 'noto-serif', label: 'Noto Serif', fontFamily: 'notoOption' },
            { value: 'pt-serif', label: 'PT Serif', fontFamily: 'ptSerifOption' },
            { value: 'baumans', label: 'Baumans', fontFamily: 'baumansOption' },
            { value: 'play', label: 'Play', fontFamily: 'playOption'},
            { value: 'roboto', label: 'Roboto', fontFamily: 'robotoOption' },
            { value: 'roboto-condensed', label: 'Roboto Condensed', fontFamily: 'robotoCondensedOption' },
            { value: 'pt-sansnarrow', label: 'PT Sans Narrow', fontFamily: 'pTSansNarrowOption' },
            { value: 'exo-2', label: 'Exo 2', fontFamily: 'exo2Option' },
            { value: 'domine', label: 'Domine', fontFamily: 'domineOption'},
            { value: 'istok-web', label: 'Istok Web', fontFamily: 'istokWebOption' },
            { value: 'lato', label: 'Lato', fontFamily: 'latoOption' },
            { value: 'open-sans', label: 'Open Sans', fontFamily: 'openSansOption' }
        ];
    }

        
    
    refreshAccount(){
        this.principal.identity().then((account) => {
            //console.log("Account : "+JSON.stringify(account));
            if(account){
                this.cs.currentAccount = account;
                this.firstLetter = this.cs.currentAccount.firstName.charAt(0);
                this.getUserPreference();
                this.getAllComments();
                this.loadschedularList();
                // const url = this.route.snapshot.url.map((segment) => segment.path).join('/');
                // if(!url || (url.trim()).length<1){
                //     this.router.navigate(['kpiboard']);
                // }
            }
        });
    }

    lastTheme: any;
    switchLogo: boolean = false;
    onThemeChange(theme) {
        if (this.cs.DYNAMIC_THEME != theme) {
            this.switchLogo = theme == 'white' ? true : false;
            this.cs.DYNAMIC_THEME = theme;
            $('body').removeClass(this.lastTheme);
            $('body').addClass(theme);
            this.lastTheme = theme;
            this.displaySaveConfig();
            this.navbarService.preferenceChangeDetect(true);
        }
    }

    getRandomColor() {
        let colorIndex = Math.floor(Math.random() * this.randomColor.length);
        this.currentColor = this.randomColor[colorIndex];
      }
    
    toggleSidebarr(){
        if(this.cs.sidebarToggler == true){
            $('body').addClass('sidebar-static');
            $('.ng-sidebar').removeClass('ng-sidebar-iconWidth');
            $('.ng-sidebar').addClass('ng-sidebar-fullWidth');
            this.cs.showText = true;
            // $('.ng-sidebar').removeClass('ng-sidebar-iconWidth').addClass('ng-sidebar-fullWidth');
            this.cs.sidebarToggler = false;
        } else if(this.cs.sidebarToggler == false){
            $('body').removeClass('sidebar-static');
            $('.ng-sidebar').removeClass('ng-sidebar-fullWidth');
            $('.ng-sidebar').addClass('ng-sidebar-iconWidth');
            this.cs.sidebarToggler = true;
            this.cs.showText = false;
        }
    }

    lastThemeMode: any;
    onThemeModeChange() {
        $('body').removeClass(this.lastThemeMode);
        if (this.isDark) {
            $('body').addClass('dark');
            this.navbarService.publishData('white');
            this.lastThemeMode = 'dark';
        } else {
            $('body').addClass('light');
            this.navbarService.publishData('black');
            this.lastThemeMode = 'light';
        }
        this.displaySaveConfig();
    }

    lastFont: any;
    onFontChange(font) {
        this.cs.DYNAMIC_FONT = font;
        $('body').removeClass(this.lastFont);
        $('body').addClass(font);
        this.lastFont = font;
        this.navbarService.preferenceChangeDetect();
    }

    changeLanguage(languageKey: string) {
        this.languageService.changeLanguage(languageKey);
    }

    collapseNavbar() {
        this.isNavbarCollapsed = true;
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    loadschedularList() {
        let filter = {"scheduledProgram":0,"scheduledType":"All","scheduledStatusList":["SUCCEEDED","RUNNING","SUSPENDED","DONEWITHERROR","FAILED","KILLED","SCHEDULED"]};
        this.navbarService.getSchedulersList(1, 6, filter).subscribe((resp: Response) => {
           
            this.trackerService.schedularList = resp.json();
        });
    }



    loadnotification(e) {
        if (!e || !e.index) {
            this.selectedTab = 0;
        } else {
            this.selectedTab = e.index;
        }
        if(this.selectedTab == 1){
            const isViewedArray = [];
            this.navbarService.getNotificationList().subscribe((res: Response) => {
                this.navbarService.notificationlist = res;
    
                //If any unread notification push in isViewedArray
                for (var i = 0; i < this.navbarService.notificationlist.data.length; i++) {
                    if (this.navbarService.notificationlist.data[i].isViewed == false ||
                        this.navbarService.notificationlist.data[i].isViewed == null) {
                        isViewedArray.push(this.navbarService.notificationlist.data[i].id);
                    }
                }
    
                if (isViewedArray.length > 0) {
                    setTimeout(() => {
                        this.navbarService.notificationisViewed(isViewedArray).subscribe((res: any) => {
                            this.navbarService.getNotificationList().subscribe((res: Response) => {
                                this.navbarService.notificationlist = res;
                                //console.log('isViewedArray ' + JSON.stringify(isViewedArray));
                            });
                        });
                    }, 2000);
                }
            });
        }
    }

    logout() {
        this.navbarService.logout().subscribe((response)=>{});
        this.collapseNavbar();
        this.cs.currentAccount = undefined;
        this.loginService.logout();
        this.router.navigate(['']);
        // location.reload();
    }

    toggleNavbar() {
        this.isNavbarCollapsed = !this.isNavbarCollapsed;
    }

    getImageUrl() {
        return this.isAuthenticated() ? this.cs.currentAccount['image'] : null;
    }

    // getCurrentAccount() {
    //     if (!this.currentAccount) {
    //         this.principal.identity().then((account) => {
    //             this.currentAccount = account;
    //         });
    //     }
    // }

    /**
     * @author sameer
     * @description Retrieve theme, themeMode and font
     */
    userPreference =  new formConfigValue();
    getUserPreference() {
        this.navbarService.getFormConfigDetails(this.formConfig, this.formLevel, this.type).subscribe(
            (formConfig: FormConfig) => {
                if (formConfig && formConfig.value) {
                    this.userPreference = JSON.parse(formConfig.value);
                }
                this.onThemeChange(this.userPreference.theme? this.userPreference.theme : 'blue-skies');
                $('body').removeClass(this.cs.DYNAMIC_FONT);
                this.cs.DYNAMIC_FONT = this.userPreference.font ? this.userPreference.font : 'lato';
                $('body').addClass(this.cs.DYNAMIC_FONT);
                this.isDark = this.userPreference.themeMode == 'dark' ? true : false;
                this.onThemeModeChange();
            },
            err => {
                this.lastTheme = this.cs.DYNAMIC_THEME = 'blue-skies';
                this.cs.DYNAMIC_FONT = 'lato';
                this.lastThemeMode = 'light';
                $('body').addClass('blue-skies light lato');
            }
        );
    }

    postUserPreference() {
        this.saveConfigurtion = false;
        this.userPreference = {
            theme: this.cs.DYNAMIC_THEME,
            themeMode: this.isDark ? 'dark' : 'light',
            font: this.cs.DYNAMIC_FONT
        }
        let postData =  {
            value : JSON.stringify(this.userPreference)
        };
        this.navbarService.postFormConfigDetails(this.formConfig, this.formLevel, this.type, postData).subscribe(
            res => { this.notificationsService.success('Success!', 'User Preferences Saved'); },
            err => { this.notificationsService.error('Warning!', 'Error Occured'); }
        );
        this.$sessionStorage.store('saveConfigurtionDontShowAgain', this.saveConfigurtionDontShowAgain);
    }

    showConfig: boolean = false;
    displaySaveConfig() {
        let dntshow = this.$sessionStorage.retrieve('saveConfigurtionDontShowAgain');
        if (!dntshow) {
            if (this.showConfig) {
                this.saveConfigurtion = true;
                setTimeout(() => {
                    this.saveConfigurtion = false;
                }, 15000);
            }
        }
    }

    showReportOutput(actionVal: String){
        if(actionVal && actionVal.includes(',')){
            this.router.navigate(['/reports', {outlets: {'content': ['run-reports',actionVal.split(',')[0],actionVal.split(',')[1]]}}]);
        }
    }


    ngOnInit() {
        $('body').removeClass('login');
        // this.getCurrentAccount();

        this.languageHelper.getAll().then((languages) => {
            this.languages = languages;
        });

        this.profileService.getProfileInfo().subscribe((profileInfo) => {
            this.inProduction = profileInfo.inProduction;
            this.swaggerEnabled = profileInfo.swaggerEnabled;
        });
        if(this.isAuthenticated()){
            this.getUserPreference();
            this.navbarService.getNotificationList().subscribe((res: Response) => {
                this.navbarService.notificationlist = res;
            });
            this.getAllComments();
            this.loadschedularList();
        }
        //Load Initial - User Notification and jobs schedulers
        if (this.cs.currentAccount) {
            setTimeout(() => {
                //console.log(document.querySelector('.account-picture'));
                if (this.cs.currentAccount['image']) {
                    $('.account-picture').css({
                        'background-image': 'url(data:image/png;base64,' + this.cs.currentAccount['image'] + ')',
                        'background-position': '-15px',
                        'background-size' : '125px'
                    });
                } else {
                    this.firstLetter = this.cs.currentAccount.firstName.charAt(0);
                    // $('.account-picture').attr('data-content', this.cs.currentAccount.firstName.charAt(0));
                }
            }, 100);
        }
    }

    getAllComments(){
        this.navbarService.getAllComments().subscribe((res: any) => {
            this.trackerService.allMessages = res;
            this.trackerService.commentUnReadCount = 0;
            for (let i = 0; i < this.trackerService.allMessages.length; i++) {
                const mess = this.trackerService.allMessages[i];
                mess['isReply'] = false;
                mess['replyText'] = "";
                if(mess.isRead == false){
                    this.trackerService.commentUnReadCount = this.trackerService.commentUnReadCount + 1;
                }
            }
            // console.log('ALl Messages : '+JSON.stringify(this.trackerService.allMessages));
        });
    }

    formatAMPM(date) {
        let hours = date.getHours();
        let minutes = date.getMinutes();
        let ampm = hours >= 12 ? 'PM' : 'AM';
        hours = hours % 12;
        hours = hours ? hours : 12; // the hour '0' should be '12'
        minutes = minutes < 10 ? '0' + minutes : minutes;
        let strTime = hours + ':' + minutes + ' ' + ampm;
        return strTime;
    }

    comments(){
            setTimeout(() => {
                $("#comments_table").animate({scrollTop:$("#comments_table")[0].scrollHeight}, 0)
            }, 100);
    }
    

    resetChat() {
        $("ul.chatul").empty();
    }

    userLetter:any;
    OnKeyDown(text) {
        this.sendmessage = {};
        this.sendmessage.userId = this.cs.currentAccount.id;
        this.sendmessage.subject = null;
        this.sendmessage.recipientUserId = null;
        this.sendmessage.messageBody = text;
        this.sendmessage.isActive = true;
        this.sendmessage.isRead = false;
        this.sendmessage.repliedToCommentId = null;
        this.sendmessage.userName = this.cs.currentAccount['firstName'] + " " + this.cs.currentAccount['lastName'];
        this.sendmessage.image = '';
            if (text !== "") {
                this.navbarService.postComments(this.sendmessage).subscribe((res: any) => {
                    this.sendmessage = res;
                    this.sendmessage['isReply'] = false;
                    this.trackerService.sendActivity(this.sendmessage);
                    this.sendmessage.replyText = "";
                    this.sendtext = '';
                    this.sendmessage = {};
                    // this.closeCommentBox();
                });
                
            }
    }

    sendReply(text, commentId) {
        if (text !== "") {
            this.sendmessage = {};
            this.sendmessage.userId = this.cs.currentAccount.id;
            this.sendmessage.subject = null;
            this.sendmessage.recipientUserId = null;
            this.sendmessage.messageBody = text;
            this.sendmessage.isActive = true;
            this.sendmessage.isRead = false;
            this.sendmessage.repliedToCommentId = commentId;
            this.sendmessage.userName = this.cs.currentAccount['firstName'] + " " + this.cs.currentAccount['lastName'];
            this.sendmessage.image = '';
            this.navbarService.postComments(this.sendmessage).subscribe((res: any) => {
                this.sendmessage = res;
                this.sendmessage['isReply'] = true;
                this.trackerService.sendActivity(this.sendmessage);
                this.sendmessage.replyText = "";
                this.replyText = '';
                this.sendmessage = {};
                this.isReplyActions = false;
            });

        }
    }

    // openCommentBox(){
    //     this.comments();
    //     this.isCommentBox = true;
    //     if($(".jobs-window-new .comment-area").hasClass('comment-area-open')){
    //     } else {
    //         $(".jobs-window-new .comment-area").addClass('comment-area-open');
    //         $(".comments-table #comments_table").css( 'max-height' ,'calc(100vh - 350px)');
    //     }
        
    // }

    // closeCommentBox(){
    //     this.isCommentBox = false;
    //     if($(".jobs-window-new .comment-area").hasClass('comment-area-open')){
    //         $(".jobs-window-new .comment-area").removeClass('comment-area-open');
    //         $(".comments-table #comments_table").css( 'max-height' ,'calc(100vh - 200px)');
    //     }
    //     this.comments();
    // }

    openReplyBox(index){
        this.isReplyBox = true;
        this.isReplyActions = true;
        this.trackerService.allMessages[index].isReply = true;
    }

    closeReplyBox(index){
        this.selectedIndex = index;
        this.isReplyActions = false;
    }

    openReplyActions(index){
        this.selectedIndex = index;
        this.isReplyActions = true;
    }

    postUnReadCom(){
        this.navbarService.postUnReadComments().subscribe((res : any) => {
            this.trackerService.commentUnReadCount = 0;
        });
    }

    deleteComment(commentId,type){
        this.navbarService.deleteCommentService(commentId,type).subscribe((res : any) => {

        });
    }

    toggleThemeSidebar(val) {
        if (val == 'open') {
            this.themeSidebar = true;
            this.overlayContainer.getContainerElement().classList.add('sidebar-zindex');
        } else {
            this.overlayContainer.getContainerElement().classList.remove('sidebar-zindex');
        }
    }


}
