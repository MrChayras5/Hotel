import { Component, ElementRef, ViewChild, OnInit, AfterViewInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HuespedRequest, HuespedResponse } from '../../models/Huesped.models';
import { HuespedesService } from '../../services/huespedes.service';
import Swal from 'sweetalert2';
import { Roles } from '../../constants/Roles';
import { AuthService } from '../../services/auth.service';

declare var bootstrap: any;

@Component({
  selector: 'app-huespedes',
  standalone: false,
  templateUrl: './huespedes.component.html',
  styleUrl: './huespedes.component.css'
})
export class HuespedesComponent implements OnInit, AfterViewInit {
  modalText: string = 'Registrar Huesped';
  listaDocumentos: string[] = ['INE', 'PASAPORTE', 'LICENCIA'];
  
  listaHuespedes: HuespedResponse[] = [];
  listaHuespedesFiltrada: HuespedResponse[] = [];
  
  isEditMode: boolean = false;
  selectedHuesped: HuespedResponse | null = null;
  showActions: boolean = false;

  @ViewChild('huespedModalRef') huespedModalEl!: ElementRef;
  huespedForm: FormGroup;
  private modalInstance!: any;

  constructor(private fb: FormBuilder, private huespedesService: HuespedesService, private authService: AuthService) {
    this.huespedForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
      apellido: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
      email: ['', [Validators.required, Validators.email, Validators.maxLength(50)]],
      // ✅ Requisito: Teléfono de 10 dígitos [cite: 125]
      telefono: ['', [Validators.required, Validators.pattern(/^[0-9]{10}$/)]], 
      tipoDocumento: ['INE', [Validators.required]], 
      numeroDocumento: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(10), Validators.pattern(/^[0-9]*$/)]], 
      nacionalidad: ['Mexicana', [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.listarHuespedes();
    if (this.authService.hasRole(Roles.ADMIN)) {
      this.showActions = true;
    }
  }

  ngAfterViewInit(): void {
    this.modalInstance = new bootstrap.Modal(this.huespedModalEl.nativeElement, { keyboard: false });
    this.huespedModalEl.nativeElement.addEventListener('hidden.bs.modal', () => {
      this.resetForm();
    });
  }

  listarHuespedes(): void {
    this.huespedesService.getHuespedes().subscribe({
      next: resp => {
        this.listaHuespedes = resp;
        this.listaHuespedesFiltrada = resp; // Muestra todos al inicio
      },
      error: err => console.error('Error al cargar huespedes', err)
    });
  }

  // ✅ Método de búsqueda por nombre, documento o email [cite: 93]
  onSearch(event: any): void {
    const term = event.target.value.toLowerCase();
    
    if (!term) {
      this.listaHuespedesFiltrada = this.listaHuespedes;
      return;
    }

    this.listaHuespedesFiltrada = this.listaHuespedes.filter(h => 
      h.nombre.toLowerCase().includes(term) ||
      h.apellido.toLowerCase().includes(term) ||
      h.email.toLowerCase().includes(term) ||
      h.documento.toLowerCase().includes(term)
    );
  }

  getFilaClass(activo: number): string {
    return activo === 0 ? 'fila-inactiva' : '';
  }

  onSubmit(): void {
    if (this.huespedForm.invalid) {
      this.huespedForm.markAllAsTouched();
      return;
    }

    const formValues = this.huespedForm.value;
    const documentoFinal = formValues.tipoDocumento + formValues.numeroDocumento;

    const huespedData: HuespedRequest = {
      nombre: formValues.nombre,
      apellido: formValues.apellido,
      email: formValues.email,
      telefono: formValues.telefono,
      documento: documentoFinal,
      nacionalidad: formValues.nacionalidad
    };

    if (this.isEditMode && this.selectedHuesped) {
      this.huespedesService.putHuesped(huespedData, this.selectedHuesped.id).subscribe({
        next: updated => {
          const index = this.listaHuespedes.findIndex(h => h.id == this.selectedHuesped?.id);
          if (index !== -1) this.listaHuespedes[index] = updated;
          this.listaHuespedesFiltrada = [...this.listaHuespedes];
          Swal.fire('Actualizado', 'Huesped actualizado correctamente', 'success');
          this.modalInstance.hide();
        },
        error: (err) => Swal.fire('Error', 'No se pudo actualizar.', 'error')
      });
    } else {
      this.huespedesService.postHuesped(huespedData).subscribe({
        next: (registro) => {
          this.listaHuespedes.push(registro);
          this.listaHuespedesFiltrada = [...this.listaHuespedes];
          Swal.fire('Registrado', 'Huesped registrado correctamente', 'success');
          this.modalInstance.hide();
        },
        error: (err) => Swal.fire('Error', 'Verifica datos duplicados.', 'error')
      });
    }
  }

  deleteHuesped(idHuesped: number): void {
    Swal.fire({
      title: '¿Desactivar huésped?',
      text: 'El registro se marcará como inactivo.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí, desactivar',
      cancelButtonColor: '#d33'
    }).then(result => {
      if (result.isConfirmed) {
        this.huespedesService.deleteHuesped(idHuesped).subscribe({
          next: () => {
            // Actualización para borrado lógico (se mantiene en lista pero cambia estado)
            const index = this.listaHuespedes.findIndex(h => h.id === idHuesped);
            if (index !== -1) {
              this.listaHuespedes[index].activo = 0;
              this.listaHuespedesFiltrada = [...this.listaHuespedes];
            }
            Swal.fire('Desactivado', 'Huésped inactivo.', 'success');
          },
          error: () => Swal.fire('Error', 'No se pudo desactivar.', 'error')
        });
      }
    });
  }

  editHuesped(huesped: HuespedResponse): void {
    this.isEditMode = true;
    this.selectedHuesped = huesped;
    this.modalText = 'Editando Huesped: ' + huesped.nombre;

    let tipo = 'INE';
    let numero = huesped.documento;

    for (const docType of this.listaDocumentos) {
      if (huesped.documento.startsWith(docType)) {
        tipo = docType;
        numero = huesped.documento.substring(docType.length);
        break; 
      }
    }

    this.huespedForm.patchValue({
      nombre: huesped.nombre,
      apellido: huesped.apellido,
      email: huesped.email,
      telefono: huesped.telefono,
      nacionalidad: huesped.nacionalidad,
      tipoDocumento: tipo,
      numeroDocumento: numero
    });
    this.modalInstance.show();
  }

  resetForm(): void {
    this.isEditMode = false;
    this.selectedHuesped = null;
    this.huespedForm.reset({
      tipoDocumento: 'INE',
      nacionalidad: 'Mexicana'
    });
  }

  toggleForm(): void {
    this.resetForm();
    this.modalInstance.show();
  }
}