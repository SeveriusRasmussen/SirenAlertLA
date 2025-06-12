package group.sirenalertla.Service;

import group.sirenalertla.Model.Fire;
import group.sirenalertla.Model.Siren;
import group.sirenalertla.Model.Status;
import group.sirenalertla.Repository.FireRepository;
import group.sirenalertla.Repository.SirenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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

    public void closeFire(Long fireId) {
        Fire fire = fireRepo.findById(fireId).orElseThrow();
        fire.setClosed(true);

        fire.getSirens().forEach(s -> s.setStatus(Status.FRED));
        sirenRepo.saveAll(fire.getSirens());

        fireRepo.save(fire);
    }
}
