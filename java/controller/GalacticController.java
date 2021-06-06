package controller;


import model.Planet;
import model.Ruler;
import model.RulerPlanet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import repository.PlanetRepository;
import repository.RulerPlanetRepository;
import repository.RulerRepository;


@Controller
public class GalacticController {

    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private RulerRepository rulerRepository;

    @Autowired
    private RulerPlanetRepository rulerPlanetRepository;


    @GetMapping("/rulers")
    public String showAllRulers(Model model) {
        Iterable<Ruler> rulers = rulerRepository.findAll();
        model.addAttribute("rulers", rulers);
        return "all-rulers";
    }


    @GetMapping("/planets")
    public String showAllPlanets(Model model) {
        Iterable<Planet> planets = planetRepository.findAll();
        model.addAttribute("planets", planets);
        return "all-planets";
    }


    @GetMapping("/rulers/young")
    public String showYoungRulers(Model model) {

        Iterable<Ruler> youngRulers = rulerRepository.findTop10ByOrderByAgeAsc();

        model.addAttribute("youngRulers", youngRulers);
        return "young-rulers";
    }

    @GetMapping("/no-planet-rulers")
    public String showRulersNoPlanet(Model model) {

        long planet_id = 0;
        Iterable<RulerPlanet> rulersNoPlanet = rulerPlanetRepository
                .findAllByPlanetIdIs(planet_id);

        model.addAttribute("rulersNoPlanet", rulersNoPlanet);
        return "no-planet-rulers";
    }


    @GetMapping("/rulers/add")
    public String rulersAdd(Model model) {
        return "rulers-add";
    }


    @PostMapping(value = "/rulers/add")
    public String rulersAddPostMap(@RequestParam String name,
                                   @RequestParam int age,
                                   Model model) {

        Ruler ruler = new Ruler(name, age);
        rulerRepository.save(ruler);

        RulerPlanet rulerPlanet = new RulerPlanet(ruler.getId(), 0);
        rulerPlanetRepository.save(rulerPlanet);

        return "redirect:/";
    }


    @GetMapping("/planets/add")
    public String planetsAdd(Model model) {
        return "planets-add";
    }

    @PostMapping(value = "/planets/add")
    public String planetsAddPostMap(@RequestParam String name,
                                    Model model) {

        Planet planet = new Planet(name, 0);
        planetRepository.save(planet);


        return "redirect:/";
    }


    @GetMapping("/order")
    public String orderRulerToPlanet(Model model) {
        return "order";
    }



    @PostMapping(value = "/order")
    public String orderRulerToPlanetPost(@RequestParam long ruler_id,
                                         @RequestParam long planet_id,
                                         Model model) {

        Ruler ruler = rulerRepository.findById(ruler_id).orElseThrow();
        Planet planet = planetRepository.findById(planet_id).orElseThrow();
        RulerPlanet rulerPlanet = rulerPlanetRepository.findByRulerId(ruler_id);


        try {
            if (rulerPlanet.getPlanetId() == 0) {

                planet.setRulerId(ruler_id);
                planetRepository.save(planet);

                rulerPlanet.setPlanetId(planet_id);
                rulerPlanetRepository.save(rulerPlanet);
            }

            if (planet.getRulerId() == 0) {

                planet.setRulerId(ruler_id);
                planetRepository.save(planet);

                rulerPlanet.setRulerId(ruler_id);
                rulerPlanetRepository.save(rulerPlanet);
            }


            if (rulerPlanet.getPlanetId() != 0) {

                RulerPlanet rulerPlanet1 = rulerPlanetRepository.findByPlanetId(planet_id);
                RulerPlanet rulerPlanet2 = rulerPlanetRepository.findByRulerId(ruler_id);

                rulerPlanet1.setRulerId(rulerPlanet2.getRulerId());
                rulerPlanet2.setPlanetId(rulerPlanet1.getPlanetId());

                planet.setRulerId(ruler.getId());

                rulerPlanetRepository.save(rulerPlanet1);
                rulerPlanetRepository.save(rulerPlanet2);
                planetRepository.save(planet);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "redirect:/";
    }


    @PostMapping("/rulers/{id}/remove")
    public String rulerDeletePostMap(@PathVariable(value = "id") long id, Model model) {

        Ruler ruler = rulerRepository.findById(id).orElseThrow();
        RulerPlanet rulerPlanet = rulerPlanetRepository.findByRulerId(id);

        rulerPlanet.setRulerId(0);
        rulerRepository.delete(ruler);
        return "redirect:/";
    }

    @PostMapping("/planets/{id}/remove")
    public String planetDeletePostMap(@PathVariable(value = "id") long id, Model model) {

        Planet planet = planetRepository.findById(id).orElseThrow();
        RulerPlanet rulerPlanet = rulerPlanetRepository.findByPlanetId(id);

        rulerPlanet.setPlanetId(0);
        planetRepository.delete(planet);
        return "redirect:/";
    }


}
