import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class CVService {
    private apiCVUrl = `${environment.apiUrl}/api/cv`;

    constructor(
        private http: HttpClient
    ) {
    }

    getCV(query: string){
        return this.http.get(`${this.apiCVUrl}/search?q=${query}`);
    }

    uploadCV(file) {
        const httpOptions = {
            headers: new HttpHeaders({
                ContentType: 'multipart/form-data; boundary=WebAppBoundary'
            })
        };
        const fd = new FormData();
        fd.append('file', file);
        return this.http.post(`${this.apiCVUrl}/upload`, fd, httpOptions);
    }
}
