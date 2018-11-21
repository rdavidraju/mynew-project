import { Directive, Input, HostListener } from '@angular/core';

@Directive({
    selector: '[materialFilter]'
})
export class MaterialFilterDirective {

    @Input('materialFilter') materialFilter: any;
    @Input('materialFilterInput') materialFilterInput: any;
    @Input('materialFilterModel') materialFilterModel: any;

    @HostListener('onOpen')
    onOpen() {
        this.materialFilter._keyManager.onKeydown = function() { };
        this.materialFilter._keyManager.setActiveItem = function() { };
        this.materialFilterInput.focus();
    }

    @HostListener('onClose')
    onClose() {
        this.materialFilterModel.control.setValue(null);
    }

}