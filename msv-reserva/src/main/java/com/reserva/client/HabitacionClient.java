package com.reserva.client; 

import com.reserva.dto.HabitacionDTO; 
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msv-habitacion", url = "http://localhost:8002")
public interface HabitacionClient {

    @GetMapping("/habitacion/{id}")
    HabitacionDTO obtenerHabitacion(@PathVariable("id") Long id);
}