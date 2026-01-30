import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environments } from '../environments/environments'; 
import { Habitacion } from '../models/Habitacion.models';

@Injectable({
  providedIn: 'root'
})
export class HabitacionesService {

  
  private apiUrl: string = environments.apiUrl.concat('habitaciones');

  constructor(private http: HttpClient) { }

  listar(): Observable<Habitacion[]> {
    return this.http.get<Habitacion[]>(this.apiUrl);
  }

  crear(habitacion: Habitacion): Observable<Habitacion> {
    return this.http.post<Habitacion>(this.apiUrl, habitacion);
  }

  actualizar(id: number, habitacion: Habitacion): Observable<Habitacion> {
    return this.http.put<Habitacion>(`${this.apiUrl}/${id}`, habitacion);
  }

  
  cambiarEstado(id: number, nuevoEstado: string): Observable<Habitacion> {
    return this.http.patch<Habitacion>(`${this.apiUrl}/${id}/estado`, nuevoEstado);
  }
}