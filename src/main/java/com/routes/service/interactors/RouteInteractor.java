package com.routes.service.interactors;

import com.routes.service.entities.Route;
import com.routes.service.gateways.RouteGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RouteInteractor {

    @Autowired
    private RouteGateway routeGateway;

    public List<Route> getRoutes(String city, String country, LocalDate startDate, LocalDate endDate) {
        return routeGateway.findRoutes(city, country, startDate, endDate);
    }

    public List<Route> getRoutes(String country) {
        return routeGateway.findRoutes(country);
    }

    public void saveRoute(Route route) {
        routeGateway.save(route);
    }
}
