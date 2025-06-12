package group.sirenalertla;

import group.sirenalertla.Model.Siren;
import group.sirenalertla.Model.Status;
import group.sirenalertla.Repository.SirenRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SirenAlertLaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SirenAlertLaApplication.class, args);
    }

    //Til test:
    /**/
    @Bean
    CommandLineRunner initData(SirenRepository sirenRepo) {
        return args -> {
            sirenRepo.save(Siren.builder().latitude(34.0195).longitude(-118.4912).status(Status.FRED).disabled(false).build()); // Santa Monica
            sirenRepo.save(Siren.builder().latitude(34.0356).longitude(-118.5154).status(Status.FRED).disabled(false).build()); // Pacific Palisades
            sirenRepo.save(Siren.builder().latitude(34.0230).longitude(-118.4950).status(Status.FRED).disabled(false).build()); // Santa Monica Pier
            sirenRepo.save(Siren.builder().latitude(34.0255).longitude(-118.5000).status(Status.FRED).disabled(false).build()); // Ocean Park
            sirenRepo.save(Siren.builder().latitude(34.0280).longitude(-118.5100).status(Status.FRED).disabled(false).build()); // Temescal Canyon
            sirenRepo.save(Siren.builder().latitude(34.0320).longitude(-118.5125).status(Status.FRED).disabled(false).build()); // Chautauqua Blvd
            sirenRepo.save(Siren.builder().latitude(34.0380).longitude(-118.5180).status(Status.FRED).disabled(false).build()); // Sunset Blvd
            sirenRepo.save(Siren.builder().latitude(34.0300).longitude(-118.5050).status(Status.FRED).disabled(false).build()); // Montana Ave
            sirenRepo.save(Siren.builder().latitude(34.0270).longitude(-118.5080).status(Status.FRED).disabled(false).build()); // 7th Street
            sirenRepo.save(Siren.builder().latitude(33.923217).longitude(-117.707489).status(Status.FRED).disabled(false).build()); // Rolling M Ranch Campground
            sirenRepo.save(Siren.builder().latitude(34.11105).longitude(-118.49).status(Status.FRED).disabled(false).build()); // TestPosition
        };
        /*
        Hvis man bruger :
            Latitude = 34.02
            Longitude = -118.49
        Så alle andre sirener i nærhed indenfor 10 km.
            Santa Monica: 0,1km
            Pacific Palisades: 3km
            Santa Monica Pier: 0.5km
            Ocean Park: 1.0km
            Temescal Canyon: 2.3km
            Chautauqua Blvd: 2.8km
            Sunset Blvd: 3.5km
            Montana Ave: 1.9km
            7th Street: 1.4km
            Rolling M Ranch Campground: 73.16km.
            Testposition: lidt over 10 km nord.
        */
    }
}
