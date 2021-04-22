package ru.aegorova.subwayinfoservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.aegorova.subwayinfoservice.dto.SubwayStationDto;
import ru.aegorova.subwayinfoservice.service.SubwayFeignService;

@RestController
@RequiredArgsConstructor
public class SubwayController {

    private final SubwayFeignService subwayFeignService;

    @GetMapping("/suway-info/stations")
    public SubwayStationDto getAllStations() {
        return subwayFeignService.getAllStations();
    }

}
