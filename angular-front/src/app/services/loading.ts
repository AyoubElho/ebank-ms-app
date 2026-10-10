import {Service, signal} from '@angular/core';

@Service()
export class LoadingService {
  loading = signal<boolean>(false);
  public isLoading = this.loading.asReadonly();
  public setLoading(value : boolean){
    this.loading.set(value);
  }
}
