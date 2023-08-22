import { AfterContentChecked, Component, OnInit } from '@angular/core';
import { ProductInfo } from "../../models/productInfo";
import { ProductService } from "../../services/product.service";
import { ActivatedRoute, Router } from "@angular/router";

@Component({
    selector: 'app-product-add',
    templateUrl: './product-add.component.html',
    styleUrls: ['./product-add.component.css']
})
export class ProductAddComponent implements OnInit, AfterContentChecked {

    product = new ProductInfo();

    constructor(private productService: ProductService,
        private route: ActivatedRoute,
        private router: Router) {
    }

    productId: string;
    isEdit = false;
    categories = []

    ngOnInit() {
        this.productService.getAllCategories().subscribe((data) => {
            this.categories = data
        })

    }

    onSubmit() {

        this.add();
    }

    handleUpload(event) {
        const file = event.target.files[0];
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => {
            this.product.productIcon = reader.result as any
        };
    }


    add() {
        this.product = { ...this.product, productCategory: { categoryId: this.product.productCategory } }
        this.productService.create(this.product).subscribe(prod => {
            if (!prod) throw new Error;
            this.router.navigate(['/seller/product']);
        },
            e => {
            });
    }

    ngAfterContentChecked(): void {
        console.log(this.product);
    }
}
