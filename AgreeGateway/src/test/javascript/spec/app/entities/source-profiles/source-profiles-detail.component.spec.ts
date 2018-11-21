/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { AgreeGatewayV1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { SourceProfilesDetailComponent } from '../../../../../../main/webapp/app/entities/source-profiles/source-profiles-detail.component';
import { SourceProfilesService } from '../../../../../../main/webapp/app/entities/source-profiles/source-profiles.service';
import { SourceProfiles } from '../../../../../../main/webapp/app/entities/source-profiles/source-profiles.model';

describe('Component Tests', () => {

    describe('SourceProfiles Management Detail Component', () => {
        let comp: SourceProfilesDetailComponent;
        let fixture: ComponentFixture<SourceProfilesDetailComponent>;
        let service: SourceProfilesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [AgreeGatewayV1TestModule],
                declarations: [SourceProfilesDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    SourceProfilesService,
                    JhiEventManager
                ]
            }).overrideTemplate(SourceProfilesDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SourceProfilesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SourceProfilesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new SourceProfiles(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.sourceProfiles).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
