import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { apiUrl } from '../../environments/environment';
import { Category } from '../models/Category';

@Injectable({
    providedIn: 'root'
})
export class CategoryService {

    private categoryUrl = `${apiUrl}/category`;

    constructor(private http: HttpClient) {
    }

    getCategoryPaged(page: number, size: number): Observable<any> {
        const url = `${this.categoryUrl}?page=${page}&size=${size}`;
        return this.http.get(url).pipe(
            // tap(data => console.log(data))
        );
    }

    getAllInPage(page: number, size: number): Observable<any> {
        const url = `${this.categoryUrl}?page=${page}&size=${size}`;
        return this.http.get(url)
            .pipe(
            // tap(_ => console.log(_)),
        )
    }

    getCategoryInPage(categoryType: number, page: number, size: number): Observable<any> {
        const url = `${this.categoryUrl}/${categoryType}?page=${page}&size=${size}`;
        return this.http.get(url).pipe(
            // tap(data => console.log(data))
        );
    }


    getDetail(id: String): Observable<Category> {
        const url = `${this.categoryUrl}/${id}`;
        return this.http.get<Category>(url).pipe(
            catchError(_ => {
                console.log("Get Detail Failed");
                return of(new Category());
            })
        );
    }

    update(category: Category): Observable<Category> {
        console.log(category)
        const url = `${apiUrl}/seller/category`;
        return this.http.put<Category>(url, category);
    }

    create(category: Category): Observable<Category> {
        const url = `${apiUrl}/seller/category`;
        return this.http.post<Category>(url, category);
    }


    delelte(category: Category): Observable<any> {
        const url = `${apiUrl}/seller/category/${category.categoryId}`;
        return this.http.delete(url);
    }


    /**
     * Handle Http operation that failed.
     * Let the app continue.
     * @param operation - name of the operation that failed
     * @param result - optional value to return as the observable result
     */
    private handleError<T>(operation = 'operation', result?: T) {
        return (error: any): Observable<T> => {

            console.error(error); // log to console instead

            // Let the app keep running by returning an empty result.
            return of(result as T);
        };
    }
}
