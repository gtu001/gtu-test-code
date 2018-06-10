import { Component, ViewChild, Input, Directive } from '@angular/core';
import { BasePageComponent } from 'app/ng/base/base-page.component';
import { Forms } from 'app/ng/validation/forms';
import { Rules } from 'app/ng/validation/rules';
import { TabConfig } from "app/ng/view/nav-tabs-fx/nav-tabs-fx.component";
import { FormDateRangeComponent } from 'app/ng/view/form-date-range/form-date-range.component';
import { ModalSelectComponent } from 'app/ng/view/modal-select/modal-select.component';
import { TWRBMPayTx018011Component } from 'app/twrbm-fxrate/twrbm-pay-tx018/twrbm-pay-tx018-010/twrbm-pay-tx018-011.component';

@Component({
    selector: 'twrbm-pay-tx018-010',
    templateUrl: './twrbm-pay-tx018-010.component.html',
    styleUrls: ['./twrbm-pay-tx018-010.component.css']
})
export class TWRBMPayTx018010Component extends BasePageComponent {

    pageData: any;

    tabConfig: TabConfig;

    isLoading: boolean = true;
    hasError: boolean = true;

    @ViewChild('formFromRef')
    private formFromRef;
    private formHolder: Forms;
    private formUtil: FormUtil;
    private formGroup: {};
    private formInputSetter: FormInputSetter;

    @ViewChild('selectModal')
    private selectModal: ModalSelectComponent;
    @ViewChild('tx018_011_dropdown')
    private tx018011dropdownModal: TWRBMPayTx018011Component;

    gasHolder: DropdownHandler;


    constructor() {
        super();
    }

    init(pageData: {}) {
        this.pageData = pageData;
        super.changePageTitle(getBundleString('其他瓦斯費', 'pay_tx018.z00001186'));

        this.formGroup = { 'gasCompany': "", 'account': "", 'balanceNumber': "", 'payAmount': "", 'remark': "" };
        this.formHolder = new Forms(null, d => this.validator(d), 0, this.formGroup, this.formFromRef);
        this.formUtil = new FormUtil(this.formHolder, this.formFromRef);
        this.formInputSetter = new FormInputSetter(this.formFromRef);

        this.initSetup();
    }

    private initSetup() {
        super.sendAndReceiveAsync("/twrbm-pay/tx018/050", {})
            .then((rsData: any) => {
                Log.log("so far so good! START");

                //瓦斯
                this.gasHolder = new DropdownHandler(this.selectModal, rsData["gasCompany"], "key", "value", (k, v) => this.dropdownSetVal('gasCompany', k, v), this);

                // "acctType":"00","acctId":"0000888880000293","availBal":"835821480"
                let accountLst = new ArrayTransfer().addKey("acctId", "account").addKey("acctType", "type").addKey("availBal", "money").transfer(rsData["accountLst"]);
                this.tx018011dropdownModal.initSetup(accountLst, null);
                // Log.log(JSON.stringify(accountLst));

                Log.log("so far so good! END");
            })
            .catch(status => {
                super.handleResponseError(status);
            });
    }

    dropdownSetVal(id, k, v) {
        this.formUtil.set(id, k);
        this.formInputSetter.setInputValue(id, v);
    }

    click_twrbmPayTx018_011_dropdownModel(val) {
        let choiceJson = this.tx018011dropdownModal.accountList[parseInt(val["idx"])];
        let value = choiceJson["account"];
        this.formUtil.set("account", value);
        this.formInputSetter.setInputValue("account", value);
    }

    validator(form) {
        Log.log("form : " + JSON.stringify(form));

        if (Rules.isBlank(form.gasCompany)) {
            this.formHolder.setFormErrors("gasCompany", "瓦斯未輸入");
        }
        if (Rules.isBlank(form.balanceNumber)) {
            this.formHolder.setFormErrors("balanceNumber", "請輸入銷帳編號");
        } else {
            if (!/^[a-zA-Z0-9]+$/.test(form.balanceNumber)) {
                this.formHolder.setFormErrors("balanceNumber", "僅接受輸入半形英數字");
            } else if (!/^[a-zA-Z0-9]{16}$/.test(form.balanceNumber)) {
                this.formHolder.setFormErrors("balanceNumber", "銷帳編號需為16碼英數字");
            }
        }
        if (!Rules.isNumber(form.payAmount)) {
            this.formHolder.setFormErrors("payAmount", "僅接受輸入半形數字");
        } else if (parseInt(form.payAmount) <= 0) {
            this.formHolder.setFormErrors("payAmount", "僅接受輸入正整數");
        }
        if (Rules.isBlank(form.account)) {
            this.formHolder.setFormErrors("account", "扣款帳號未輸入");
        }

        Log.log("error : " + JSON.stringify(this.formHolder.formErrors));
    }

