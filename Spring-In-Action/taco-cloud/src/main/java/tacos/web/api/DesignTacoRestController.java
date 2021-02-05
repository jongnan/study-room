package tacos.web.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tacos.Taco;
import tacos.data.TacoRepository;

import java.util.List;


@RepositoryRestController
@RequiredArgsConstructor
public class DesignTacoRestController {
    private final TacoRepository tacoRepo;

    @GetMapping(path = "/tacos/recent", produces = "application/hal+json")
    public ResponseEntity<CollectionModel<TacoResource>> recentTacos() {
        PageRequest page = PageRequest.of(0, 12, Sort.by("createdAt")
                                        .descending());

        List<Taco> tacos = tacoRepo.findAll(page).getContent();

        CollectionModel<TacoResource> recentResources =
                CollectionModel.of(new TacoResourceAssembler().toCollectionModel(tacos));

        recentResources.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(DesignTacoRestController.class).recentTacos()
                ).withRel("recents"));
        return new ResponseEntity<>(recentResources, HttpStatus.OK);
    }
}
