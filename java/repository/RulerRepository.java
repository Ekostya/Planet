package repository;

import model.Ruler;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RulerRepository extends JpaRepository<Ruler, Long> {

    List<Ruler> findTop10ByOrderByAgeAsc();

    List<Ruler> findAllByName(String name);

    Ruler findByName(String name);
}