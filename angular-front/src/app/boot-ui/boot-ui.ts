import { Component, inject } from '@angular/core';
import {
  HttpClient,
  HttpDownloadProgressEvent,
  HttpEventType,
  HttpResponse
} from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { LoadingService } from '../services/loading';
import {FormsModule} from '@angular/forms';
import {AsyncPipe} from '@angular/common';
import {MarkdownComponent} from 'ngx-markdown';

@Component({
  selector: 'app-boot-ui',
  standalone: true,
  imports: [
    FormsModule,
    AsyncPipe,
    MarkdownComponent
  ],
  templateUrl: './boot-ui.html',
  styleUrl: './boot-ui.css'
})
export class BotUi {
  query: string = '';

  private http = inject(HttpClient);

  response$!: Observable<any>;

  public loadingService = inject(LoadingService);

  askAgent(): void {
    this.response$ = this.http.get(
      `${environment.apiUrl}/EBANK-BOOT/chat`,
      {
        params: { query: this.query },
        responseType: 'text'
      }
    );
  }

  askAgentStream(): void {
    this.response$ = this.http
      .get(
        `${environment.apiUrl}/EBANK-BOOT/chatStream`,
        {
          params: { query: this.query },
          responseType: 'text',
          observe: 'events',
          reportProgress: true
        }
      )
      .pipe(
        map(event => {
          switch (event.type) {
            case HttpEventType.Sent:
              return { type: 'Sent' };

            case HttpEventType.DownloadProgress:
              return {
                type: 'Response',
                content: (event as HttpDownloadProgressEvent).partialText ?? ''
              };

            case HttpEventType.Response:
              return {
                type: 'Response',
                content: (event as HttpResponse<string>).body ?? ''
              };

            default:
              return {
                type: 'Other',
                data: event
              };
          }
        })
      );
  }
}

