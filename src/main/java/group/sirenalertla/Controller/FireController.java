package group.sirenalertla.Controller;

import group.sirenalertla.Model.Fire;
import group.sirenalertla.Repository.FireRepository;
import group.sirenalertla.Service.FireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/fires")
public class FireController {

    @Autowired
    private FireService fireService;


    // Opret ny brand og aktivér sirener
    @PostMapping
    public Fire createFire(@RequestParam double latitude, @RequestParam double longitude) {
        return fireService.createFire(latitude, longitude);
    }

    // Vis aktive brande
    @GetMapping
    public List<Fire> getActiveFires(@RequestParam(required = false) String status) {
        if ("active".equalsIgnoreCase(status)) {
            return fireService.getActiveFires();
        }
        return List.of(); // Evt. tom liste for andre statusser.
    }

    // Luk en brand og sæt sirener til FRED
    @PutMapping("/{id}/closure")
    public ResponseEntity<Void> closeFire(@PathVariable Long id) {
        fireService.closeFire(id);
        return ResponseEntity.noContent().build();
    }


    //_____ Ekstra funktioner _______
    /*
    Hvis man vil se forrige slukket brandene,
    så skal man ikke slette brande.
    */
    // Slette en brand med ændring til dens relation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFire(@PathVariable Long id) {
        fireService.deleteFire(id);
        return ResponseEntity.noContent().build();
    }
    // Slette alle brande med ændring til deres relation
    @DeleteMapping("/before-date")
    public ResponseEntity<Void> deleteFiresBeforeOrOn(@RequestParam String date) {
        // Forvent format: yyyy-MM-dd
        LocalDate parsedDate = LocalDate.parse(date);
        fireService.deleteFiresBeforeOrOn(parsedDate);
        return ResponseEntity.noContent().build();
    }
}
