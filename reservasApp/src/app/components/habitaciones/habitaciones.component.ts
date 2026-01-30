import { Component, ElementRef, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HabitacionesService } from '../../services/habitaciones.service';

import Swal from 'sweetalert2';
import { Habitacion } from '../../models/Habitacion.models';

declare var bootstrap: any;

@Component({
  selector: 'app-habitaciones',
  standalone: false, 
  templateUrl: './habitaciones.component.html',
  styleUrls: ['./habitaciones.component.css']
})
export class HabitacionesComponent implements OnInit, AfterViewInit {

  listaHabitaciones: Habitacion[] = [];
  listaFiltrada: Habitacion[] = [];
  habitacionForm: FormGroup;
  modalTitulo: string = 'Registrar Habitación';
  isEditMode: boolean = false;
  idEdicion: number | null = null;
  
  
  tiposHabitacion: string[] = ['INDIVIDUAL', 'DOBLE', 'SUITE', 'FAMILIAR'];

  @ViewChild('modalRef') modalRef!: ElementRef;
  private modalInstance: any;

  constructor(private fb: FormBuilder, private service: HabitacionesService) {
    this.habitacionForm = this.fb.group({
      numero: ['', [Validators.required, Validators.min(1)]],
      tipo: ['INDIVIDUAL', Validators.required],
      precio: ['', [Validators.required, Validators.min(1)]],
      capacidad: ['', [Validators.required, Validators.min(1)]],
      descripcion: ['', Validators.maxLength(255)]
    });
  }

  ngOnInit(): void {
    this.cargarHabitaciones();
  }

  ngAfterViewInit(): void {
    this.modalInstance = new bootstrap.Modal(this.modalRef.nativeElement);
  }

  cargarHabitaciones() {
    this.service.listar().subscribe({
      next: (resp) => {
        this.listaHabitaciones = resp;
        this.listaFiltrada = resp;
      },
      error: () => Swal.fire('Error', 'No se pudieron cargar las habitaciones', 'error')
    });
  }

  onSearch(event: any) {
    const term = event.target.value.toLowerCase();
    this.listaFiltrada = this.listaHabitaciones.filter(h => 
      h.numero.toString().includes(term) || 
      h.tipo.toLowerCase().includes(term) ||
      h.estado.toLowerCase().includes(term)
    );
  }

  abrirModal() {
    this.isEditMode = false;
    this.modalTitulo = 'Registrar Habitación';
    this.habitacionForm.reset({ tipo: 'INDIVIDUAL' });
    this.modalInstance.show();
  }

  editar(habitacion: Habitacion) {
    this.isEditMode = true;
    this.idEdicion = habitacion.id;
    this.modalTitulo = `Editar Habitación ${habitacion.numero}`;
    this.habitacionForm.patchValue(habitacion);
    this.modalInstance.show();
  }

  guardar() {
    if (this.habitacionForm.invalid) {
      this.habitacionForm.markAllAsTouched();
      return;
    }

    const datos = this.habitacionForm.value;

    if (this.isEditMode && this.idEdicion) {
      
      const habitacionActualizada = { ...datos, id: this.idEdicion };
      
      this.service.actualizar(this.idEdicion, habitacionActualizada).subscribe({
        next: () => {
          Swal.fire('Actualizado', 'Habitación actualizada', 'success');
          this.cargarHabitaciones();
          this.modalInstance.hide();
        },
        error: () => Swal.fire('Error', 'No se pudo actualizar', 'error')
      });
    } else {
      
      this.service.crear(datos).subscribe({
        next: () => {
          Swal.fire('Registrado', 'Habitación creada', 'success');
          this.cargarHabitaciones();
          this.modalInstance.hide();
        },
        error: (err) => Swal.fire('Error', 'Verifica si el número ya existe', 'error')
      });
    }
  }

  cambiarEstado(id: number, estadoActual: string) {
    const nuevoEstado = estadoActual === 'MANTENIMIENTO' ? 'DISPONIBLE' : 'MANTENIMIENTO';
    const textoAccion = estadoActual === 'MANTENIMIENTO' ? 'Habilitar' : 'Deshabilitar';

    Swal.fire({
      title: `¿${textoAccion} habitación?`,
      text: `La habitación pasará a estado: ${nuevoEstado}`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí, cambiar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.service.cambiarEstado(id, nuevoEstado).subscribe({
          next: () => {
            this.cargarHabitaciones();
            Swal.fire('Éxito', `Estado cambiado a ${nuevoEstado}`, 'success');
          },
          error: () => Swal.fire('Error', 'No se pudo cambiar el estado', 'error')
        });
      }
    });
  }

  getBadgeClass(estado: string): string {
    switch (estado) {
      case 'DISPONIBLE': return 'badge bg-success';
      case 'OCUPADA': return 'badge bg-danger';
      case 'MANTENIMIENTO': return 'badge bg-secondary';
      case 'LIMPIEZA': return 'badge bg-warning text-dark';
      default: return 'badge bg-primary';
    }
  }
}