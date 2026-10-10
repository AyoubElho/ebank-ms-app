import { Component, OnInit, inject } from '@angular/core';
import { AsyncPipe, DatePipe, DecimalPipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { catchError, map, Observable, of } from 'rxjs';

import { environment } from '../../environments/environment';
import {
  AccountListState,
  Account,
  RequestStatus
} from '../model/account.model';
import {LoadingService} from '../services/loading';

@Component({
  selector: 'app-accounts',
  standalone: true,
  imports: [AsyncPipe, DecimalPipe, DatePipe],
  templateUrl: './accounts.html',
  styleUrl: './accounts.css'
})
export class Accounts implements OnInit {

  private http = inject(HttpClient);

  private apiUrl =
    `${environment.apiUrl}/EBANK-SERVICE/bankAccounts/`;

  accounts$!: Observable<AccountListState>;
  public loadService = inject(LoadingService);

  ngOnInit(): void {
    this.accounts$ = this.http
      .get<Account[]>(this.apiUrl)
      .pipe(
        map(resp => ({
          accounts: resp,
          status: RequestStatus.SUCCESS
        })),
        catchError(err => {
          let message = 'Something went wrong. Please try again later.';

          if (err.status === 0) {
            message = 'Cannot connect to the server. Please check your connection.';
          } else if (err.status === 404) {
            message = 'The bank accounts endpoint was not found.';
          } else if (err.status >= 500) {
            message = 'The server is temporarily unavailable. Please try again later.';
          }

          return of({
            status: RequestStatus.ERROR,
            errorMessage: message
          });
        })
      );
  }
}
