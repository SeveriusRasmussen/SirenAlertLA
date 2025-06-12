package group.sirenalertla.Service;

import org.springframework.stereotype.Service;

@Service
public class DistanceService {

    // Radius of the Earth in kilometers
    private static final int EARTH_RADIUS_KM = 6371;

    public double calculateDistanceKM(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.pow(Math.sin(dLat / 2), 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.pow(Math.sin(dLon / 2), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }
    /*
     * Denne metode bruger Haversine-formlen til at beregne afstanden
     * mellem to punkter på en kugleflade (Jorden) ud fra deres
     * længde- og breddegrader.
     *
     * Trin:
     * 1. Omregn grader til radianer
     * 2. Beregn forskellen i latitude og longitude
     * 3. Brug Haversine-formlen:
     *    a = sin²(Δφ/2) + cos(φ1) * cos(φ2) * sin²(Δλ/2)
     *    c = 2 * atan2(√a, √(1−a))
     *    distance = R * c
     *    Hvor R = Jordens radius (6371 km)
    */
}
