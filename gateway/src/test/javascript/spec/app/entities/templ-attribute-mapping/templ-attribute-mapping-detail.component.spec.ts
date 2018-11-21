/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { AgreeGatewayV1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TemplAttributeMappingDetailComponent } from '../../../../../../main/webapp/app/entities/templ-attribute-mapping/templ-attribute-mapping-detail.component';
import { TemplAttributeMappingService } from '../../../../../../main/webapp/app/entities/templ-attribute-mapping/templ-attribute-mapping.service';
import { TemplAttributeMapping } from '../../../../../../main/webapp/app/entities/templ-attribute-mapping/templ-attribute-mapping.model';

describe('Component Tests', () => {

    describe('TemplAttributeMapping Management Detail Component', () => {
        let comp: TemplAttributeMappingDetailComponent;
        let fixture: ComponentFixture<TemplAttributeMappingDetailComponent>;
        let service: TemplAttributeMappingService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [AgreeGatewayV1TestModule],
                declarations: [TemplAttributeMappingDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TemplAttributeMappingService,
                    JhiEventManager
                ]
            }).overrideTemplate(TemplAttributeMappingDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TemplAttributeMappingDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TemplAttributeMappingService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new TemplAttributeMapping(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.templAttributeMapping).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
