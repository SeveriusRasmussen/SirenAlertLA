package group.sirenalertla.Service;

import group.sirenalertla.Model.Siren;
import group.sirenalertla.Repository.SirenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SirenService {

    @Autowired
    private SirenRepository sirenRepo;
    //Her slipper man for at skrive variable og konstruktør.

    public List<Siren> getAll() {
        return sirenRepo.findAll();
    }

    public Siren create(Siren siren) {
        return sirenRepo.save(siren);
    }

    public Optional<Siren> update(Long id, Siren updated) {
        return sirenRepo.findById(id).map(existing -> {
            existing.setLatitude(updated.getLatitude());
            existing.setLongitude(updated.getLongitude());
            existing.setStatus(updated.getStatus());
            existing.setDisabled(updated.isDisabled());
            return sirenRepo.save(existing);
        });
    }

    public void delete(Long id) {
        try {
            sirenRepo.deleteById(id);
        } catch (Exception e) {
            System.out.println("Siren delete failed");
        }
    }
}