    onblurInput(event, id) {
        this.formUtil.set(id, event.target.value);
    }

    goNextPage(e) {
        if (!this.formHolder.isFormValid()) {
            return;
        }
        super.preventEvent(e, () => {
            // 因為此頁下各tab是各自交易,所以為正常發送數位軌跡資訊,需交易自行補上
            super.getUtils().sendDigitalTrackingData4Click(null, getBundleString('放款利率', 'fxrate_qu004.00003099'), 'tab2');
            // super.changeTxn('/twrbm-fxrate/tx018/020', {}, true);
            const rq = {
                form: this.formHolder,
            }
            super.nextPage('/twrbm-fxrate/tx018/020', rq, false);
        });
    }
}

export class DropdownHandler {

    private lstForVal: string[];
    private lstForKey: string[];

    constructor(private selectModal: ModalSelectComponent, orignArry: any, private key: string, private val: string, private callback: Function, private _super: BasePageComponent) {
        if (!Array.isArray(orignArry)) {
            throw new Error("必須為array");
        }
        this.lstForKey = new Array<string>();
        this.lstForVal = new Array<string>();
        orignArry.forEach((v) => {
            if (v.hasOwnProperty(val)) {
                this.lstForVal.push(v[val]);
            } else {
                this.lstForVal.push(v);
            }
            if (v.hasOwnProperty(key)) {
                this.lstForKey.push(v[key]);
            }
        });
    }

    private getKey(index) {
        if (this.lstForKey.length != 0 && this.lstForKey.length > index) {
            return this.lstForKey[index];
        }
        return this.getVal(index);
    }

    private getVal(index) {
        if (this.lstForVal.length != 0 && this.lstForVal.length > index) {
            return this.lstForVal[index];
        }
        return -1;
    }

    launchSelection(e) {
        this.selectModal.reset();
        this._super.preventEvent(e, () => {
            let items = this.lstForVal; // will not launch if returns []
            if (items.length > 0)
                this.selectModal.showModal(this.selectModal.getFocusIndex(),
                    () => items,
                    (index) => {
                        let key = this.getKey(index);
                        let val = this.getVal(index);
                        Log.log("choice : " + key + " / " + val);
                        this.callback(key, val);
                        return key;
                    }
                );
        });
    }
}

export class Log {
    static log(log: any, ...logs: any[]) {
        console.log('%c GTU001: ', 'background: red;color:white', log, ...logs);
    }
}

export class FormInputSetter {
    constructor(private formFromRef?: any) {
    }

    setInputValue(name: string, value: string) {
        if (!this.formFromRef) {
            throw new Error("未設定formFromRef");
        }
        let $form = $(this.formFromRef.nativeElement);
        let $inputText = $form.find("input[formcontrolname=" + name + "]");
        $inputText.attr("value", value);
    }
}

export class FormUtil {
    formInputSetter: FormInputSetter;

    constructor(private formHolder: Forms, private formFromRef?: any) {
        this.formInputSetter = new FormInputSetter(formFromRef);
    }

    get(s) {
        return this.formHolder.getForm().controls[s].value;
    }

    set(s, val) {
        this.formHolder.getForm().controls[s].setValue(val);
    }

    getErr(id) {
        try {
            return this.formHolder.formErrors[id];
        } catch (e) {
            return "";
        }
    }

    getNgClz(id) {
        if (this.getErr(id) == "") {
            return "";
        }
        return 'has-error';
    }
}

export class ArrayTransfer {

    keyMap: {} = {};

    constructor() {
    }

    addKey(fromKey: string, toKey: string) {
        this.keyMap[fromKey] = toKey;
        return this;
    }

    transfer(arry: {}[]) {
        let rtnArry = [];
        arry.forEach(v => {
            let tempMap = {};
            for (let k in this.keyMap) {
                if (v.hasOwnProperty(k)) {
                    tempMap[this.keyMap[k]] = v[k];
                }
            }
            rtnArry.push(tempMap);
        });
        return rtnArry;
    }
}

@Directive({
    selector: "[formCssHandler]",
    exportAs: "formCssHandler",
})
export class FormCssHandler {
    @Input()
    err: any;
    @Input()
    formCssHandler;

    ngOnInit() {
    }

    msg() {
        return this.err ? this.err : "";
    }

    css() {
        return this.err ? "has-error" : "";
    }
}