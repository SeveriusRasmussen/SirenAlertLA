package group.sirenalertla.Service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Test til Santa Monica til Pacific Palisades.
/* Afstand: cirka 2.86 km
Santa Monica: Latitude=34.0195, Longitude=-118.4912
Pacific Palisades: Latitude=34.0356, Longitude=-118.5154
*/

class DistanceServiceTest {

    //Bruges til test afstand mellem to koordinater
    @Test
    void testCalculateDistanceKM() {
        DistanceService distanceService = new DistanceService();

        double lat1 = 34.0195;
        double lon1 = -118.4912;
        double lat2 = 34.0356;
        double lon2 = -118.5154;

        double distance = distanceService.calculateDistanceKM(lat1, lon1, lat2, lon2);

        System.out.println("Distance: " + distance + " km");

        // Vi ved det er cirka 2.8 km - lad os tillade lidt tolerance.
        //assertTrue er boolean svar om resultat er sandt eller ej.
        assertTrue(distance > 2.5 && distance < 3.2);
    }
}