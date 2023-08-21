import { AfterContentChecked, Component, OnInit } from '@angular/core';
import { ProductInfo } from "../../models/productInfo";
import { ProductService } from "../../services/product.service";
import { ActivatedRoute, Router } from "@angular/router";

@Component({
    selector: 'app-product-edit',
    templateUrl: './product-edit.component.html',
    styleUrls: ['./product-edit.component.css']
})
export class ProductEditComponent implements OnInit, AfterContentChecked {

    product = new ProductInfo();

    constructor(private productService: ProductService,
        private route: ActivatedRoute,
        private router: Router) {
    }

    productId: string;
    isEdit = false;
    categories = []



    ngOnInit() {
        this.productId = this.route.snapshot.paramMap.get('id');
        if (this.productId) {
            this.isEdit = true;
            this.productService.getDetail(this.productId).subscribe(prod => this.product = prod);
            this.productService.getAllCategories().subscribe((data) => {
                this.categories = data
            })
        }

    }

    update() {
        this.productService.update(this.product).subscribe(prod => {
            if (!prod) throw new Error();
            this.router.navigate(['/seller']);
        },
            err => {
            });

    }

    onSubmit() {
        if (this.productId) {
            this.update();
        }
    }



    ngAfterContentChecked(): void {
        console.log(this.product);
    }
}
