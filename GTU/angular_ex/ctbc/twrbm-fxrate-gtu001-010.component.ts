import { Component, ViewChild, Input } from '@angular/core';
import { BasePageComponent } from 'app/ng/base/base-page.component';
import { Forms } from 'app/ng/validation/forms';
import { Rules } from 'app/ng/validation/rules';
import { TabConfig } from "app/ng/view/nav-tabs-fx/nav-tabs-fx.component";
// import { TWRBMFxrateQU001011Component } from '../twrbm-fxrate-qu001-011/twrbm-fxrate-qu001-011.component';
import { FormDateRangeComponent } from 'app/ng/view/form-date-range/form-date-range.component';
import { Gtu001_test_Child_Component, Gtu001_test_Parent_Component } from '../twrbm-fxrate-gtu001-011/twrbm-fxrate-gtu001-011.component'



@Component({
    selector: 'twrbm-fxrate-gtu001',
    templateUrl: './twrbm-fxrate-gtu001-010.component.html',
    styleUrls: ['./twrbm-fxrate-gtu001-010.component.css']
})
export class TWRBMFxrateGtu001_010_Component extends BasePageComponent {

    pageData: any;

    tabConfig: TabConfig;
    tabNum: number = 0;
    _isDivHidden: boolean = false;

    // @ViewChild("gtu001_test_gogo")
    // gtu001Test: Gtu001_test_001_Component
    // @ViewChild("gtu002_test_gogo")
    // gtu002Test: Gtu001_test_Parent_Component

    init(pageData: {}) {
        this.pageData = pageData;
        this.initTabConfig();
    }

    initTabConfig() {
        this.tabConfig = new TabConfig();
        this.tabConfig.tabsLabelTxt = ["aaaa", "bbbbb"];
        this.tabConfig.tabClass = 'account-info-tab tab-line ct';
        this.tabConfig.tabSwitchEvent = (idx) => {
            super.log(`GOGO change tab : ${idx}`);
        }
    }

    changeTabs(tabNum) {
        this.tabNum = tabNum;
    }

    initTab(show) {
        show();
    }

    get hasBoolTest() {
        return true;
    }

    get isDivHidden() {
        return this._isDivHidden;
    }
}
