import { AfterContentChecked, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { Category } from 'src/app/models/Category';
import { CategoryService } from 'src/app/services/CategoryService';

@Component({
    selector: 'app-category-edit',
    templateUrl: './category-edit.component.html',
    styleUrls: ['./category-edit.component.css']
})
export class CategoryEditComponent implements OnInit, AfterContentChecked {

    category = new Category();

    constructor(private categoryService: CategoryService,
        private route: ActivatedRoute,
        private router: Router) {
    }

    categoryId: string;
    isEdit = false;



    ngOnInit() {
        this.categoryId = this.route.snapshot.paramMap.get('id');
        if (this.categoryId) {
            this.isEdit = true;
            this.categoryService.getDetail(this.categoryId).subscribe(prod => this.category = prod);
        }

    }

    update() {
        this.categoryService.update(this.category).subscribe(prod => {
            if (!prod) throw new Error();
            this.router.navigate(['/seller/category']);
        },
            err => {
            });

    }

    onSubmit() {
        if (this.category) {
            this.update();
        }
    }



    ngAfterContentChecked(): void {
        console.log(this.category);
    }
}
