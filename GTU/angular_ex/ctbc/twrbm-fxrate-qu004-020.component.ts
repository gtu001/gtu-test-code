import { Component, ViewChild, Input } from '@angular/core';
import { BasePageComponent } from 'app/ng/base/base-page.component';
import { Forms } from 'app/ng/validation/forms';
import { Rules } from 'app/ng/validation/rules';
import { TabConfig } from "app/ng/view/nav-tabs-fx/nav-tabs-fx.component";
import { FormDateRangeComponent } from 'app/ng/view/form-date-range/form-date-range.component';
import { Directive, ElementRef, HostListener } from '@angular/core';


@Component({
    selector: 'twrbm-fxrate-qu004-020',
    templateUrl: './twrbm-fxrate-qu004-020.component.html',
    styleUrls: ['./twrbm-fxrate-qu004-020.component.css']
})
export class TWRBMFxrateQU004020Component extends BasePageComponent {

    pageData: any;

    tabConfig: TabConfig;

    _isLoading: boolean = true;
    _hasError: boolean = true;
    updateDate: string = "";
    errorStatus: string;

    rateSummary: {};

    init(pageData: {}) {
        this.pageData = pageData;

        if (this.tabConfig == undefined) {
            this.initTabConfig();
        }

        let useUrl = ('useUrl' in pageData) ? pageData["useUrl"] : '/twrbm-fxrate/qu004/010/';
        this._isLoading = true;
        this.rateSummary = [];

        super.changePageTitle(getBundleString('利率查詢', 'fxrate_qu004.00001262'));

        super.sendAndReceiveAsync(useUrl, {})
            .then((rsData: any) => {
                Log.log("so far so good! START");

                this.tabConfig.isLoading = true;

                this.updateDate = rsData["updateDate"];
                this.rateSummary = rsData["rateSummary"];

                // this.rateSummary.forEach(r => {
                //     this.log(">>>>>>>>" + JSON.stringify(r));
                // });

                this._isLoading = false;
                this.tabConfig.isLoading = false;

                Log.log("so far so good! END");
            })
            .catch(status => {
                /*
                this.errorStatus = status;

                this._isLoading = false;
                this.tabConfig.isLoading = false;

                this._hasError = true;
                */

                super.handleResponseError(status);
            });
    }

    initTabConfig() {
        this.tabConfig = new TabConfig();
        this.tabConfig.tabIdx = 1;
        this.tabConfig.tabsLabelTxt = [
            getBundleString('台幣存款利率', 'fxrate_qu004.00004559'),
            getBundleString('外幣存款利率', 'fxrate_qu003.00001857'),
            getBundleString('外幣利率', 'fxrate_qu004.00001262'),
        ];
        this.tabConfig.tabClass = '';
        this.tabConfig.tabSwitchEvent = (idx) => {
            Log.log(`GOGO change tab : ${idx}`);
            let urlConfigMap = {
                0: '/twrbm-fxrate/qu003/010/',//TODO
                1: '/twrbm-fxrate/qu004/020/',
                2: '/twrbm-fxrate/qu005/010/',//TODO
            };
            let urlConfig = urlConfigMap[idx] || urlConfigMap[1];
            this.init({ useUrl: urlConfig });
        }
    }

    goToCustCurrency(event) {
        alert("TODO");
    }


    goFxrateQU004(event) {
        alert("TODO");
    }

    goFxrateQU003(e) {
        super.preventEvent(e, () => {
            // 因為此頁下各tab是各自交易,所以為正常發送數位軌跡資訊,需交易自行補上
            super.getUtils().sendDigitalTrackingData4Click(null, getBundleString('臺幣存款利率', 'fxrate_qu004.00004559'), 'tab0');

            super.changeTxn('/twrbm-fxrate/qu003/010', {}, true);
        });
    }

    goFxrateQU005(e) {
        super.preventEvent(e, () => {
            // 因為此頁下各tab是各自交易,所以為正常發送數位軌跡資訊,需交易自行補上
            super.getUtils().sendDigitalTrackingData4Click(null, getBundleString('放款利率', 'fxrate_qu004.00003099'), 'tab2');

            super.changeTxn('/twrbm-fxrate/qu005/010', {}, true);
        })
    }

    isShowTitleOnly(info: TestBean): boolean {
        if (info.livePayment != null && info.livePayment.trim().length != 0) {
            return true;
        }
        if (info.oneYearPayment != null && info.oneYearPayment.trim().length != 0) {
            return true;
        }
        return false;
    }

    get isLoading() {
        return this._isLoading;
    }
    get hasError() {
        return this._hasError;
    }

    getBodyKey() {
        var arry = new Array();
        for (let key in this.rateSummary) {
            arry.push(key);
        }
        return arry;
    }

    getBodyValue(key: string) {
        return this.rateSummary[key];
    }
}

interface TestBean {
    currency: string;
    currency_name: string;
    livePayment: string;
    oneYearPayment: string;
    periodUnit: string;
    rateFix: string;
}

class Log {
    static log(log: any, ...logs: any[]) {
        console.log('%c GTU001: ', 'background: red;color:white', log, ...logs);
    }
}

@Directive({
    selector: "[ngInit]",
    exportAs: "ngInit"
})
export class NgInit {
    @Input() values: any = {};
    @Input() ngInit;
    ngOnInit() {
        if (this.ngInit) {
            this.ngInit();
        }
    }
}