/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { AgreeGatewayV1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { SourceConnectionDetailsDetailComponent } from '../../../../../../main/webapp/app/entities/source-connection-details/source-connection-details-detail.component';
import { SourceConnectionDetailsService } from '../../../../../../main/webapp/app/entities/source-connection-details/source-connection-details.service';
import { SourceConnectionDetails } from '../../../../../../main/webapp/app/entities/source-connection-details/source-connection-details.model';

describe('Component Tests', () => {

    describe('SourceConnectionDetails Management Detail Component', () => {
        let comp: SourceConnectionDetailsDetailComponent;
        let fixture: ComponentFixture<SourceConnectionDetailsDetailComponent>;
        let service: SourceConnectionDetailsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [AgreeGatewayV1TestModule],
                declarations: [SourceConnectionDetailsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    SourceConnectionDetailsService,
                    JhiEventManager
                ]
            }).overrideTemplate(SourceConnectionDetailsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SourceConnectionDetailsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SourceConnectionDetailsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new SourceConnectionDetails(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.sourceConnectionDetails).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
