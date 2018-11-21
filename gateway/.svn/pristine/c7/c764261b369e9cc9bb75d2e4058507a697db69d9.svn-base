import { Directive, ElementRef, HostListener, OnDestroy } from '@angular/core';

@Directive({
  selector: '[numberOnly]'
})
export class NumberDirective implements OnDestroy {

  // Allow decimal numbers
  private regex: RegExp = new RegExp(/^-?[0-9]+(\.[0-9]*){0,1}$/g);
  // Allow key codes for special events. Reflect :
  // Backspace, tab, end, home, arrowleft, arrowright and delete
  private specialKeys: Array<string> = ['Backspace', 'Tab', 'End', 'Home', 'ArrowLeft', 'ArrowRight', 'Delete'];

  constructor(private el: ElementRef) {
  }
  @HostListener('keydown', ['$event'])
  onKeyDown(evt: KeyboardEvent) {
    if (this.specialKeys.indexOf(evt.key) !== -1) {
      return;
    }
    const current: string = this.el.nativeElement.value;
    const next: string = current.concat(evt.key);
    if (next && !String(next).match(this.regex)) {
      evt.preventDefault();
    }
  }

  ngOnDestroy() {

  }

}