import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { Test001Module } from './gtu/test001/test001.module';
import { LogComponent } from './gtu/utils/log.component'
import { SanitizeUrl } from './gtu/utils/sanitize_url.component'

@NgModule({
	declarations: [
		AppComponent,
	],
	imports: [
		BrowserModule,
		Test001Module,
	],
	providers: [LogComponent, SanitizeUrl],
	bootstrap: [AppComponent]
})
export class AppModule { }
