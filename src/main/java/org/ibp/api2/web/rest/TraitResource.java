package org.ibp.api2.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.ibp.api2.domain.Trait;
import org.ibp.api2.repository.TraitRepository;
import org.ibp.api2.repository.search.TraitSearchRepository;
import org.ibp.api2.web.rest.util.HeaderUtil;
import org.ibp.api2.web.rest.util.PaginationUtil;
import org.ibp.api2.web.rest.dto.TraitDTO;
import org.ibp.api2.web.rest.mapper.TraitMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Trait.
 */
@RestController
@RequestMapping("/api")
public class TraitResource {

    private final Logger log = LoggerFactory.getLogger(TraitResource.class);

    @Inject
    private TraitRepository traitRepository;

    @Inject
    private TraitMapper traitMapper;

    @Inject
    private TraitSearchRepository traitSearchRepository;

    /**
     * POST  /traits -> Create a new trait.
     */
    @RequestMapping(value = "/traits",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TraitDTO> create(@RequestBody TraitDTO traitDTO) throws URISyntaxException {
        log.debug("REST request to save Trait : {}", traitDTO);
        if (traitDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new trait cannot already have an ID").body(null);
        }
        Trait trait = traitMapper.traitDTOToTrait(traitDTO);
        Trait result = traitRepository.save(trait);
        traitSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/traits/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("trait", result.getId().toString()))
                .body(traitMapper.traitToTraitDTO(result));
    }

    /**
     * PUT  /traits -> Updates an existing trait.
     */
    @RequestMapping(value = "/traits",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TraitDTO> update(@RequestBody TraitDTO traitDTO) throws URISyntaxException {
        log.debug("REST request to update Trait : {}", traitDTO);
        if (traitDTO.getId() == null) {
            return create(traitDTO);
        }
        Trait trait = traitMapper.traitDTOToTrait(traitDTO);
        Trait result = traitRepository.save(trait);
        traitSearchRepository.save(trait);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("trait", traitDTO.getId().toString()))
                .body(traitMapper.traitToTraitDTO(result));
    }

    /**
     * GET  /traits -> get all the traits.
     */
    @RequestMapping(value = "/traits",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<TraitDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Trait> page = traitRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/traits", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(traitMapper::traitToTraitDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /traits/:id -> get the "id" trait.
     */
    @RequestMapping(value = "/traits/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TraitDTO> get(@PathVariable Long id) {
        log.debug("REST request to get Trait : {}", id);
        return Optional.ofNullable(traitRepository.findOne(id))
            .map(traitMapper::traitToTraitDTO)
            .map(traitDTO -> new ResponseEntity<>(
                traitDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /traits/:id -> delete the "id" trait.
     */
    @RequestMapping(value = "/traits/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Trait : {}", id);
        traitRepository.delete(id);
        traitSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("trait", id.toString())).build();
    }

    /**
     * SEARCH  /_search/traits/:query -> search for the trait corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/traits/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Trait> search(@PathVariable String query) {
        return StreamSupport
            .stream(traitSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
