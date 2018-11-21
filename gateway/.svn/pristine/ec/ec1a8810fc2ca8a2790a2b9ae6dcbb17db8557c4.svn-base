import { Injectable } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { Observable, Observer, Subscription } from 'rxjs/Rx';

import { CSRFService } from '../auth/csrf.service';
import { WindowRef } from './window.service';
import { AuthServerProvider } from '../auth/auth-jwt.service';
import { SessionStorageService } from 'ng2-webstorage';
import { CommonService } from '../../entities/common.service';
import { NotificationsService } from 'angular2-notifications-lite';
import { JobsSchedules } from '../../entities/jobs/jobs.model';

import * as SockJS from 'sockjs-client';
import * as Stomp from 'webstomp-client';

export class ChatMessage {
    constructor(
        public id?: any,
        public userId?: any,
        public subject?: string,
        public messageBody?: string,
        public repliedToCommentId?: any,
        public userName?: string,
        public image?: string,
        public isRead?: boolean,
        public isActive?: boolean,
        public createdBy?: any,
        public creationDate?: string,
        public lastUpdatedDate?: string,
        public lastUpdatedBy?: any,
        public recipientUserId?: any,
        public replies?: any[],
        public isReply?: boolean,
        public replyText?: string

    ) {
        this.id = undefined;
        this.userId = undefined;
        this.subject = "";
        this.messageBody = "";
        this.repliedToCommentId = undefined;
        this.userName = "";
        this.image = "";
        this.isRead = false;
        this.isActive = false;
        this.createdBy = undefined;
        this.creationDate = "";
        this.lastUpdatedDate = "";
        this.lastUpdatedBy = undefined;
        this.recipientUserId = undefined;
        this.replies = [];
        this.isReply = false;
        this.replyText = "";

    }
}


@Injectable()
export class JhiTrackerService {
    stompClient = null;
    commentSubscriber = null;
    jobsSubscriber = null;
    connection: Promise<any>;
    connectedPromise: any;
    commentlistener: Observable<any>;
    jobslistener: Observable<any>;
    commentListenerObserver: Observer<any>;
    jobsListenerObserver: Observer<any>;
    alreadyConnectedOnce = false;
    socketMessage: ChatMessage;
    allMessages = new Array<ChatMessage>();
    httpHeaders: any;
    input: any = {};
    private subscription: Subscription;
    commentUnReadCount: number = 0;
    schedularList = new Array<JobsSchedules>();

    constructor(
        private router: Router,
        private authServerProvider: AuthServerProvider,
        private $window: WindowRef,
        private csrfService: CSRFService,
        private cs: CommonService,
        private notificationService: NotificationsService
    ) {
        this.connection = this.createConnection();
        this.commentlistener = this.createCommentListener();
        this.jobslistener = this.createJobsListener();
        this.allMessages = [];
        // this.schedularList =[];
        this.commentUnReadCount = 0;
        for (let i = 0; i < this.allMessages.length; i++) {
            this.allMessages[i].replies = [];
        }
    }

