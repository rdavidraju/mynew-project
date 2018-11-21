/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { AgreeGatewayV1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { JeLinesDetailComponent } from '../../../../../../main/webapp/app/entities/je-lines/je-lines-detail.component';
import { JeLinesService } from '../../../../../../main/webapp/app/entities/je-lines/je-lines.service';
import { JeLines } from '../../../../../../main/webapp/app/entities/je-lines/je-lines.model';

describe('Component Tests', () => {

    describe('JeLines Management Detail Component', () => {
        let comp: JeLinesDetailComponent;
        let fixture: ComponentFixture<JeLinesDetailComponent>;
        let service: JeLinesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [AgreeGatewayV1TestModule],
                declarations: [JeLinesDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    JeLinesService,
                    JhiEventManager
                ]
            }).overrideTemplate(JeLinesDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(JeLinesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JeLinesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new JeLines(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.jeLines).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
