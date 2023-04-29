package com.example.Geocoder.repository;

import com.example.Geocoder.entity.QueryCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueryCacheRepository extends JpaRepository<QueryCache,Long> {
    QueryCache getQueryCacheByQuery(String query);
}
