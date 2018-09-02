


import { DomSanitizer } from '@angular/platform-browser';
import { Injectable } from '@angular/core';

@Injectable()
export class SanitizeUrl {
    constructor(private sanitizer: DomSanitizer) { }

    getUrl(url) : any {
        return this.sanitizer.bypassSecurityTrustUrl(url);
    }
}

