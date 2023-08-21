import { Component, OnDestroy, OnInit } from '@angular/core';
import { UserService } from "../../services/user.service";
import { JwtResponse } from "../../response/JwtResponse";
import { Subscription } from "rxjs";
import { ActivatedRoute, Router } from "@angular/router";
import { Role } from "../../enum/Role";

@Component({
    selector: 'app-user.list',
    templateUrl: './user.list.component.html',
    styleUrls: ['./user.list.component.css']
})
export class UserListComponent implements OnInit, OnDestroy {

    constructor(private userService: UserService,
        private route: ActivatedRoute,
        private router: Router) {
    }

    Role = Role;
    currentUser: JwtResponse;
    page: any;
    private querySub: Subscription;

    ngOnInit() {
        this.getUsers()
    }

    ngOnDestroy(): void {
    }



    getUsers(page: number = 1, size: number = 5) {
        this.userService.getAllUsers(page, size)
            .subscribe(data => {
                this.page = data;
            });
    }



}
