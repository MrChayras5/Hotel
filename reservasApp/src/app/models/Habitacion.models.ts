export interface Habitacion {
    id: number;
    numero: number;
    tipo: string; // 'INDIVIDUAL', 'DOBLE', 'SUITE'
    descripcion: string;
    precio: number;
    capacidad: number;
    estado: string; // 'DISPONIBLE', 'OCUPADA', 'MANTENIMIENTO'
}