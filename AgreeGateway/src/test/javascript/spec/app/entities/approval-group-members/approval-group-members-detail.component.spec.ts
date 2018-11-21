/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { AgreeGatewayV1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ApprovalGroupMembersDetailComponent } from '../../../../../../main/webapp/app/entities/approval-group-members/approval-group-members-detail.component';
import { ApprovalGroupMembersService } from '../../../../../../main/webapp/app/entities/approval-group-members/approval-group-members.service';
import { ApprovalGroupMembers } from '../../../../../../main/webapp/app/entities/approval-group-members/approval-group-members.model';

describe('Component Tests', () => {

    describe('ApprovalGroupMembers Management Detail Component', () => {
        let comp: ApprovalGroupMembersDetailComponent;
        let fixture: ComponentFixture<ApprovalGroupMembersDetailComponent>;
        let service: ApprovalGroupMembersService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [AgreeGatewayV1TestModule],
                declarations: [ApprovalGroupMembersDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ApprovalGroupMembersService,
                    JhiEventManager
                ]
            }).overrideTemplate(ApprovalGroupMembersDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ApprovalGroupMembersDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ApprovalGroupMembersService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ApprovalGroupMembers(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.approvalGroupMembers).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
