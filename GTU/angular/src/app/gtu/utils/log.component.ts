import { Injectable } from '@angular/core';

@Injectable()
export class LogComponent {
    constructor() {
    }

    debug(log: any, ...logs: any[]) {
        console.log('%c DEBUG: ', 'background: #222;color:white', log, ...logs);
    }

    info(log: any, ...logs: any[]) {
        console.log('%c INFO:  ', 'background: green;color:white', log, ...logs);
    }

    warn(log: any, ...logs: any[]) {
        console.log('%c WARN:  ', 'background: gray;color:white', log, ...logs);
    }

    error(log: any, ...logs: any[]) {
        console.log('%c ERROR: ', 'background: red;color:white', log, ...logs);
    }

    fatal(log: any, ...logs: any[]) {
        console.log('%c FATAL: ', 'background: red;color:white', log, ...logs);
    }
}