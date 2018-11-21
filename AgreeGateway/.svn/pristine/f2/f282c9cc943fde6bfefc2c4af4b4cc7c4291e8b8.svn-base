/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { AgreeGatewayV1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { MappingSetDetailComponent } from '../../../../../../main/webapp/app/entities/mapping-set/mapping-set-detail.component';
import { MappingSetService } from '../../../../../../main/webapp/app/entities/mapping-set/mapping-set.service';
import { MappingSet } from '../../../../../../main/webapp/app/entities/mapping-set/mapping-set.model';

describe('Component Tests', () => {

    describe('MappingSet Management Detail Component', () => {
        let comp: MappingSetDetailComponent;
        let fixture: ComponentFixture<MappingSetDetailComponent>;
        let service: MappingSetService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [AgreeGatewayV1TestModule],
                declarations: [MappingSetDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    MappingSetService,
                    JhiEventManager
                ]
            }).overrideTemplate(MappingSetDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MappingSetDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MappingSetService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new MappingSet(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.mappingSet).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
