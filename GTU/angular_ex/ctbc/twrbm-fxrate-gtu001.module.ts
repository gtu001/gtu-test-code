import { NgModule } from '@angular/core';
import { SharedModule } from 'app/ng/module/shared.module';

import { TWRBMFxrateGtu001_010_Component } from './twrbm-fxrate-gtu001-010/twrbm-fxrate-gtu001-010.component';
import { TWRBMFxrateGtu001RoutingModule } from './twrbm-fxrate-gtu001.routes';

import { 
    Gtu001_test_Child_Component, 
    Gtu001_test_Parent_Component, 
    Gtu001_test_DataTable_Component,
    HighlightDirective,
} from './twrbm-fxrate-gtu001-011/twrbm-fxrate-gtu001-011.component' 

@NgModule( {
    imports: [
        SharedModule,
        TWRBMFxrateGtu001RoutingModule
    ],
    declarations: [
        TWRBMFxrateGtu001_010_Component,
        Gtu001_test_Child_Component,
        Gtu001_test_Parent_Component,
        Gtu001_test_DataTable_Component,
        HighlightDirective,
    ],
    exports: [
        TWRBMFxrateGtu001_010_Component
    ]
} )
export class TWRBMFxrateGtu001Module { }
