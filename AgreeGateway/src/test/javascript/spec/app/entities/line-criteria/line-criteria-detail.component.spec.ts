/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { AgreeGatewayV1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { LineCriteriaDetailComponent } from '../../../../../../main/webapp/app/entities/line-criteria/line-criteria-detail.component';
import { LineCriteriaService } from '../../../../../../main/webapp/app/entities/line-criteria/line-criteria.service';
import { LineCriteria } from '../../../../../../main/webapp/app/entities/line-criteria/line-criteria.model';

describe('Component Tests', () => {

    describe('LineCriteria Management Detail Component', () => {
        let comp: LineCriteriaDetailComponent;
        let fixture: ComponentFixture<LineCriteriaDetailComponent>;
        let service: LineCriteriaService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [AgreeGatewayV1TestModule],
                declarations: [LineCriteriaDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    LineCriteriaService,
                    JhiEventManager
                ]
            }).overrideTemplate(LineCriteriaDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(LineCriteriaDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LineCriteriaService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new LineCriteria(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.lineCriteria).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
