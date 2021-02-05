package tacos.web.api;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import tacos.Taco;

public class TacoResourceAssembler extends RepresentationModelAssemblerSupport<Taco, TacoResource>{

    public TacoResourceAssembler() {
        super(DesignTacoRestController.class, TacoResource.class);
    }

    @Override
    protected TacoResource instantiateModel(Taco taco) {
        return new TacoResource(taco);
    }

    @Override
    public TacoResource toModel(Taco taco) {
        return createModelWithId(taco.getId(), taco);
    }
}
