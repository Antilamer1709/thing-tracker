import {LazyLoadEvent} from "primeng/api";
import {SearchDTO} from "../../generated/dto";
import {HostListener} from "@angular/core";

export class CommonComponent {

  public scrHeight: any;
  public scrWidth: any;
  public scrWithLess560: boolean = false;

  public europeLocale = {
    firstDayOfWeek: 0,
    dayNames: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"],
    dayNamesShort: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
    dayNamesMin: ["Mo","Tu","We","Th","Fr","Sa", "Su"],
    monthNames: [ "January","February","March","April","May","June","July","August","September","October","November","December" ],
    monthNamesShort: [ "Jan", "Feb", "Mar", "Apr", "May", "Jun","Jul", "Aug", "Sep", "Oct", "Nov", "Dec" ],
    today: 'Today',
    clear: 'Clear',
    dateFormat: "dd.mm.yy"
  };

  constructor() {}


  public initSearchDTO(searchDTO: SearchDTO<any>, event: LazyLoadEvent): void {
    if (event) {
      searchDTO.rows = event.rows;
      searchDTO.first = event.first;
      if (event.sortField) {
        searchDTO.sortField = event.sortField;
        searchDTO.sortOrder = event.sortOrder;
      }
    }
  }

  @HostListener('window:resize', ['$event'])
  getScreenSize(event?) {
    this.scrHeight = window.innerHeight;
    this.scrWidth = window.innerWidth;
    this.scrWithLess560 = this.scrWidth < 560;
    console.log(this.scrHeight, this.scrWidth);
  }

}
