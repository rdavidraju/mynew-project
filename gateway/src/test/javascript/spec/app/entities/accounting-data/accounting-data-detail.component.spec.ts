// /* tslint:disable max-line-length */
// import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
// import { OnInit } from '@angular/core';
// import { DatePipe } from '@angular/common';
// import { ActivatedRoute } from '@angular/router';
// import { Observable } from 'rxjs/Rx';
// import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
// import { AgreeGatewayV1TestModule } from '../../../test.module';
// import { MockActivatedRoute } from '../../../helpers/mock-route.service';
// import { AccountingDataDetailComponent } from '../../../../../../main/webapp/app/entities/accounting-data/accounting-data-detail.component';
// import { AccountingDataService } from '../../../../../../main/webapp/app/entities/accounting-data/accounting-data.service';
// import { AccountingData } from '../../../../../../main/webapp/app/entities/accounting-data/accounting-data.model';

// describe('Component Tests', () => {

//     describe('AccountingData Management Detail Component', () => {
//         let comp: AccountingDataDetailComponent;
//         let fixture: ComponentFixture<AccountingDataDetailComponent>;
//         let service: AccountingDataService;

//         beforeEach(async(() => {
//             TestBed.configureTestingModule({
//                 imports: [AgreeGatewayV1TestModule],
//                 declarations: [AccountingDataDetailComponent],
//                 providers: [
//                     JhiDateUtils,
//                     JhiDataUtils,
//                     DatePipe,
//                     {
//                         provide: ActivatedRoute,
//                         useValue: new MockActivatedRoute({id: 123})
//                     },
//                     AccountingDataService,
//                     JhiEventManager
//                 ]
//             }).overrideTemplate(AccountingDataDetailComponent, '')
//             .compileComponents();
//         }));

//         beforeEach(() => {
//             fixture = TestBed.createComponent(AccountingDataDetailComponent);
//             comp = fixture.componentInstance;
//             service = fixture.debugElement.injector.get(AccountingDataService);
//         });

//         describe('OnInit', () => {
//             it('Should call load all on init', () => {
//             // GIVEN

//             spyOn(service, 'find').and.returnValue(Observable.of(new AccountingData(10)));

//             // WHEN
//             comp.ngOnInit();

//             // THEN
//             expect(service.find).toHaveBeenCalledWith(123);
//             expect(comp.accountingData).toEqual(jasmine.objectContaining({id: 10}));
//             });
//         });
//     });

// });
