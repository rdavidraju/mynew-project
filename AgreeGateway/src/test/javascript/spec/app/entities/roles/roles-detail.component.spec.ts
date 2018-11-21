/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { AgreeGatewayV1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { RolesDetailComponent } from '../../../../../../main/webapp/app/entities/roles/roles-detail.component';
import { RolesService } from '../../../../../../main/webapp/app/entities/roles/roles.service';
import { Roles } from '../../../../../../main/webapp/app/entities/roles/roles.model';

describe('Component Tests', () => {

    describe('Roles Management Detail Component', () => {
        let comp: RolesDetailComponent;
        let fixture: ComponentFixture<RolesDetailComponent>;
        let service: RolesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [AgreeGatewayV1TestModule],
                declarations: [RolesDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    RolesService,
                    JhiEventManager
                ]
            }).overrideTemplate(RolesDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RolesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RolesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Roles(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.roles).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
