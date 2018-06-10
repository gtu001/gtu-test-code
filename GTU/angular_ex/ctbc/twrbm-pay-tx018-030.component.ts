import { Component, ViewChild, Input } from '@angular/core';
import { BasePageComponent } from 'app/ng/base/base-page.component';
import { Forms } from 'app/ng/validation/forms';
import { Rules } from 'app/ng/validation/rules';
import { TabConfig } from "app/ng/view/nav-tabs-fx/nav-tabs-fx.component";
import { FormDateRangeComponent } from 'app/ng/view/form-date-range/form-date-range.component';

@Component({
    selector: 'twrbm-pay-tx018-030',
    templateUrl: './twrbm-pay-tx018-030.component.html',
    styleUrls: ['./twrbm-pay-tx018-030.component.css']
})
export class TWRBMPayTx018030Component extends BasePageComponent {

    pageData: any;

    tabConfig: TabConfig;

    isLoading: boolean = true;
    hasError: boolean = true;

    form : {};

    init(pageData: {}) {
        this.pageData = pageData;
        super.changePageTitle(getBundleString('其他瓦斯費', 'pay_tx018.00001186'));
    }

    goNextPage(e){
        super.preventEvent(e, () => {
            // 因為此頁下各tab是各自交易,所以為正常發送數位軌跡資訊,需交易自行補上
            super.getUtils().sendDigitalTrackingData4Click(null, getBundleString('放款利率', 'fxrate_qu004.00003099'), 'tab2');            
            // super.changeTxn('/twrbm-fxrate/tx018/020', {}, true);
            const rq = {
                form: this.form,
            }
            super.nextPage('/twrbm-fxrate/tx018/030', {}, false);
        });
    }

    log(log: any, ...logs: any[]) {
        console.log('%c GTU001: ', 'background: red;color:white', log, ...logs);
    }
}