package tacos.web.api;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import tacos.Ingredient;

public class IngredientResourceAssembler extends
        RepresentationModelAssemblerSupport<Ingredient, IngredientResource> {

    public IngredientResourceAssembler() {
        super(IngredientRestController.class, IngredientResource.class);
    }

    @Override
    protected IngredientResource instantiateModel(Ingredient ingredient) {
        return createModelWithId(ingredient.getId(), ingredient);
    }

    @Override
    public IngredientResource toModel(Ingredient ingredient) {
        return new IngredientResource(ingredient);
    }
}
