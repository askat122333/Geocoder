package com.example.Geocoder.service;

import com.example.Geocoder.dto.QueryCacheDto;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class QueryCacheService {
    private QueryCacheRepository queryCacheRepository;

    private final String MY_KEY = "AIzaSyBnuXUFsvocdwB3ei5Gp3iem_-Bvb7C0YY";

    public QueryCacheDto toDto(QueryCache queryCache) {
        QueryCacheDto queryCacheDto = new QueryCacheDto(
                queryCache.getId(),
                queryCache.getQuery(),
                queryCache.getResponse(),
                queryCache.getCreatedAt()
        );
        return queryCacheDto;
    }

    public Optional<QueryCacheDto> getById(Long id) {
        QueryCache queryCache = queryCacheRepository.findById(id).orElse(null);
        if (queryCache == null) {
            return Optional.empty();
        }
        return Optional.of(toDto(queryCache));
    }

    public List<QueryCacheDto> getAll() {
        List<QueryCache> queryCaches = queryCacheRepository.findAll();
        List<QueryCacheDto> queryCacheDtoList = new ArrayList<>();
        for (QueryCache queryCache : queryCaches) {
            queryCacheDtoList.add(toDto(queryCache));
        }
        return queryCacheDtoList;
    }

    public QueryCacheDto save(QueryCacheDto queryCacheDto) {
        QueryCache queryCache = new QueryCache(
                queryCacheDto.getId(),
                queryCacheDto.getQuery(),
                queryCacheDto.getResponse(),
                queryCacheDto.getCreatedAt()
        );
        queryCacheRepository.save(queryCache);
        return toDto(queryCache);
    }


    public String getLatLng(String address) throws IOException, InterruptedException, ApiException {
        QueryCache queryCache = queryCacheRepository.getQueryCacheByQuery(address);
        if (queryCache != null && queryCache.getQuery().equals(address)) {
            return "Location : " + queryCache.getResponse();
        }
        GeoApiContext context = new GeoApiContext.Builder().apiKey(MY_KEY).build();
        GeocodingResult[] results = GeocodingApi
                .geocode(context, address)
                .await();

        LatLng location = results[0].geometry.location;
        String response = location.lat + "," + location.lng;

        queryCache = new QueryCache();
        queryCache.setQuery(address);
        queryCache.setResponse(response);
        queryCache.setCreatedAt(LocalDate.now());
        queryCacheRepository.save(queryCache);

        return "Location :" + response;
    }

    public String getAddress(double lat, double lng) throws IOException, InterruptedException, ApiException {
        QueryCache queryCache = queryCacheRepository.getQueryCacheByQuery(lat + "," + lng);
        if (queryCache != null && queryCache.getQuery().equals(lat + "," + lng)) {
            return "Address : " + queryCache.getResponse();
        }

        GeoApiContext context = new GeoApiContext.Builder().apiKey(MY_KEY).build();

        LatLng location = new LatLng(lat, lng);

        GeocodingResult[] results = GeocodingApi.reverseGeocode(context, location).await();

        GeocodingResult result = results[0];
        String response = result.formattedAddress;

        queryCache = new QueryCache();
        queryCache.setQuery(lat + "," + lng);
        queryCache.setResponse(response);
        queryCache.setCreatedAt(LocalDate.now());
        queryCacheRepository.save(queryCache);

        return result.formattedAddress;
    }


//    lat = 37.4223878
//    lng = -122.0841877
//    latitude = 37.4230606
//longitude = -122.0840905

//    "1600 Amphitheatre Parkway, Mountain View, CA"
//    "P1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA"
}
