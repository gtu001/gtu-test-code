import { NgModule } from '@angular/core';
import { SharedModule } from 'app/ng/module/shared.module';

import { TWRBMFxrateQU004020Component, NgInit } from './twrbm-fxrate-qu004-020/twrbm-fxrate-qu004-020.component';
import { TWRBMFxrateQU004RoutingModule } from './twrbm-fxrate-qu004.routes';
@NgModule( {
    imports: [
        SharedModule,
        TWRBMFxrateQU004RoutingModule,
    ],
    declarations: [
        TWRBMFxrateQU004020Component,
        NgInit,
    ],
    exports: [
        TWRBMFxrateQU004020Component,
    ]
} )
export class TWRBMFxrateQU004Module { }
