/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { AgreeGatewayV1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { NotificationBatchDetailComponent } from '../../../../../../main/webapp/app/entities/notification-batch/notification-batch-detail.component';
import { NotificationBatchService } from '../../../../../../main/webapp/app/entities/notification-batch/notification-batch.service';
import { NotificationBatch } from '../../../../../../main/webapp/app/entities/notification-batch/notification-batch.model';

describe('Component Tests', () => {

    describe('NotificationBatch Management Detail Component', () => {
        let comp: NotificationBatchDetailComponent;
        let fixture: ComponentFixture<NotificationBatchDetailComponent>;
        let service: NotificationBatchService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [AgreeGatewayV1TestModule],
                declarations: [NotificationBatchDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    NotificationBatchService,
                    JhiEventManager
                ]
            }).overrideTemplate(NotificationBatchDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(NotificationBatchDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NotificationBatchService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new NotificationBatch(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.notificationBatch).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
