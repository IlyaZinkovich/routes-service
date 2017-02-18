package com.routes.service.interactor;

import com.routes.service.entity.Route;
import com.routes.service.gateway.RouteGateway;
import com.routes.service.boundary.RoutesBatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void saveRoutesBatch(RoutesBatch routesBatch) {
        routesBatch.getRoutes().forEach(routeGateway::save);
    }
}
