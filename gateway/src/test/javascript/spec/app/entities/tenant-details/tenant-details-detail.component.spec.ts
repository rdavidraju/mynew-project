/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { AgreeGatewayV1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TenantDetailsDetailComponent } from '../../../../../../main/webapp/app/entities/tenant-details/tenant-details-detail.component';
import { TenantDetailsService } from '../../../../../../main/webapp/app/entities/tenant-details/tenant-details.service';
import { TenantDetails } from '../../../../../../main/webapp/app/entities/tenant-details/tenant-details.model';

describe('Component Tests', () => {

    describe('TenantDetails Management Detail Component', () => {
        let comp: TenantDetailsDetailComponent;
        let fixture: ComponentFixture<TenantDetailsDetailComponent>;
        let service: TenantDetailsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [AgreeGatewayV1TestModule],
                declarations: [TenantDetailsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TenantDetailsService,
                    JhiEventManager
                ]
            }).overrideTemplate(TenantDetailsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TenantDetailsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TenantDetailsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new TenantDetails(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.tenantDetails).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
