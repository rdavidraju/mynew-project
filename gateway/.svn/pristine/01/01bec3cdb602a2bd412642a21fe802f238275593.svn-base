// /* tslint:disable max-line-length */
// import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
// import { OnInit } from '@angular/core';
// import { DatePipe } from '@angular/common';
// import { ActivatedRoute } from '@angular/router';
// import { Observable } from 'rxjs/Rx';
// import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
// import { AgreeGatewayV1TestModule } from '../../../test.module';
// import { MockActivatedRoute } from '../../../helpers/mock-route.service';
// import { ReconcileDetailComponent } from '../../../../../../main/webapp/app/entities/reconcile/reconcile-detail.component';
// import { ReconcileService } from '../../../../../../main/webapp/app/entities/reconcile/reconcile.service';
// import { Reconcile } from '../../../../../../main/webapp/app/entities/reconcile/reconcile.model';

// describe('Component Tests', () => {

//     describe('Reconcile Management Detail Component', () => {
//         let comp: ReconcileDetailComponent;
//         let fixture: ComponentFixture<ReconcileDetailComponent>;
//         let service: ReconcileService;

//         beforeEach(async(() => {
//             TestBed.configureTestingModule({
//                 imports: [AgreeGatewayV1TestModule],
//                 declarations: [ReconcileDetailComponent],
//                 providers: [
//                     JhiDateUtils,
//                     JhiDataUtils,
//                     DatePipe,
//                     {
//                         provide: ActivatedRoute,
//                         useValue: new MockActivatedRoute({id: 123})
//                     },
//                     ReconcileService,
//                     JhiEventManager
//                 ]
//             }).overrideTemplate(ReconcileDetailComponent, '')
//             .compileComponents();
//         }));

//         beforeEach(() => {
//             fixture = TestBed.createComponent(ReconcileDetailComponent);
//             comp = fixture.componentInstance;
//             service = fixture.debugElement.injector.get(ReconcileService);
//         });

//         describe('OnInit', () => {
//             it('Should call load all on init', () => {
//             // GIVEN

//             spyOn(service, 'find').and.returnValue(Observable.of(new Reconcile(10)));

//             // WHEN
//             comp.ngOnInit();

//             // THEN
//             expect(service.find).toHaveBeenCalledWith(123);
//             expect(comp.reconcile).toEqual(jasmine.objectContaining({id: 10}));
//             });
//         });
//     });

// });
