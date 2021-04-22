package ru.aegorova.subwayservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.aegorova.subwayservice.dto.SubwayStationDto;

import java.util.Arrays;

@RestController
public class SubwayController {

    @GetMapping("/subway/stations")
    public SubwayStationDto getAllStations() {
        return SubwayStationDto.builder()
                .subwayStations(Arrays.asList("Кремлевская", "Площадь Тукая", "Суконная слобода", "Аметьево"))
                .build();
    }
}
