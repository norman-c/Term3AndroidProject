package ca.bcit.avoidit.model;

/**
 * A user-defined route.
 * Goes from Point A (start) to Point B (end).
 */
public class UserRoute {
    String routeName;
    String routePointA;
    String routePointB;

    public UserRoute() {}

    public UserRoute(String inputName, String inputPointA, String inputPointB) {
        this.routeName = inputName;
        this.routePointA = inputPointA;
        this.routePointB = inputPointB;
    }

    public String getRouteName() {
        return routeName;
    }

    public String getRoutePointA() {
        return routePointA;
    }

    public String getRoutePointB() {
        return routePointB;
    }
}
