package group.sirenalertla.Controller;

import group.sirenalertla.Model.Siren;
import group.sirenalertla.Service.SirenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sirens")
public class SirenController {

    @Autowired
    private SirenService sirenService;

    //Hent all Sirener
    @GetMapping
    public List<Siren> getAll() {
        return sirenService.getAll();
    }
    //Opret ny Sirene
    @PostMapping
    public Siren create(@RequestBody Siren siren) {
        return sirenService.create(siren);
    }
    //Rediger en sirene
    @PutMapping("/{id}")
    public ResponseEntity<Siren> update(@PathVariable Long id, @RequestBody Siren siren) {
        return sirenService.update(id, siren)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    //Slet sirene
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sirenService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
