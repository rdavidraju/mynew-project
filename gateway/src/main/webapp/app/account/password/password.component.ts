import { Component, OnInit } from '@angular/core';
import { NotificationsService } from 'angular2-notifications-lite';
import { Router } from '@angular/router';

import { Principal } from '../../shared';
import { PasswordService } from './password.service';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';

declare var $: any;
declare var jQuery: any;
@Component({
    selector: 'jhi-password',
    templateUrl: './password.component.html',
    styleUrls: ['./password.component.scss']
})
export class PasswordComponent implements OnInit {
    doNotMatch: boolean = false;
    error: string;
    success: string;
    account: any;
    password: string;
    confirmPassword: string;

    constructor(
        private passwordService: PasswordService,
        private principal: Principal,
        private notificationsService: NotificationsService,
        private router: Router
    ) {
    }

    ngOnInit() {
        $('body').removeClass('login');
        this.principal.identity().then((account) => {
            this.account = account;
        });

        document.getElementById('password').focus();
    }

    changePassword() {
        if (this.password !== this.confirmPassword) {
            this.error = null;
            this.success = null;
            this.doNotMatch = true;
        } else {
            this.doNotMatch = false;
            this.passwordService.save(this.password).subscribe(() => {
                this.error = null;
                this.notificationsService.success('Success!','Password changed!');
                this.router.navigate(['/kpiboard']);
            }, () => {
                this.success = null;
                // this.error = 'ERROR';
                this.notificationsService.error('Warning','An error has occurred! The password could not be changed.');
            });
        }
    }
}
