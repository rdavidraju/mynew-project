/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { AgreeGatewayV1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { AccountingLineTypesDetailComponent } from '../../../../../../main/webapp/app/entities/accounting-line-types/accounting-line-types-detail.component';
import { AccountingLineTypesService } from '../../../../../../main/webapp/app/entities/accounting-line-types/accounting-line-types.service';
import { AccountingLineTypes } from '../../../../../../main/webapp/app/entities/accounting-line-types/accounting-line-types.model';

describe('Component Tests', () => {

    describe('AccountingLineTypes Management Detail Component', () => {
        let comp: AccountingLineTypesDetailComponent;
        let fixture: ComponentFixture<AccountingLineTypesDetailComponent>;
        let service: AccountingLineTypesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [AgreeGatewayV1TestModule],
                declarations: [AccountingLineTypesDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    AccountingLineTypesService,
                    JhiEventManager
                ]
            }).overrideTemplate(AccountingLineTypesDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AccountingLineTypesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AccountingLineTypesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new AccountingLineTypes(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.accountingLineTypes).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
