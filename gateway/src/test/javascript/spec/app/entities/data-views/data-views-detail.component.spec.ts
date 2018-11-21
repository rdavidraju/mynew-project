/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { AgreeGatewayV1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { DataViewsDetailComponent } from '../../../../../../main/webapp/app/entities/data-views/data-views-detail.component';
import { DataViewsService } from '../../../../../../main/webapp/app/entities/data-views/data-views.service';
import { DataViews } from '../../../../../../main/webapp/app/entities/data-views/data-views.model';

describe('Component Tests', () => {

    describe('DataViews Management Detail Component', () => {
        let comp: DataViewsDetailComponent;
        let fixture: ComponentFixture<DataViewsDetailComponent>;
        let service: DataViewsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [AgreeGatewayV1TestModule],
                declarations: [DataViewsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    DataViewsService,
                    JhiEventManager
                ]
            }).overrideTemplate(DataViewsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DataViewsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DataViewsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new DataViews(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.dataViews).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
