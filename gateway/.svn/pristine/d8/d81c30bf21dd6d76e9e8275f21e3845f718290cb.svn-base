/**
 * @author: Sameer
 * @description: Add active class to element if current router link contains given link/string
 */

import { Directive, ElementRef, OnInit, Input, Renderer2 } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';

@Directive({
  selector: '[routerLinkActiveContains]'
})
export class RouterLinkActiveContainsDirective implements OnInit {
  @Input('routerLinkActiveContains') routerLinkActiveContains: string;
  @Input('linkMatchClass') linkMatchClass: string;

  constructor(
    private router: Router,
    private eRef: ElementRef,
    private renderer: Renderer2
  ) { }

  ngOnInit() {
    this.router.events.subscribe((evt) => {
      if (evt instanceof NavigationEnd) {
        if (evt.url.includes(this.routerLinkActiveContains)) {
          this.renderer.addClass(this.eRef.nativeElement, this.linkMatchClass);
        } else {
          this.renderer.removeClass(this.eRef.nativeElement, this.linkMatchClass);
        }
      }
    });
  }

}