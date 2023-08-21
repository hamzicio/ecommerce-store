import { ProductInfo } from "./productInfo";

export interface ProductCategory {
    categoryId: number;
    categoryName: string;
}


export class CartItem {
    productId: string;
    productName: string;
    productPrice: number;
    productStock: number;
    productDescription: string;
    productIcon: string;
    productCategory: any;
    count: number;

    constructor(productInfo: ProductInfo, quantity = 1) {
        this.productId = productInfo.productId;
        this.productName = productInfo.productName;
        this.productPrice = productInfo.productPrice;
        this.productStock = productInfo.productStock;
        this.productDescription = productInfo.productDescription;;
        this.productIcon = productInfo.productIcon;
        this.productCategory = productInfo.productCategory.categoryId;
        this.count = quantity;
    }
}
