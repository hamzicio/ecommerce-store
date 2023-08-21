import { Component, OnDestroy, OnInit } from '@angular/core';
import { UserService } from "../../services/user.service";
import { ProductService } from "../../services/product.service";
import { JwtResponse } from "../../response/JwtResponse";
import { Subscription } from "rxjs";
import { ActivatedRoute, Router } from "@angular/router";
import { ProductStatus } from "../../enum/ProductStatus";
import { Role } from "../../enum/Role";

@Component({
    selector: 'app-product.list',
    templateUrl: './product.list.component.html',
    styleUrls: ['./product.list.component.css']
})
export class ProductListComponent implements OnInit, OnDestroy {

    constructor(private userService: UserService,
        private productService: ProductService,
        private route: ActivatedRoute,
        private router: Router) {
    }

    Role = Role;
    currentUser: JwtResponse;
    page: any;
    ProductStatus = ProductStatus;
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
            this.getProds(currentPage, size);
        } else {
            this.getProds();
        }
    }

    getProds(page: number = 1, size: number = 5) {
        this.productService.getAllInPage(+page, +size)
            .subscribe(page => {
                this.page = page;
            });

    }


    getCategories() {
        this.productService.getAllCategories()
            .subscribe(page => {
                console.log(page);
            });
    }


    remove(productInfo) {
        this.productService.delelte(productInfo).subscribe(_ => {
            this.router.navigate(['/seller/product']);
        },
            err => {
            });
    }


}
