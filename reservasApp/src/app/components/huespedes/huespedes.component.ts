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
  isEditMode: boolean = false;
  selectedHuesped: HuespedResponse | null = null;
  showActions: boolean = false;

  @ViewChild('huespedModalRef') huespedModalEl!: ElementRef;
  huespedForm: FormGroup;
  private modalInstance!: any;

  constructor(private fb: FormBuilder, private huespedesService: HuespedesService, private authService: AuthService) {
    this.huespedForm = this.fb.group({
      id: [null],
      nombre: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
      apellido: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
      email: ['', [Validators.required, Validators.email, Validators.maxLength(50)]],
      telefono: ['', [Validators.required, Validators.pattern(/^[0-9]{10}$/)]], // Solo 10 dígitos
      
      tipoDocumento: ['INE', [Validators.required]], 
      numeroDocumento: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(10), Validators.pattern(/^[0-9]*$/)]], 
      
      nacionalidad: ['Mexicana', [Validators.required]]
    });
  }

  //MIOPOOOOO

listaHuespedesFiltrada: HuespedResponse[] = [];

//Pavel
listarHuespedes(): void {
  this.huespedesService.getHuespedes().subscribe({
    next: resp => {
      this.listaHuespedes = resp;
      this.listaHuespedesFiltrada = resp; 
    },
    error: err => console.error('Error al cargar huespedes', err)
  });
} 

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

  /*
  listarHuespedes(): void {
    this.huespedesService.getHuespedes().subscribe({
      next: resp => this.listaHuespedes = resp,
      error: err => console.error('Error al cargar huespedes', err)
    });
  } */

  toggleForm(): void {
    this.resetForm();
    this.modalText = 'Registrar Huesped';
    this.modalInstance.show();
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
      id: huesped.id,
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
    this.huespedForm.reset();
    this.huespedForm.patchValue({
      tipoDocumento: 'INE',
      nacionalidad: 'Mexicana'
    });
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
      documento: documentoFinal, // <--- Aquí va el unido
      nacionalidad: formValues.nacionalidad
    };

    if (this.isEditMode && this.selectedHuesped) {
      const id = this.selectedHuesped.id;
      this.huespedesService.putHuesped(huespedData, id).subscribe({
        next: updated => {
          const index = this.listaHuespedes.findIndex(h => h.id == id);
          if (index !== -1) this.listaHuespedes[index] = updated;
          
          Swal.fire('Actualizado', 'Huesped actualizado correctamente', 'success');
          this.modalInstance.hide();
        },
        error: (err) => {
          console.error(err);
          Swal.fire('Error', 'No se pudo actualizar. Verifica duplicados en Email o Documento.', 'error');
        }
      });

    } else {
      this.huespedesService.postHuesped(huespedData).subscribe({
        next: (registro) => {
          this.listaHuespedes.push(registro);
          Swal.fire('Registrado', 'Huesped registrado correctamente', 'success');
          this.modalInstance.hide();
        },
        error: (err) => {
          console.error(err);
          Swal.fire('Error de Registro', 'No se pudo guardar. Verifica que el <b>EMAIL</b>, <b>TELÉFONO</b> o <b>DOCUMENTO</b> no estén ya registrados.', 'error');
        }
      });
    }
  }

  deleteHuesped(idHuesped: number): void {
  Swal.fire({
    title: '¿Desactivar huesped?',
    text: 'El huesped quedará marcado como eliminado.',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: 'Sí, desactivar',
    cancelButtonText: 'Cancelar'
  }).then(result => {
    if (result.isConfirmed) {
      this.huespedesService.deleteHuesped(idHuesped).subscribe({
        next: (updated) => {
          const index = this.listaHuespedes.findIndex(h => h.id === idHuesped);
          if (index !== -1) this.listaHuespedes[index] = updated;

          Swal.fire('Desactivado', 'Huesped marcado como eliminado', 'success');
        },
        error: () => Swal.fire('Error', 'No se pudo desactivar.', 'error')
      });
    }
  });
}

}