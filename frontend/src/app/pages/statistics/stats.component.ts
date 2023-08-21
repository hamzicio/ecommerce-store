import { Component, OnDestroy, OnInit } from '@angular/core';
import { ProductService } from '../../services/product.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-stats',
  templateUrl: './stats.component.html',
  styleUrls: ['./stats.component.css']
})
export class StatsComponent implements OnInit, OnDestroy {


  title: string;
  page: any;



  constructor(private productService: ProductService,
    private route: ActivatedRoute) {
    this.title = "Welcome Admin"

  }

  cards: any[] = [
    { title: 'Products', number: 0, icon: 'fas fa-box' },
    { title: 'Categories', number: 0, icon: 'fas fa-tags' },
    { title: 'Users', number: 0, icon: 'fas fa-users' },
    { title: 'Orders', number: 0, icon: 'fas fa-shopping-cart' }
  ];

  ngOnInit() {
    this.productService.getStats().subscribe(u => {
      this.cards =
        [
          { title: 'Products', number: u.products, icon: 'fas fa-box' },
          { title: 'Categories', number: u.categories, icon: 'fas fa-tags' },
          { title: 'Users', number: u.users, icon: 'fas fa-users' },
          { title: 'Orders', number: u.orders, icon: 'fas fa-shopping-cart' }
        ];
    }, e => {


    });

  }

  ngOnDestroy(): void {

  }


}
