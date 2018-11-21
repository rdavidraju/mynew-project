/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { AgreeGatewayV1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { LedgerDefinitionDetailComponent } from '../../../../../../main/webapp/app/entities/ledger-definition/ledger-definition-detail.component';
import { LedgerDefinitionService } from '../../../../../../main/webapp/app/entities/ledger-definition/ledger-definition.service';
import { LedgerDefinition } from '../../../../../../main/webapp/app/entities/ledger-definition/ledger-definition.model';

describe('Component Tests', () => {

    describe('LedgerDefinition Management Detail Component', () => {
        let comp: LedgerDefinitionDetailComponent;
        let fixture: ComponentFixture<LedgerDefinitionDetailComponent>;
        let service: LedgerDefinitionService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [AgreeGatewayV1TestModule],
                declarations: [LedgerDefinitionDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    LedgerDefinitionService,
                    JhiEventManager
                ]
            }).overrideTemplate(LedgerDefinitionDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(LedgerDefinitionDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LedgerDefinitionService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new LedgerDefinition(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.ledgerDefinition).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
