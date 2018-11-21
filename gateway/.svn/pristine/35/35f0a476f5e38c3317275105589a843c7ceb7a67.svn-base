/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
/*import { AgreeGatewayTestModule } from '../../../test.module';*/
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { LookUpCodeDetailComponent } from '../../../../../../main/webapp/app/entities/look-up-code/look-up-code-detail.component';
import { LookUpCodeService } from '../../../../../../main/webapp/app/entities/look-up-code/look-up-code.service';
import { LookUpCode } from '../../../../../../main/webapp/app/entities/look-up-code/look-up-code.model';

describe('Component Tests', () => {

    describe('LookUpCode Management Detail Component', () => {
        let comp: LookUpCodeDetailComponent;
        let fixture: ComponentFixture<LookUpCodeDetailComponent>;
        let service: LookUpCodeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [],
                declarations: [LookUpCodeDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    LookUpCodeService,
                    JhiEventManager
                ]
            }).overrideTemplate(LookUpCodeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(LookUpCodeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LookUpCodeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new LookUpCode(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.lookUpCodeDetails).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
