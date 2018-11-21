import { Injectable } from '@angular/core';
import { JhiLanguageService } from 'ng-jhipster';

import { Principal } from '../auth/principal.service';
import { AuthServerProvider } from '../auth/auth-jwt.service';
import { SessionStorageService } from 'ng2-webstorage';
import { JhiTrackerService } from '../tracker/tracker.service';
@Injectable()
export class LoginService {

    constructor(
        private languageService: JhiLanguageService,
        private principal: Principal,
        private trackerService: JhiTrackerService,
        private authServerProvider: AuthServerProvider,
        private $sessionStorage: SessionStorageService
    ) {}

    login(credentials, callback?) {
        const cb = callback || function() {};

        return new Promise((resolve, reject) => {
            this.authServerProvider.login(credentials).subscribe((data) => {
                this.principal.identity(true).then((account) => {
                    // After the login the language will be changed to
                    // the language selected by the user during his registration
                    if (account !== null) {
                        this.languageService.changeLanguage(account.langKey);
                    }
                    // this.trackerService.sendActivity();
                     resolve(data);
                    // this.$sessionStorage.store('userData', account);
                    this.$sessionStorage.store('recentRoutes', {});
                    // console.log('this.$sessionStorage ' + JSON.stringify(this.$sessionStorage.retrieve('userData')));
                });
                return cb();
            }, (err) => {
                this.logout();
                reject(err);
                return cb(err);
            });
        });
    }

    loginWithToken(jwt, rememberMe) {
        return this.authServerProvider.loginWithToken(jwt, rememberMe);
    }

    logout() {
        // this.$sessionStorage.clear('userData');
        this.$sessionStorage.clear('recentRoutes');
        this.$sessionStorage.clear('sessionbreadCrumb');
        this.$sessionStorage.clear('sessionbreadCrumbHis');
        // console.log('this.$sessionStorage ' + JSON.stringify(this.$sessionStorage.retrieve('userData')));
        this.authServerProvider.logout().subscribe();
        this.principal.authenticate(null);
        
    }
}
export var UserData :any =[];