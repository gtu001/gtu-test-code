import { Component, ViewChild, Input } from '@angular/core';
import { BasePageComponent } from 'app/ng/base/base-page.component';
import { Forms } from 'app/ng/validation/forms';
import { Rules } from 'app/ng/validation/rules';
import { TabConfig } from "app/ng/view/nav-tabs-fx/nav-tabs-fx.component";
import { FormDateRangeComponent } from 'app/ng/view/form-date-range/form-date-range.component';
import {
    DropdownHandler,
    Log,
    FormInputSetter,
    FormUtil,
    ArrayTransfer,
    FormCssHandler
} from 'app/twrbm-fxrate/twrbm-pay-tx018/twrbm-pay-tx018-010/twrbm-pay-tx018-010.component';

@Component({
    selector: 'twrbm-pay-tx018-020',
    templateUrl: './twrbm-pay-tx018-020.component.html',
    styleUrls: ['./twrbm-pay-tx018-020.component.css']
})
export class TWRBMPayTx018020Component extends BasePageComponent {

    pageData: any;

    tabConfig: TabConfig;

    isLoading: boolean = true;
    hasError: boolean = true;

    @ViewChild('formFromRef')
    private formFromRef;
    private formHolder : Forms;
    private formUtil: FormUtil;
    private formGroup: {};
    private formInputSetter: FormInputSetter;

    init(pageData: {}) {
        this.pageData = pageData;
        this.formHolder = pageData['form'];
        this.formUtil = new FormUtil(this.formHolder);
        super.changePageTitle(getBundleString('其他瓦斯費', 'pay_tx018.00001186'));
    }

    goNextPage(e) {
        super.preventEvent(e, () => {
            // 因為此頁下各tab是各自交易,所以為正常發送數位軌跡資訊,需交易自行補上
            super.getUtils().sendDigitalTrackingData4Click(null, getBundleString('放款利率', 'fxrate_qu004.00003099'), 'tab2');
            // super.changeTxn('/twrbm-fxrate/tx018/020', {}, true);
            const rq = {
                form: this.formHolder,
            }
            super.nextPage('/twrbm-fxrate/tx018/030', {}, false);
        });
    }
}