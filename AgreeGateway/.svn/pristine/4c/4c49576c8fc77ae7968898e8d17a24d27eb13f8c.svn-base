import {Pipe, PipeTransform} from '@angular/core';

@Pipe({name: 'amountFormat', pure: false})
export class AmountFormat implements PipeTransform {
    transform(input:number, currency?:any, decimal?:number): any{

        if(input != 0 || input != null){
            /* having decimal */
            if(!decimal){
                decimal = 2;
            }

            if (input >= 1000000000000) {
                return currency + " " + (input / 1000000000000).toFixed(decimal).replace(/\.0$/, '') + 'T';
            }
            else if (input >= 1000000000) {
                return currency + " " + (input / 1000000000).toFixed(decimal).replace(/\.0$/, '') + 'B';
            }
            else if (input >= 1000000) {
                return currency + " " + (input / 1000000).toFixed(decimal).replace(/\.0$/, '') + 'M';
            }
            else if(input >= 100000) {
                return currency + " " + (input / 1000).toFixed(decimal).replace(/\.0$/, '') + 'K';
            } else {
                //return currency + " " + input.toFixed(decimal).replace(/\.0$/, '');
                return currency + " " + input.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,")
            }
        }else{

        }
    }
}