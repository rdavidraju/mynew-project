/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { AgreeGatewayV1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ApplicationProgramsDetailComponent } from '../../../../../../main/webapp/app/entities/application-programs/application-programs-detail.component';
import { ApplicationProgramsService } from '../../../../../../main/webapp/app/entities/application-programs/application-programs.service';
import { ApplicationPrograms } from '../../../../../../main/webapp/app/entities/application-programs/application-programs.model';

describe('Component Tests', () => {

    describe('ApplicationPrograms Management Detail Component', () => {
        let comp: ApplicationProgramsDetailComponent;
        let fixture: ComponentFixture<ApplicationProgramsDetailComponent>;
        let service: ApplicationProgramsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [AgreeGatewayV1TestModule],
                declarations: [ApplicationProgramsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ApplicationProgramsService,
                    JhiEventManager
                ]
            }).overrideTemplate(ApplicationProgramsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ApplicationProgramsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ApplicationProgramsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ApplicationPrograms(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.applicationPrograms).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
