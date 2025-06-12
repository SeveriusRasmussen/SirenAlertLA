package group.sirenalertla.Controller;

import group.sirenalertla.Model.Fire;
import group.sirenalertla.Service.FireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    //IDÉ Slet en brand?
    /*
    Hvis man vil se forrige slukket brandene,
    så skal man ikke slette brande.
     */
}