    connect() {
        if (this.connectedPromise === null) {
            this.connection = this.createConnection();
        }

        // building absolute path so that websocket doesn't fail when deploying with a context path
        const loc = this.$window.nativeWindow.location;
        let url;
        url = loc.origin + '/' + 'agreeapplication/websocket/tracker';
        const authToken = this.authServerProvider.getToken();
        const socket = new SockJS(url);
        // console.log('Socket URL :'+JSON.stringify(socket));
        this.stompClient = Stomp.over(socket, { debug: false });
        this.httpHeaders = {};
        this.httpHeaders = {
            'Authorization': 'Bearer ' + authToken,
            'X-XSRF-TOKEN': this.csrfService.getCSRF('XSRF-TOKEN')
        };
        this.stompClient.connect(this.httpHeaders, () => {
            // console.log("Stomp Client : "+JSON.stringify(this.stompClient));
            this.connectedPromise('success');
            this.connectedPromise = null;
            this.subscribeComments();
            this.subscribeJobSubmissions();
            this.receiveComments().subscribe((activity) => {
                // console.log('trackercom ' + JSON.stringify(activity));
                let replen: number = 0;
                this.commentUnReadCount = 0;
                if (this.allMessages.length > 0) {
                    for (let i = 0; i < this.allMessages.length; i++) {
                        const allMes = this.allMessages[i];
                        allMes.replyText = "";
                        if (allMes.id == activity.repliedToCommentId) {
                            replen = replen + 1;
                            if (allMes.replies) {
                                allMes.replies.push(activity);
                            } else {
                                allMes.replies = [];
                                allMes.replies.push(activity);
                            }

                        }
                        if (allMes.isRead == false) {
                            this.commentUnReadCount = this.commentUnReadCount + 1;
                        }

                    }
                    if (replen == 0) {
                        this.allMessages.push(activity);
                    }
                } else {
                    this.allMessages.push(activity);
                }
                if (activity['userId'] != this.cs.currentAccount['id']) {
                    this.notificationService.info(
                        '',
                        activity['userName'] + ": " + activity['messageBody'],
                        {
                            timeOut: 3000,
                            showProgressBar: false,
                            pauseOnHover: true,
                            clickToClose: true,
                            maxLength: 100
                        }
                    )
                }
            });
            this.receiveJobSchedules().subscribe((jobActivity) => {
                // console.log('job schedules ' + JSON.stringify(jobActivity));
                // let replen: number = 0;
                // this.commentUnReadCount = 0;
                this.schedularList.push(jobActivity);
                this.notificationService.info(
                    "",
                    " Submitted " + jobActivity['programName'] + " Job",
                    {
                        timeOut: 3000,
                        showProgressBar: false,
                        pauseOnHover: true,
                        clickToClose: true,
                        maxLength: 100
                    }
                )
            });
            if (!this.alreadyConnectedOnce) {
                this.subscription = this.router.events.subscribe((event) => {
                    // console.log('this.subscription ' + this.subscription);
                    //   if (event instanceof NavigationEnd) {
                    //     this.sendActivity();
                    //   }
                });
                this.alreadyConnectedOnce = true;
            }
        });
    }

    disconnect() {
        if (this.stompClient !== null) {
            this.stompClient.disconnect();
            this.stompClient = null;
        }
        if (this.subscription) {
            this.subscription.unsubscribe();
            this.subscription = null;
        }
        this.alreadyConnectedOnce = false;
    }

    receiveComments() {
        return this.commentlistener;
    }

    receiveJobSchedules() {
        return this.jobslistener;
    }

    sendActivity(inputData: any) {
        if (this.stompClient !== null && this.stompClient.connected && this.cs.currentAccount && this.cs.currentAccount.tenantIdForDisplay) {
            this.stompClient.send('/websocket/comments/' + this.cs.currentAccount.tenantIdForDisplay,// destination
                JSON.stringify(inputData),  // body
                {} // header
            );
        }
    }

    subscribeComments() {
        if (this.cs.currentAccount && this.cs.currentAccount.tenantIdForDisplay) {
            this.connection.then(() => {
                this.commentSubscriber = this.stompClient.subscribe('/websocket/subscribecomments/' + this.cs.currentAccount.tenantIdForDisplay, (data) => {
                    this.socketMessage = JSON.parse(data.body);
                    if (this.commentListenerObserver) {
                        this.commentListenerObserver.next(JSON.parse(data.body));
                        // console.log('data 2' + JSON.stringify(JSON.parse(data.body)));
                    }
                });
            });
        }
    }

    subscribeJobSubmissions() {
        if (this.cs.currentAccount && this.cs.currentAccount.tenantIdForDisplay) {
            this.connection.then(() => {
                this.jobsSubscriber = this.stompClient.subscribe('/websocket/notifications/' + this.cs.currentAccount.tenantIdForDisplay, (jobData) => {
                    // console.log("Jobs Schedules : "+JSON.stringify(jobData));
                    //    this.socketMessage = JSON.parse(data.body);
                    if (this.jobsListenerObserver) {
                        this.jobsListenerObserver.next(JSON.parse(jobData.body));
                        // console.log('data 2' + JSON.stringify(JSON.parse(jobData.body)));
                    }
                });
            });
        }
    }

    unsubscribe() {
        if (this.commentSubscriber !== null) {
            this.commentSubscriber.unsubscribe();
        }
        if (this.jobsSubscriber !== null) {
            this.jobsSubscriber.unsubscribe();
        }
        this.commentlistener = this.createCommentListener();
        this.jobslistener = this.createJobsListener();
    }

    private createCommentListener(): Observable<any> {
        return new Observable(observer => {
            this.commentListenerObserver = observer;
        });
    }

    private createJobsListener(): Observable<any> {
        return new Observable(observer => {
            this.jobsListenerObserver = observer;
        });
    }

    private createConnection(): Promise<any> {
        // console.log('create connection fun called ' + this.connectedPromise);
        return new Promise((resolve, reject) => this.connectedPromise = resolve);
    }
}