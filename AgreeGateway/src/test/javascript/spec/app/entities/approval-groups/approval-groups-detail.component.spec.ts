/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { AgreeGatewayV1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ApprovalGroupsDetailComponent } from '../../../../../../main/webapp/app/entities/approval-groups/approval-groups-detail.component';
import { ApprovalGroupsService } from '../../../../../../main/webapp/app/entities/approval-groups/approval-groups.service';
import { ApprovalGroups } from '../../../../../../main/webapp/app/entities/approval-groups/approval-groups.model';

describe('Component Tests', () => {

    describe('ApprovalGroups Management Detail Component', () => {
        let comp: ApprovalGroupsDetailComponent;
        let fixture: ComponentFixture<ApprovalGroupsDetailComponent>;
        let service: ApprovalGroupsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [AgreeGatewayV1TestModule],
                declarations: [ApprovalGroupsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ApprovalGroupsService,
                    JhiEventManager
                ]
            }).overrideTemplate(ApprovalGroupsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ApprovalGroupsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ApprovalGroupsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ApprovalGroups(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.approvalGroups).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
