package ru.aegorova.subwayinfobasefeignclient.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import ru.aegorova.subwayinfobasefeignclient.dto.SubwayStationDto;


@FeignClient(value="subway-service", url="http://localhost:8080")
public interface SubwayFeignService {

    @GetMapping("/subway/stations")
    SubwayStationDto getAllStations();
}
