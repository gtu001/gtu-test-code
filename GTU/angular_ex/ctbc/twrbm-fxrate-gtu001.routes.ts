import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { TWRBMFxrateGtu001_010_Component } from './twrbm-fxrate-gtu001-010/twrbm-fxrate-gtu001-010.component';
// twrbm-fxrate/twrbm-fxrate-gtu001/twrbm-fxrate-gtu001-010/twrbm-fxrate-gtu001.component

const TWRBMFxrateGtu001Routes:Routes = [
  {
    path:'',
    redirectTo:'010',
    pathMatch:'full'
  },{
    path:'010',
    component:TWRBMFxrateGtu001_010_Component
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(TWRBMFxrateGtu001Routes)
  ],
  exports:[
    RouterModule
  ]
})
export class TWRBMFxrateGtu001RoutingModule {}


