import { AfterContentChecked, Component, OnDestroy, OnInit } from '@angular/core';
import { CartService } from '../../services/cart.service';
import { Subject, Subscription } from 'rxjs';
import { UserService } from '../../services/user.service';
import { JwtResponse } from '../../response/JwtResponse';
import { CartItem } from '../../models/ProductInOrder';
import { debounceTime, switchMap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { Role } from '../../enum/Role';

@Component({
    selector: 'app-cart',
    templateUrl: './cart.component.html',
    styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit, OnDestroy, AfterContentChecked {

    constructor(private cartService: CartService,
        private userService: UserService,
        private router: Router) {
        this.userSubscription = this.userService.currentUser.subscribe(user => this.currentUser = user);
    }

    cartItems = [];
    total = 0;
    currentUser: JwtResponse;
    userSubscription: Subscription;

    private updateTerms = new Subject<CartItem>();
    sub: Subscription;

    static validateCount(productInOrder) {
        const max = productInOrder.productStock;
        if (productInOrder.count > max) {
            productInOrder.count = max;
        } else if (productInOrder.count < 1) {
            productInOrder.count = 1;
        }
        console.log(productInOrder.count);
    }

    ngOnInit() {
        this.cartService.getCart().subscribe(prods => {
            this.cartItems = prods;
        });

        this.sub = this.updateTerms.pipe(
            // wait 300ms after each keystroke before considering the term
            debounceTime(300), 
            //
            // ignore new term if same as previous term
            // Same Object Reference, not working here
            //  distinctUntilChanged((p: ProductInOrder, q: ProductInOrder) => p.count === q.count),
            //
            // switch to new search observable each time the term changes
            switchMap((productInOrder: CartItem) => this.cartService.update(productInOrder))
        ).subscribe(prod => {
            if (prod) { throw new Error(); }
        },
            _ => console.log('Update Item Failed'));
    }

    ngOnDestroy() {
        if (!this.currentUser) {
            this.cartService.storeLocalCart();
        }
        this.userSubscription.unsubscribe();
    }

    ngAfterContentChecked() {
        this.total = this.cartItems.reduce(
            (prev, cur) => prev + cur.count * cur.productPrice, 0);
    }

    addOne(productInOrder) {
        productInOrder.count++;
        CartComponent.validateCount(productInOrder);
        if (this.currentUser) { this.updateTerms.next(productInOrder); }
    }

    minusOne(productInOrder) {
        productInOrder.count--;
        CartComponent.validateCount(productInOrder);
        if (this.currentUser) { this.updateTerms.next(productInOrder); }
    }

    onChange(productInOrder) {
        CartComponent.validateCount(productInOrder);
        if (this.currentUser) { this.updateTerms.next(productInOrder); }
    }


    remove(productInOrder: CartItem) {
        this.cartService.remove(productInOrder).subscribe(
            success => {
                this.cartItems = this.cartItems.filter(e => e.productId !== productInOrder.productId);
                console.log('Cart: ' + this.cartItems);
            },
            _ => console.log('Remove Cart Failed'));
    }

    checkout() {
        if (!this.currentUser) {
            this.router.navigate(['/login'], { queryParams: { returnUrl: this.router.url } });
        } else if (this.currentUser.role !== Role.Customer) {
            this.router.navigate(['/seller']);
        } else {
            this.cartService.checkout().subscribe(
                _ => {
                    this.cartItems = [];
                },
                error1 => {
                    console.log('Checkout Cart Failed');
                });
            this.router.navigate(['/']);
        }

    }
}

