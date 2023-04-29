package com.example.Geocoder.controller;

import com.example.Geocoder.service.QueryCacheService;
import com.google.maps.errors.ApiException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/Geocoder")
public class QueryCacheController {
    private QueryCacheService queryCacheService;

    @GetMapping("/getLatLng")
    public String getLatLng(@RequestParam String address)
            throws IOException, InterruptedException, ApiException {
        return queryCacheService.getLatLng(address);
    }

    @GetMapping("/getAddress")
    public String getAddress(@RequestParam double lat,
                             @RequestParam double lng)
            throws IOException, InterruptedException, ApiException {
        return queryCacheService.getAddress(lat, lng);
    }

}
