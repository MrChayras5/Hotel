import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environments } from '../environments/environments';
import { Reserva } from '../models/Reserva.models';


@Injectable({
  providedIn: 'root'
})
export class ReservasService {

  private apiUrl: string = environments.apiUrl.concat('reservaciones'); 

  constructor(private http: HttpClient) { }

  listar(): Observable<Reserva[]> {
    return this.http.get<Reserva[]>(this.apiUrl);
  }

  crear(reserva: Reserva): Observable<Reserva> {
    return this.http.post<Reserva>(this.apiUrl, reserva);
  }

  actualizar(id: number, reserva: Reserva): Observable<Reserva> {
    return this.http.put<Reserva>(`${this.apiUrl}/${id}`, reserva);
  }

  cancelar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}