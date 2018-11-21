import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'searchFilter'
})
export class SearchFilterPipe implements PipeTransform {

  transform(arr: any, searchString: string, cusKey?: any, ): any {
    if (!arr || !searchString) return arr;
    if (cusKey) {
      return arr.filter((each) =>
        each[cusKey].toLowerCase().indexOf(searchString.toLocaleLowerCase()) !== -1
      );
    } else {
      // return arr.filter(obj => {
      //   return Object.keys(obj).some(key => {
      //     return obj[key].toString().toLowerCase().includes(searchString.toLocaleLowerCase());
      //   })
      // });

      if (Array.isArray(arr)) {
        return arr.filter(function iter(o) {
          // check if arr contains object or a value
          if (typeof o != 'object') {
            return o.toString().toLowerCase().indexOf(searchString.toString().toLowerCase()) !== -1;
          } else {
            return Object.keys(o).some(function(k) {
              if(o[k] == null){
                o[k] = '';
              }
              if (o[k].toString().toLowerCase().indexOf(searchString.toString().toLowerCase()) !== -1) {
                return true;
              }
              if (Array.isArray(o[k])) {
                o[k] = o[k].filter(iter);
                return o[k].length;
              }
              if (typeof (o[k]) == 'object') {
                return iter(o[k]);
              }
            });
          }
        });
      }
    }
  }

}