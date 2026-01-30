import { Component, ElementRef, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import Swal from 'sweetalert2';
import { ReservasService } from '../../services/reservas.service';
import { HuespedesService } from '../../services/huespedes.service'; 
import { HabitacionesService } from '../../services/habitaciones.service'; 

import { HuespedResponse } from '../../models/Huesped.models'; 
import { Reserva } from '../../models/Reserva.models';
import { Habitacion } from '../../models/Habitacion.models';


declare var bootstrap: any;

@Component({
  selector: 'app-reservas',
  standalone: false,
  templateUrl: './reservas.component.html',
  styleUrls: ['./reservas.component.css']
  
})
export class ReservasComponent implements OnInit, AfterViewInit {

  listaReservas: Reserva[] = [];
  listaHuespedes: HuespedResponse[] = [];
  listaHabitaciones: Habitacion[] = []; 
  
  reservaForm: FormGroup;
  modalTitulo: string = 'Nueva Reserva';
  isEditMode: boolean = false;
  idEdicion: number | null = null;
  
  @ViewChild('modalRef') modalRef!: ElementRef;
  private modalInstance: any;

  constructor(
    private fb: FormBuilder,
    private reservasService: ReservasService,
    private huespedesService: HuespedesService,
    private habitacionesService: HabitacionesService
  ) {
    this.reservaForm = this.fb.group({
      idHuesped: ['', Validators.required],
      idHabitacion: ['', Validators.required],
      fechaEntrada: ['', Validators.required],
      fechaSalida: ['', Validators.required],
      total: [{value: 0, disabled: true}] 
    });
  }

  ngOnInit(): void {
    this.cargarDatos();
  }

  ngAfterViewInit(): void {
    this.modalInstance = new bootstrap.Modal(this.modalRef.nativeElement);
  }

  cargarDatos() {
    this.reservasService.listar().subscribe(resp => {
      this.listaReservas = resp;
      this.enrichReservasData(); 
    });

    this.huespedesService.getHuespedes().subscribe(resp => this.listaHuespedes = resp);

    this.habitacionesService.listar().subscribe(resp => this.listaHabitaciones = resp);
  }


  enrichReservasData() {

    if(this.listaHuespedes.length > 0 && this.listaHabitaciones.length > 0){
        this.listaReservas.forEach(res => {
            const huesped = this.listaHuespedes.find(h => h.id === res.idHuesped);
            const habitacion = this.listaHabitaciones.find(h => h.id === res.idHabitacion);
            res.nombreHuesped = huesped ? `${huesped.nombre} ${huesped.apellido}` : 'ID: ' + res.idHuesped;
            res.numeroHabitacion = habitacion ? habitacion.numero.toString() : 'ID: ' + res.idHabitacion;
        });
    }
  }

  abrirModal() {
    this.isEditMode = false;
    this.modalTitulo = 'Nueva Reserva';
    this.reservaForm.reset();
    this.modalInstance.show();
  }

  editar(reserva: Reserva) {
    this.isEditMode = true;
    this.idEdicion = reserva.id!;
    this.modalTitulo = 'Editar Reserva';
    this.reservaForm.patchValue({
        idHuesped: reserva.idHuesped,
        idHabitacion: reserva.idHabitacion,
        fechaEntrada: reserva.fechaEntrada,
        fechaSalida: reserva.fechaSalida
    });
    this.modalInstance.show();
  }

  guardar() {
    if (this.reservaForm.invalid) {
      this.reservaForm.markAllAsTouched();
      return;
    }

    const datos: Reserva = this.reservaForm.getRawValue();

    if (new Date(datos.fechaEntrada) >= new Date(datos.fechaSalida)) {
        Swal.fire('Error', 'La fecha de salida debe ser mayor a la entrada', 'error');
        return;
    }

    if (this.isEditMode && this.idEdicion) {
      this.reservasService.actualizar(this.idEdicion, datos).subscribe({
        next: () => {
          Swal.fire('Actualizado', 'Reserva modificada', 'success');
          this.cargarDatos();
          this.modalInstance.hide();
        },
        error: (e) => Swal.fire('Error', e.error?.message || 'Error al actualizar', 'error')
      });
    } else {
      this.reservasService.crear(datos).subscribe({
        next: () => {
          Swal.fire('Éxito', 'Reserva creada', 'success');
          this.cargarDatos();
          this.modalInstance.hide();
        },
        error: (e) => Swal.fire('Error', e.error?.message || 'Error al crear', 'error')
      });
    }
  }

  cancelarReserva(id: number) {
    Swal.fire({
      title: '¿Cancelar Reserva?',
      text: "Esta acción cambiará el estado a CANCELADA",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí, cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.reservasService.cancelar(id).subscribe({
          next: () => {
            Swal.fire('Cancelada', 'La reserva ha sido cancelada.', 'success');
            this.cargarDatos();
          },
          error: () => Swal.fire('Error', 'No se pudo cancelar', 'error')
        });
      }
    });
  }
}