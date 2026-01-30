export interface Reserva {
    id?: number;
    idHuesped: number;
    idHabitacion: number;
    fechaEntrada: string;
    fechaSalida: string;
    noches?: number;
    total?: number;
    estado?: string;
    
    nombreHuesped?: string;
    numeroHabitacion?: string;
}