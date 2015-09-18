package org.ibp.api2.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.ibp.api2.domain.Study;
import org.ibp.api2.repository.StudyRepository;
import org.ibp.api2.repository.search.StudySearchRepository;
import org.ibp.api2.web.rest.util.HeaderUtil;
import org.ibp.api2.web.rest.util.PaginationUtil;
import org.ibp.api2.web.rest.dto.StudyDTO;
import org.ibp.api2.web.rest.mapper.StudyMapper;
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
 * REST controller for managing Study.
 */
@RestController
@RequestMapping("/api")
public class StudyResource {

    private final Logger log = LoggerFactory.getLogger(StudyResource.class);

    @Inject
    private StudyRepository studyRepository;

    @Inject
    private StudyMapper studyMapper;

    @Inject
    private StudySearchRepository studySearchRepository;

    /**
     * POST  /studys -> Create a new study.
     */
    @RequestMapping(value = "/studys",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StudyDTO> create(@RequestBody StudyDTO studyDTO) throws URISyntaxException {
        log.debug("REST request to save Study : {}", studyDTO);
        if (studyDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new study cannot already have an ID").body(null);
        }
        Study study = studyMapper.studyDTOToStudy(studyDTO);
        Study result = studyRepository.save(study);
        studySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/studys/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("study", result.getId().toString()))
                .body(studyMapper.studyToStudyDTO(result));
    }

    /**
     * PUT  /studys -> Updates an existing study.
     */
    @RequestMapping(value = "/studys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StudyDTO> update(@RequestBody StudyDTO studyDTO) throws URISyntaxException {
        log.debug("REST request to update Study : {}", studyDTO);
        if (studyDTO.getId() == null) {
            return create(studyDTO);
        }
        Study study = studyMapper.studyDTOToStudy(studyDTO);
        Study result = studyRepository.save(study);
        studySearchRepository.save(study);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("study", studyDTO.getId().toString()))
                .body(studyMapper.studyToStudyDTO(result));
    }

    /**
     * GET  /studys -> get all the studys.
     */
    @RequestMapping(value = "/studys",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<StudyDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Study> page = studyRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/studys", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(studyMapper::studyToStudyDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /studys/:id -> get the "id" study.
     */
    @RequestMapping(value = "/studys/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StudyDTO> get(@PathVariable Long id) {
        log.debug("REST request to get Study : {}", id);
        return Optional.ofNullable(studyRepository.findOneWithEagerRelationships(id))
            .map(studyMapper::studyToStudyDTO)
            .map(studyDTO -> new ResponseEntity<>(
                studyDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /studys/:id -> delete the "id" study.
     */
    @RequestMapping(value = "/studys/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Study : {}", id);
        studyRepository.delete(id);
        studySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("study", id.toString())).build();
    }

    /**
     * SEARCH  /_search/studys/:query -> search for the study corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/studys/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Study> search(@PathVariable String query) {
        return StreamSupport
            .stream(studySearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
