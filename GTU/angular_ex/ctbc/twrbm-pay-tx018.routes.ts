import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { TWRBMPayTx018010Component } from './twrbm-pay-tx018-010/twrbm-pay-tx018-010.component'
import { TWRBMPayTx018020Component } from './twrbm-pay-tx018-020/twrbm-pay-tx018-020.component'
import { TWRBMPayTx018030Component } from './twrbm-pay-tx018-030/twrbm-pay-tx018-030.component';

const TWRBMFxrateQU004Routes:Routes = [
  {
    path:'',
    redirectTo:'010',
    pathMatch:'full'
  },
  {
    path:'010',
    component:TWRBMPayTx018010Component
  },
  {
    path:'020',
    component:TWRBMPayTx018020Component
  },
  {
    path:'030',
    component:TWRBMPayTx018030Component
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(TWRBMFxrateQU004Routes)
  ],
  exports:[
    RouterModule
  ]
})
export class TWRBMPayTx01810RoutingModule {}
