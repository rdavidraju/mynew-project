import { trigger, state, style, animate, transition, query, group } from '@angular/animations';

export const routerAnimation = trigger('routerAnimation', [
    transition('*<=>*', [
        // css styles at start of transition
        style({ opacity: 0 }),
        // animation and styles at end of transition
        animate('0.3s', style({ opacity: 1 }))
    ])
]);

export const displayAnimation = trigger(
    'displayAnimation', [
        transition(':enter', [
            style({ opacity: 0 }),
            animate('300ms 100ms ease-in', style({ opacity: 1 }))
        ]),
        transition(':leave', [
            style({  opacity: 1 }),
            animate('100ms 100ms ease-in', style({  opacity: 0 }))
        ])
    ]
)