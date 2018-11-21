import { Pipe, PipeTransform } from '@angular/core';
import { CommonService } from './common.service';

@Pipe({ name: 'amountFormat', pure: false })
export class AmountFormat implements PipeTransform {
    convertDec: boolean = false;
    constructor(
        private commonService: CommonService) {
    }
    transform(input: number, currency?: any, decimal?: number, currencyCode?: any): any {
        if (input != 0 || input != null || input != undefined) {
            /* having decimal */
            if (decimal == 0) {
                decimal = 0;
            } else if (!decimal) {
                decimal = 2;
            }
            if (currencyCode) {
                this.convertDec = true;
                this.commonService.currencies.forEach(element => {
                    if (element.cc == currencyCode) {
                        currency = element.cs;
                    }
                });
                return currency + " " + parseFloat(input.toFixed(decimal).replace(/\.0$/, '')).toLocaleString();
            }
            if (!this.convertDec) {
                if (input >= 1000000000000) {
                    return currency + " " + parseFloat((input / 1000000000000).toFixed(decimal).replace(/\.0$/, '')) + ' T';
                }
                else if (input >= 1000000000) {
                    return currency + " " + parseFloat((input / 1000000000).toFixed(decimal).replace(/\.0$/, '')) + ' B';
                }
                else if (input >= 1000000) {
                    return currency + " " + parseFloat((input / 1000000).toFixed(decimal).replace(/\.0$/, '')) + ' M';
                }
                else if (input >= 100000) {
                    return currency + " " + parseFloat((input / 1000).toFixed(decimal).replace(/\.0$/, '')) + ' K';
                } else {
                    //return currency + " " + input.toFixed(decimal).replace(/\.0$/, '');
                    if (input) {
                        // return currency + " " + parseFloat(input.toFixed(decimal).replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,"));
                        // return currency + " " + input.toFixed(decimal).replace(/\.0$/, '');
                        return currency + " " + parseFloat(input.toFixed(decimal).replace(/\.0$/, '')).toLocaleString();
                    } else {
                        return currency + " " + 0;
                    }

                }
            }

        } else {

        }
    }
}