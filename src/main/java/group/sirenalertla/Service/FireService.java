package group.sirenalertla.Service;

import group.sirenalertla.Model.Fire;
import group.sirenalertla.Model.Siren;
import group.sirenalertla.Model.Status;
import group.sirenalertla.Repository.FireRepository;
import group.sirenalertla.Repository.SirenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FireService {

    private final FireRepository fireRepo;
    private final SirenRepository sirenRepo;
    private final DistanceService distanceService;

    public FireService(FireRepository fireRepo, SirenRepository sirenRepo, DistanceService distanceService) {
        this.fireRepo = fireRepo;
        this.sirenRepo = sirenRepo;
        this.distanceService = distanceService;
    }

    // DTO sandkasse
    public Fire createFire(double latitude, double longitude) {
        List<Siren> nearbySirens = sirenRepo.findAll().stream()
                .filter(siren -> !siren.isDisabled() &&
                        distanceService.calculateDistanceKM(latitude, longitude,
                                siren.getLatitude(), siren.getLongitude()) <= 10)
                .collect(Collectors.toList());

        // Sæt status til FARE
        nearbySirens.forEach(s -> s.setStatus(Status.FARE));
        sirenRepo.saveAll(nearbySirens);

        Fire fire = Fire.builder()
                .latitude(latitude)
                .longitude(longitude)
                .time(LocalDateTime.now())
                .closed(false)
                .sirens(Set.copyOf(nearbySirens))
                .build();

        return fireRepo.save(fire);
    }

    public List<Fire> getActiveFires() {
        return fireRepo.findByClosedFalse();
    }

    // Slukker en brand og slå sirens fra
    public Optional<Fire> closeFire(Long fireId) {
        Optional<Fire> optionalFire = fireRepo.findById(fireId);

        if (optionalFire.isEmpty()) return Optional.empty();

        Fire fire = optionalFire.get();
        fire.setClosed(true);
        fireRepo.save(fire); // Gemmer lukning

        // Tjek hver sirene tilknyttet denne brand
        for (Siren siren : fire.getSirens()) {

            // Find ud af om denne siren stadig er tæt på en anden aktiv brand.
            boolean hasNearbyActiveFire = fireRepo.findAll().stream()
                    .filter(f -> !f.isClosed())
                    .filter(f -> !Objects.equals(f.getId(), fire.getId()))
                    .anyMatch(f -> distanceService.calculateDistanceKM(
                            f.getLatitude(), f.getLongitude(),
                            siren.getLatitude(), siren.getLongitude()
                    ) <= 10);
            // Hvis ingen andre aktive brande -> sæt til FRED
            if (!hasNearbyActiveFire) {
                siren.setStatus(Status.FRED);
            }
        }

        sirenRepo.saveAll(fire.getSirens()); // Gem ændringer på sirener

        return Optional.of(fire);
    }
    /*
//    public void closeFire(Long fireId) {
//        Fire fire = fireRepo.findById(fireId).orElseThrow();
//        fire.setClosed(true);
//
//        fire.getSirens().forEach(s -> s.setStatus(Status.FRED));
//        sirenRepo.saveAll(fire.getSirens());
//
//        fireRepo.save(fire);
    } */

    //_______ Ekstra funktioner______
    /**/
    //Slette en brand med ændring til dens relations
    public void deleteFire(Long fireId) {
        Fire fire = fireRepo.findById(fireId).orElseThrow();

        // Skift sirenens status til FRED
        fire.getSirens().forEach(s -> s.setStatus(Status.FRED));
        sirenRepo.saveAll(fire.getSirens());

        // Fjern relationen mellem brand og sirener
        fire.getSirens().clear();
        fireRepo.save(fire); // Opdatér ændringen
        fireRepo.delete(fire); // Slet branden
    }
    //Slette alle brande før og med den valgt dag.
    public void deleteFiresBeforeOrOn(LocalDate date) {
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        List<Fire> firesToDelete = fireRepo.findByTimeLessThanEqual(endOfDay);

        for (Fire fire : firesToDelete) {
            fire.getSirens().forEach(siren -> siren.setStatus(Status.FRED));
            sirenRepo.saveAll(fire.getSirens());

            fire.getSirens().clear();
            fireRepo.save(fire);
            fireRepo.delete(fire);
        }
    }
}
