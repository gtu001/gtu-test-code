import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { TWRBMFxrateQU004020Component } from './twrbm-fxrate-qu004-020/twrbm-fxrate-qu004-020.component';


const TWRBMFxrateQU004Routes:Routes = [
  {
    path:'',
    redirectTo:'020',
    pathMatch:'full'
  },{
    path:'020',
    component:TWRBMFxrateQU004020Component
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
export class TWRBMFxrateQU004RoutingModule {}
