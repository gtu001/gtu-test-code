import { NgModule } from '@angular/core';
import { SharedModule } from 'app/ng/module/shared.module';

import { TWRBMPayTx018010Component, FormCssHandler } from './twrbm-pay-tx018-010/twrbm-pay-tx018-010.component';
import { TWRBMPayTx018011Component } from 'app/twrbm-fxrate/twrbm-pay-tx018/twrbm-pay-tx018-010/twrbm-pay-tx018-011.component';
import { TWRBMPayTx018020Component } from './twrbm-pay-tx018-020/twrbm-pay-tx018-020.component';
import { TWRBMPayTx018030Component } from './twrbm-pay-tx018-030/twrbm-pay-tx018-030.component';
import { TWRBMPayTx01810RoutingModule } from './twrbm-pay-tx018.routes';


@NgModule({
    imports: [
        SharedModule,
        TWRBMPayTx01810RoutingModule,
    ],
    declarations: [
        TWRBMPayTx018010Component,
        TWRBMPayTx018011Component,
        TWRBMPayTx018020Component,
        TWRBMPayTx018030Component,
        FormCssHandler,
    ],
    exports: [
        TWRBMPayTx018010Component,
    ]
})
export class TWRBMPayTx018010Module { }
