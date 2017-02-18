package com.routes.service.graph.repository;

import com.routes.service.entity.Place;
import com.routes.service.entity.Route;
import com.routes.service.gateway.RouteGateway;
import com.routes.service.graph.model.PlaceNode;
import com.routes.service.graph.model.RouteRelationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.LocalDate.parse;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

@Repository
public class RouteRepository implements RouteGateway {

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String CITY = "city";
    private static final String COUNTRY = "country";
    private static final String AFTER_DATE = "after";
    private static final String BEFORE_DATE = "before";
    private static final String MATCHING_COUNTRY = "MATCH ()-[r:ROUTE]->({country: {country}}) RETURN r";
    private static final String MATCHING_ALL_FIELDS = "MATCH ()-[r:ROUTE]->({city: {city}, country: {country}}) " +
            "WHERE r.date >= {after} AND r.date <= {before} RETURN r";
    private static final String MATCHING_PLACE = "MATCH (from {city: {city}, country:{country}}) RETURN from";

    @Autowired
    private Neo4jOperations neo4jTemplate;

    @Override
    public List<Route> findRoutes(String city, String country, LocalDate after, LocalDate before) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(CITY, city);
        parameters.put(COUNTRY, country);
        parameters.put(AFTER_DATE, after.format(ofPattern(DATE_PATTERN)));
        parameters.put(BEFORE_DATE, before.format(ofPattern(DATE_PATTERN)));
        return queryRoutes(MATCHING_ALL_FIELDS, parameters);
    }

    @Override
    public List<Route> findRoutes(String country) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(COUNTRY, country);
        return queryRoutes(MATCHING_COUNTRY, parameters);
    }

    @Override
    public void save(Route route) {
        RouteRelationship routeRelationship = convert(route);
        PlaceNode from = findPlaceIfExists(routeRelationship.getFrom());
        if (from != null) routeRelationship.setFrom(from);
        PlaceNode to = findPlaceIfExists(routeRelationship.getTo());
        if (to != null) routeRelationship.setTo(findPlaceIfExists(to));
        neo4jTemplate.save(routeRelationship);
        route.setId(routeRelationship.getRelationshipId());
    }

    private List<Route> queryRoutes(String query, Map<String, Object> parameters) {
        Iterable<RouteRelationship> routeRelationships =
                neo4jTemplate.queryForObjects(RouteRelationship.class, query, parameters);
        return stream(routeRelationships.spliterator(), false).map(this::convert).collect(toList());
    }

    private Route convert(RouteRelationship routeRelationship) {
        PlaceNode from = routeRelationship.getFrom();
        PlaceNode to = routeRelationship.getTo();
        Place fromPlace = new Place(from.getCity(), from.getCountry(), from.getLatitude(), from.getLongitude());
        Place toPlace = new Place(to.getCity(), to.getCountry(), to.getLatitude(), to.getLongitude());
        Route route = new Route(fromPlace, toPlace,
                parse(routeRelationship.getDate()), routeRelationship.getSource());
        route.setId(routeRelationship.getRelationshipId());
        return route;
    }

    private PlaceNode findPlaceIfExists(PlaceNode placeNode) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(CITY, placeNode.getCity());
        parameters.put(COUNTRY, placeNode.getCountry());
        Iterable<PlaceNode> places = neo4jTemplate.queryForObjects(PlaceNode.class, MATCHING_PLACE, parameters);
        if (places.iterator().hasNext()) {
            return places.iterator().next();
        }
        return null;
    }

    private RouteRelationship convert(Route route) {
        Place fromPlace = route.getFrom();
        PlaceNode fromPlaceNode = new PlaceNode(fromPlace.getCity(), fromPlace.getCountry(),
                fromPlace.getLatitude(), fromPlace.getLongitude());
        Place toPlace = route.getTo();
        PlaceNode toPlaceNode = new PlaceNode(toPlace.getCity(), toPlace.getCountry(),
                toPlace.getLatitude(), toPlace.getLongitude());
        return new RouteRelationship(fromPlaceNode, toPlaceNode,
                        route.getDate().format(ofPattern(DATE_PATTERN)), route.getSource());
    }
}