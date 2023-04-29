package com.example.Geocoder.service;

import com.example.Geocoder.entity.QueryCache;
import com.example.Geocoder.repository.QueryCacheRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(MockitoJUnitRunner.class)
public class QueryCacheServiceTest {
    @Mock
    private QueryCacheRepository queryCacheRepository;
    @InjectMocks
    private QueryCacheService queryCacheService;

    @Test
    public void getLatLng()  {
        QueryCache queryCache = new QueryCache(1l,
                "P1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA",
                "37.4230606,-122.0840905", LocalDate.now());
        Mockito.when(queryCacheRepository.getQueryCacheByQuery(
                "P1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA"))
                .thenReturn(queryCache);
        QueryCache foundQuery = queryCacheRepository.getQueryCacheByQuery(
                "P1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA"
        );
        assertEquals("Location : 37.4230606,-122.0840905",queryCacheService.getLatLng(
                "P1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA"
        ));

    }

    @Test
   public void getAddress() {
        QueryCache queryCache = new QueryCache(1l,
                "37.4230606,-122.0840905",
                "1233 S 5th St, Mountain View, CA 91801, USA",
                LocalDate.now());
        Mockito.when(queryCacheRepository.getQueryCacheByQuery(
                "37.4230606,-122.0840905")).thenReturn(queryCache);
        QueryCache foundQuery = queryCacheRepository.getQueryCacheByQuery(
                "37.4230606,-122.0840905"
        );
        assertEquals("Address : 1233 S 5th St, Mountain View, CA 91801, USA",
                queryCacheService.getAddress(37.4230606,-122.0840905));
    }
}