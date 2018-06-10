import { Component, ViewChild, Input, Directive , Output, EventEmitter} from '@angular/core';
import { BasePageComponent } from 'app/ng/base/base-page.component';
import { BaseViewComponent } from 'app/ng/base/base-view.component';
import { Forms } from 'app/ng/validation/forms';
import { Rules } from 'app/ng/validation/rules';
import { TabConfig } from "app/ng/view/nav-tabs-fx/nav-tabs-fx.component";
import { FormDateRangeComponent } from 'app/ng/view/form-date-range/form-date-range.component';
import { ModalSelectComponent } from 'app/ng/view/modal-select/modal-select.component';
// import { Log } from 'app/twrbm-fxrate/twrbm-pay-tx018/twrbm-pay-tx018-010/twrbm-pay-tx018-010.component';



@Component({
    selector: 'twrbm-pay-tx018-011-dropdownModel',
    templateUrl: './twrbm-pay-tx018-011.component.html',
    styleUrls: ['./twrbm-pay-tx018-011.component.css']
})
export class TWRBMPayTx018011Component extends BaseViewComponent {

    accountList : {}[];
    itemClick : Function;
    @Output() private itemClickEvent = new EventEmitter();

    constructor() {
        super();
    }

    init() {
        Log.log("TWRBMPayTx018011Component init ok");

        // 點擊遮罩,也執行關閉
        super.get$RootElement().on('click', e => {
            this.clickCancelEvent(e);
            return false;
        });
    }

    initSetup(accountList : {}[], itemClick : Function){
        this.accountList = accountList;
        this.itemClick = itemClick;
    }

    clickCancelEvent(event?) {
        if(event){
            super.preventEvent(event, () => {
                super.get$RootElement().modal('hide');
            });
        }else{
            super.get$RootElement().modal('hide');
        }
    }

    private clickItemEvent(e, idx:number, itemRef){
        super.preventEvent(e, ()=> {
            super.get$RootElement().find('.active').removeClass('active');
            itemRef.classList.add('active');
            setTimeout(() => {
                if(this.itemClick != null){
                    this.itemClick(idx);
                }else{
                    this.itemClickEvent.emit({"idx" : idx, "itemRef" : itemRef});
                }
                this.clickCancelEvent();
            }, 200);
        });
    }

    showModal() {
        Log.log("TWRBMPayTx018011Component.showModal start");
        // hide,避免畫面會看到itemHtml切換

        if (super.hasObserver()) {
            super.triggerObserver();
            //強制險是
            setTimeout(() => {
                super.get$RootElement().modal('show');
            }, 200);
        } else {
            setTimeout(() => {
                super.get$RootElement().modal('show');
            }, 200);
        }
    }

    getViewComponentName() {
        return 'twrbm-pay-tx018-011-dropdownModel';
    }
}

export class Log {
    static log(log: any, ...logs: any[]) {
        console.log('%c GTU001: ', 'background: red;color:white', log, ...logs);
    }
}