// /* tslint:disable max-line-length */
// import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
// import { OnInit } from '@angular/core';
// import { DatePipe } from '@angular/common';
// import { ActivatedRoute } from '@angular/router';
// import { Observable } from 'rxjs/Rx';
// import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
// import { AgreeGatewayV1TestModule } from '../../../test.module';
// import { MockActivatedRoute } from '../../../helpers/mock-route.service';
// import { AccountingDataMappingDetailComponent } from '../../../../../../main/webapp/app/entities/accounting-data-mapping/accounting-data-mapping-detail.component';
// import { AccountingDataMappingService } from '../../../../../../main/webapp/app/entities/accounting-data-mapping/accounting-data-mapping.service';
// import { AccountingDataMapping } from '../../../../../../main/webapp/app/entities/accounting-data-mapping/accounting-data-mapping.model';

// describe('Component Tests', () => {

//     describe('AccountingDataMapping Management Detail Component', () => {
//         let comp: AccountingDataMappingDetailComponent;
//         let fixture: ComponentFixture<AccountingDataMappingDetailComponent>;
//         let service: AccountingDataMappingService;

//         beforeEach(async(() => {
//             TestBed.configureTestingModule({
//                 imports: [AgreeGatewayV1TestModule],
//                 declarations: [AccountingDataMappingDetailComponent],
//                 providers: [
//                     JhiDateUtils,
//                     JhiDataUtils,
//                     DatePipe,
//                     {
//                         provide: ActivatedRoute,
//                         useValue: new MockActivatedRoute({id: 123})
//                     },
//                     AccountingDataMappingService,
//                     JhiEventManager
//                 ]
//             }).overrideTemplate(AccountingDataMappingDetailComponent, '')
//             .compileComponents();
//         }));

//         beforeEach(() => {
//             fixture = TestBed.createComponent(AccountingDataMappingDetailComponent);
//             comp = fixture.componentInstance;
//             service = fixture.debugElement.injector.get(AccountingDataMappingService);
//         });

//         describe('OnInit', () => {
//             it('Should call load all on init', () => {
//             // GIVEN

//             spyOn(service, 'find').and.returnValue(Observable.of(new AccountingDataMapping(10)));

//             // WHEN
//             comp.ngOnInit();

//             // THEN
//             expect(service.find).toHaveBeenCalledWith(123);
//             expect(comp.accountingDataMapping).toEqual(jasmine.objectContaining({id: 10}));
//             });
//         });
//     });

// });
