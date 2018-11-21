/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { AgreeGatewayV1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TemplateDetailsDetailComponent } from '../../../../../../main/webapp/app/entities/template-details/template-details-detail.component';
import { TemplateDetailsService } from '../../../../../../main/webapp/app/entities/template-details/template-details.service';
import { TemplateDetails } from '../../../../../../main/webapp/app/entities/template-details/template-details.model';

describe('Component Tests', () => {

    describe('TemplateDetails Management Detail Component', () => {
        let comp: TemplateDetailsDetailComponent;
        let fixture: ComponentFixture<TemplateDetailsDetailComponent>;
        let service: TemplateDetailsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [AgreeGatewayV1TestModule],
                declarations: [TemplateDetailsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TemplateDetailsService,
                    JhiEventManager
                ]
            }).overrideTemplate(TemplateDetailsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TemplateDetailsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TemplateDetailsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new TemplateDetails(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.templateDetails).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
