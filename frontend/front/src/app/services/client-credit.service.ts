import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CreditClient } from '../models/credit-client.model';

@Injectable({
  providedIn: 'root'
})
export class ClientService {
  private apiUrl = 'http://localhost:8091/api/clients';

  constructor(private http: HttpClient) {}

  saveClient(client: CreditClient): Observable<CreditClient> {
    return this.http.post<CreditClient>(`${this.apiUrl}/non-registered-clients`, client);
  }

  getClientByCni(cni: number): Observable<CreditClient> {
    return this.http.get<CreditClient>(`${this.apiUrl}/${cni}`);
  }

  getAllClients(): Observable<CreditClient[]> {
    return this.http.get<CreditClient[]>(this.apiUrl);
  }

  deleteClient(cni: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${cni}`);
  }
}
