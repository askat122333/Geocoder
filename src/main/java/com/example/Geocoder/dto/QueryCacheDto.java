package com.example.Geocoder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class QueryCacheDto {
    private Long id;
    private String query;
    private String response;
    private LocalDate createdAt;
}
