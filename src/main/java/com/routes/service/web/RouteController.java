package com.routes.service.web;

import com.routes.service.entity.Route;
import com.routes.service.interactor.RouteInteractor;
import com.routes.service.boundary.RoutesBatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RouteController {

    @Autowired
    private RouteInteractor routeInteractor;

    @GetMapping(path = "/routes")
    public List<Route> findRoutes(@RequestParam(name = "country") String country) {
        return routeInteractor.getRoutes(country);
    }

    @PostMapping(path = "/routes/batch")
    public void saveRoute(@RequestBody RoutesBatch routesBatch) {
        routeInteractor.saveRoutesBatch(routesBatch);
    }
}