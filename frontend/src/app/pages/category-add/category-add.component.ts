import { AfterContentChecked, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { Category } from 'src/app/models/Category';
import { CategoryService } from 'src/app/services/CategoryService';

@Component({
    selector: 'app-category-add',
    templateUrl: './category-add.component.html',
    styleUrls: ['./category-add.component.css']
})
export class CategoryAddComponent implements OnInit, AfterContentChecked {

    category = new Category();

    constructor(private categoryService: CategoryService,
        private route: ActivatedRoute,
        private router: Router) {
    }

    isEdit = false;


    ngOnInit() {

    }

    onSubmit() {

        this.add();
    }


    add() {
        this.categoryService.create(this.category).subscribe(prod => {
            if (!prod) throw new Error;
            this.router.navigate(['/seller/category']);
        },
            e => {
            });
    }

    ngAfterContentChecked(): void {
        console.log(this.category);
    }
}
