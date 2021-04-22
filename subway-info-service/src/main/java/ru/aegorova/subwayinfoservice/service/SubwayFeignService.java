package ru.aegorova.subwayinfoservice.service;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import ru.aegorova.subwayinfoservice.dto.SubwayStationDto;


@FeignClient(name="subway-service")
@RibbonClient(name="subway-service")
public interface SubwayFeignService {

    @GetMapping("/subway/stations")
    SubwayStationDto getAllStations();
}
