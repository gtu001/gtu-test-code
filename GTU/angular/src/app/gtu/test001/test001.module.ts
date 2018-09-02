import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { Test001Component } from './test001.component';


@NgModule({
	declarations: [
		Test001Component
	],
	imports: [
		BrowserModule
	],
	providers: [],
	bootstrap: [Test001Component]
})
export class Test001Module { }
