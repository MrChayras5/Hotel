package com.pavel.reserva.client;

import com.pavel.reserva.dto.HabitacionDTO;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient(name = "msv-habitacion", url = "http://localhost:8002") 
public interface HabitacionClient {

    @GetMapping("/habitacion/{id}")
    HabitacionDTO obtenerHabitacion(@PathVariable("id") Long id);
}