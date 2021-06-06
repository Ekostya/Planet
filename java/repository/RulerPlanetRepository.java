package repository;
import model.RulerPlanet;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface RulerPlanetRepository extends JpaRepository<RulerPlanet, Long> {

    List<RulerPlanet> findAllByPlanetIdIs(Long planet_id);

    RulerPlanet findByRulerId(Long ruler_id);

    RulerPlanet findByPlanetId(Long planet_id);



}