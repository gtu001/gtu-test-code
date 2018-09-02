import { enableProdMode } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { AppModule } from './app/app.module';
import { environment } from './environments/environment';

import { Test001Module } from './app/gtu/test001/test001.module'

if (environment.production) {
	enableProdMode();
}


platformBrowserDynamic().bootstrapModule(AppModule)
.catch(err => console.log(err));
