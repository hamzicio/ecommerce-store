import { Component, OnDestroy, OnInit } from '@angular/core';
import { UserService } from "../../services/user.service";
import { JwtResponse } from "../../response/JwtResponse";
import { Subscription } from "rxjs";
import { ActivatedRoute, Router } from "@angular/router";
import { Role } from "../../enum/Role";
import { CategoryService } from 'src/app/services/CategoryService';

@Component({
    selector: 'app-category.list',
    templateUrl: './category.list.component.html',
    styleUrls: ['./category.list.component.css']
})
export class CategoryListComponent implements OnInit, OnDestroy {

    constructor(private userService: UserService,
        private categoryService: CategoryService,
        private route: ActivatedRoute,
        private router: Router) {
    }

    Role = Role;
    currentUser: JwtResponse;
    page: any;
    private querySub: Subscription;

    ngOnInit() {
        this.querySub = this.route.queryParams.subscribe(() => {
            this.update();
        });
    }

    ngOnDestroy(): void {
        this.querySub.unsubscribe();
    }

    update() {
        if (this.route.snapshot.queryParamMap.get('page')) {
            const currentPage = +this.route.snapshot.queryParamMap.get('page');
            const size = +this.route.snapshot.queryParamMap.get('size');
            this.getCategories(currentPage, size);
        } else {
            this.getCategories();
        }
    }

    getCategories(page: number = 1, size: number = 5) {
        this.categoryService.getCategoryPaged(page, size)
            .subscribe(data => {
                this.page = data;
            });
    }

    getProds(page: number = 1, size: number = 5) {
        this.categoryService.getAllInPage(+page, +size)
            .subscribe(page => {
                this.page = page;
            });

    }




    remove(productInfo) {
        this.categoryService.delelte(productInfo).subscribe(_ => {
            this.router.navigate(['/seller/category']);
        },
            err => {
            });
    }


}
