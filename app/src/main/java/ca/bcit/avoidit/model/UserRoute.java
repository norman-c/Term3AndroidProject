package ca.bcit.avoidit.model;

import java.time.LocalTime;
import java.util.ArrayList;

/**
 * A user-defined route.
 * Goes from Point A (start) to Point B (end).
 * Has defined days to notify the user, a defined time, and a flag to ask
 * whether it should make notifications or not.
 */
public class UserRoute {
    private String routeName;
    private String routePointA;
    private String routePointB;
    private LocalTime notificationTime;
    private ArrayList<Boolean> notificationDays; //index 0 = sunday, index 1 = monday, index 6 = saturday
    private Boolean notificationEnabled;

    public UserRoute() {}

    public UserRoute(String inputName, String inputPointA, String inputPointB,
                     LocalTime inputTime, ArrayList<Boolean> inputDays, Boolean inputEnabled) {
        this.routeName = inputName;
        this.routePointA = inputPointA;
        this.routePointB = inputPointB;
        this.notificationTime = inputTime;
        this.notificationDays = inputDays;
        this.notificationEnabled = inputEnabled;
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

    public LocalTime getNotificationTime() {
        return notificationTime;
    }

    public ArrayList<Boolean> getNotificationDays() {
        return notificationDays;
    }

    public Boolean getNotificationEnabled() {
        return notificationEnabled;
    }
}
