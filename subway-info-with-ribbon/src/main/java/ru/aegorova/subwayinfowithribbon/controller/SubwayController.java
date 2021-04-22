package ru.aegorova.subwayinfowithribbon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.aegorova.subwayinfowithribbon.dto.SubwayStationDto;
import ru.aegorova.subwayinfowithribbon.service.SubwayFeignService;


@RestController
@RequiredArgsConstructor
public class SubwayController {

    private final SubwayFeignService subwayFeignService;

    @GetMapping("/suway-info/stations")
    public SubwayStationDto getAllStations() {
        return subwayFeignService.getAllStations();
    }

}
