package com.example.Geocoder.service;


import com.example.Geocoder.entity.QueryCache;
import com.example.Geocoder.repository.QueryCacheRepository;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class QueryCacheService {
    private QueryCacheRepository queryCacheRepository;

    private final String MY_KEY = "AIzaSyBnuXUFsvocdwB3ei5Gp3iem_-Bvb7C0YY";

    public String getLatLng(String address) {
        QueryCache queryCache = queryCacheRepository.getQueryCacheByQuery(address);
        if (queryCache != null && queryCache.getQuery().equals(address)) {
            return "Location : " + queryCache.getResponse();
        }

            GeoApiContext context = new GeoApiContext.Builder().apiKey(MY_KEY).build();
        GeocodingResult[] results ;
        try {
            results = GeocodingApi
                    .geocode(context, address)
                    .await();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        LatLng location = results[0].geometry.location;
            String response = location.lat + "," + location.lng;

            queryCache = new QueryCache();
            queryCache.setQuery(address);
            queryCache.setResponse(response);
            queryCache.setCreatedAt(LocalDate.now());
            queryCacheRepository.save(queryCache);

            return "Location :" + response;

    }

    public String getAddress(double lat, double lng)  {
        QueryCache queryCache = queryCacheRepository.getQueryCacheByQuery(lat + "," + lng);
        if (queryCache != null && queryCache.getQuery().equals(lat + "," + lng)) {
            return "Address : " + queryCache.getResponse();
        }

        GeoApiContext context = new GeoApiContext.Builder().apiKey(MY_KEY).build();

        LatLng location = new LatLng(lat, lng);

        GeocodingResult[] results ;
        try {
            results = GeocodingApi.reverseGeocode(context, location).await();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        GeocodingResult result = results[0];
        String response = result.formattedAddress;

        queryCache = new QueryCache();
        queryCache.setQuery(lat + "," + lng);
        queryCache.setResponse(response);
        queryCache.setCreatedAt(LocalDate.now());
        queryCacheRepository.save(queryCache);

        return result.formattedAddress;
    }
}
