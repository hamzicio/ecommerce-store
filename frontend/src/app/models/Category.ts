export class Category {
    categoryId: number;
    categoryName: string


    constructor(category?: Category) {
        if (category) {
            this.categoryId = category.categoryId;
            this.categoryName = category.categoryName;

        } else {
            this.categoryId = null;
            this.categoryName = '';
        }
    }
}

