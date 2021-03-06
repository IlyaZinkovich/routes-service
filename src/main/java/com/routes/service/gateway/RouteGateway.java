package com.routes.service.gateway;

import com.routes.service.entity.Route;

import java.time.LocalDate;
import java.util.List;

public interface RouteGateway {

    List<Route> findRoutes(String city, String country, LocalDate after, LocalDate before);
    List<Route> findRoutes(String country);
    void save(Route route);
}
