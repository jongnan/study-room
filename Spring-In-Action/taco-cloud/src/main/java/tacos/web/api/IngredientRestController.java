package tacos.web.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tacos.Ingredient;
import tacos.data.IngredientRepository;

@RequestMapping(path="/ingredient", produces="application/json")
@CrossOrigin(origins="*")
@RequiredArgsConstructor
public class IngredientRestController {
    private final IngredientRepository repo;

    public Iterable<Ingredient> allIngredients() {
        return repo.findAll();
    }
}
