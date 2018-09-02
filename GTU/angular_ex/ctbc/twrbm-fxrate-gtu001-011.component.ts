import { Component, ViewChild, Input, Output, EventEmitter, SimpleChange, SimpleChanges } from '@angular/core';
import { BasePageComponent } from 'app/ng/base/base-page.component';
import { Forms } from 'app/ng/validation/forms';
import { Rules } from 'app/ng/validation/rules';
import { TabConfig } from "app/ng/view/nav-tabs-fx/nav-tabs-fx.component";
import { Injectable } from '@angular/core';
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import { Observable, Subject } from 'rxjs/Rx';
import { Directive, ElementRef, HostListener } from '@angular/core';

@Component({
    selector: 'gtu001-child-001',
    template: `
    <div #viewRootElement style="background-color:#82FF82;padding:10px;margin-bottom:5px;">
        <h3><b>[[[[{{title}}]]]]</b></h3>
        <input type="button" (click)="clickMe($event)" size="20" value="click me" /><br/>
    </div>
    `,
    styleUrls: []
})
export class Gtu001_test_Child_Component {
    @Input()
    private title: string;

    @Output("goEmitter")
    private testEmitter: EventEmitter<string> = new EventEmitter<string>();

    private counter: number = 0;

    constructor() {
    }

    ngOnChanges(changes: SimpleChanges) {
        console.log("ngOnChanges <<<<");
        if (changes.myNum && !changes.myNum.isFirstChange()) {
            // exteranl API call or more preprocessing...
        }

        for (let propName in changes) {
            let change = changes[propName];
            console.dir(change);
            if (change.isFirstChange()) {
                console.log(`first change: ${propName}`);
            } else {
                console.log(`prev: ${change.previousValue}, cur: ${change.currentValue}`);
            }
        }
    }

    set setTitle(str: string) {
        this.title = str;
    }

    clickMe(event) {
        this.counter++;
        console.log("clickMe = " + this.counter);
        this.testEmitter.emit("" + this.counter);
    }
}

@Component({
    selector: 'gtu001-parent',
    template: `
        <div style="background-color:#30FFFF;padding:10px;5">
            Parent here!!!
            <gtu001-child-001 #gtu001Test001 [title]="title" 
                 (goEmitter)="displayCounter($event)">
            </gtu001-child-001>
            <br/>
            改變title : <input type="input" [(ngModel)]="title" size="20" /><br/>
            <br/>
            <br/>
            <gtu001-table></gtu001-table>
            <br/>
        </div>
    `,
    styleUrls: []
})
export class Gtu001_test_Parent_Component {

    @ViewChild('gtu001Test001')
    private gtu001Test001: Gtu001_test_Child_Component;

    @Input()
    private title: string;

    constructor() {
    }

    displayCounter(counter) {
        console.log("displayCounter " + counter);
    }
}

@Component({
    selector: 'gtu001-table',
    template: `
        <div style="background-color:#FFD382;padding:10px;margin-bottom:5px;">
            <table>
                <tr *ngFor="let row of table; let index = index;">
                    <td *ngFor="let cellKey of getKeys(row);">
                        {{cellKey}} = {{row.get(cellKey)}}
                    </td>
                </tr>
            </table>
        </div>
    `,
    styleUrls: []
})
export class Gtu001_test_DataTable_Component {

    private table: Array<{}>;
    private getKeys = MapUtil.getKeys;
    private articles: TestBean[];
    private errorMessage: string;

    /*
    constructor(private _articleService: LoadJsonService) {  
        this.articles = [];  
    }  
    */
    constructor() {
    }

    ngOnInit() {
        this.table = new Array<{}>();
        let map = new Map<string, string>();
        for (let ii = 0; ii < 3; ii++) {
            map.set("A" + ii, "a" + ii);
            map.set("B" + ii, "b" + ii);
            map.set("C" + ii, "c" + ii);
            this.table.push(map);
            MapUtil.showMap(map);
        }

        /*
        let self = this;
        self._articleService.getArticles().subscribe(//
            response => this.articles = response, //
            error => this.errorMessage = <any>error);
            */
    }
}

@Injectable()
export class MapUtil {
    constructor() {
    }

    public static showMap(map: Map<string, string>) {
        map.forEach((value: string, key: string) => {
            console.log("> map ---- ", key, value);
        });
    }

    public static getKeys(map: Map<string, string>) {
        return Array.from(map.keys());
    }
}

@Injectable()
export class LogComponent {
    constructor() {
    }

    static debug(log: any, ...logs: any[]) {
        console.log('%c DEBUG: ', 'background: #222;color:white', log, ...logs);
    }

    static info(log: any, ...logs: any[]) {
        console.log('%c INFO:  ', 'background: green;color:white', log, ...logs);
    }

    static warn(log: any, ...logs: any[]) {
        console.log('%c WARN:  ', 'background: gray;color:white', log, ...logs);
    }

    static error(log: any, ...logs: any[]) {
        console.log('%c ERROR: ', 'background: red;color:white', log, ...logs);
    }

    static fatal(log: any, ...logs: any[]) {
        console.log('%c FATAL: ', 'background: red;color:white', log, ...logs);
    }
}

@Injectable()
export class LoadJsonService {
    jsonFileURL: string = "./twrbm-fxrate-gtu001-011.001.json";

    constructor(private http: Http) {
    }

    getArticles(): Observable<TestBean[]> {
        return this.http.get(this.jsonFileURL).map((response: Response) => {
            return <TestBean[]>response.json()
        }).catch(this.handleError);
    }

    private handleError(errorResponse: Response) {
        console.log(errorResponse.statusText);
        return Observable.throw(errorResponse.json().error || "Server error");
    }
}

interface TestBean {
    key: string;
    countyName: string;
}



@Directive({
    selector: '[appHighlight]'
})
export class HighlightDirective {
    constructor(private el: ElementRef) {
    }

    @HostListener('mouseenter') onMouseEnter() {
        this.highlight('yellow');
    }

    @HostListener('mouseleave') onMouseLeave() {
        this.highlight(null);
    }

    private highlight(color: string) {
        this.el.nativeElement.style.backgroundColor = color;
    }
}
