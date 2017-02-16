package com.routes.service.web;

import com.routes.service.entities.Route;
import com.routes.service.interactors.RouteInteractor;
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

    @PostMapping(path = "/routes")
    public void saveRoute(@RequestBody Route route) {
        routeInteractor.saveRoute(route);
    }
}