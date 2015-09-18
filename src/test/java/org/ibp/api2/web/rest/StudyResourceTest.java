package org.ibp.api2.web.rest;

import org.ibp.api2.Application;
import org.ibp.api2.domain.Study;
import org.ibp.api2.repository.StudyRepository;
import org.ibp.api2.repository.search.StudySearchRepository;
import org.ibp.api2.web.rest.dto.StudyDTO;
import org.ibp.api2.web.rest.mapper.StudyMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.joda.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.ibp.api2.domain.enumeration.StudyType;

/**
 * Test class for the StudyResource REST controller.
 *
 * @see StudyResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class StudyResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";
    private static final String DEFAULT_OBJECTIVE = "SAMPLE_TEXT";
    private static final String UPDATED_OBJECTIVE = "UPDATED_TEXT";

    private static final StudyType DEFAULT_TYPE = StudyType.NURSERY;
    private static final StudyType UPDATED_TYPE = StudyType.TRIAL;

    private static final LocalDate DEFAULT_START_DATE = new LocalDate(0L);
    private static final LocalDate UPDATED_START_DATE = new LocalDate();

    private static final LocalDate DEFAULT_END_DATE = new LocalDate(0L);
    private static final LocalDate UPDATED_END_DATE = new LocalDate();

    @Inject
    private StudyRepository studyRepository;

    @Inject
    private StudyMapper studyMapper;

    @Inject
    private StudySearchRepository studySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restStudyMockMvc;

    private Study study;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StudyResource studyResource = new StudyResource();
        ReflectionTestUtils.setField(studyResource, "studyRepository", studyRepository);
        ReflectionTestUtils.setField(studyResource, "studyMapper", studyMapper);
        ReflectionTestUtils.setField(studyResource, "studySearchRepository", studySearchRepository);
        this.restStudyMockMvc = MockMvcBuilders.standaloneSetup(studyResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        study = new Study();
        study.setName(DEFAULT_NAME);
        study.setDescription(DEFAULT_DESCRIPTION);
        study.setObjective(DEFAULT_OBJECTIVE);
        study.setType(DEFAULT_TYPE);
        study.setStartDate(DEFAULT_START_DATE);
        study.setEndDate(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void createStudy() throws Exception {
        int databaseSizeBeforeCreate = studyRepository.findAll().size();

        // Create the Study
        StudyDTO studyDTO = studyMapper.studyToStudyDTO(study);

        restStudyMockMvc.perform(post("/api/studys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(studyDTO)))
                .andExpect(status().isCreated());

        // Validate the Study in the database
        List<Study> studys = studyRepository.findAll();
        assertThat(studys).hasSize(databaseSizeBeforeCreate + 1);
        Study testStudy = studys.get(studys.size() - 1);
        assertThat(testStudy.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStudy.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testStudy.getObjective()).isEqualTo(DEFAULT_OBJECTIVE);
        assertThat(testStudy.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testStudy.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testStudy.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void getAllStudys() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        // Get all the studys
        restStudyMockMvc.perform(get("/api/studys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(study.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].objective").value(hasItem(DEFAULT_OBJECTIVE.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    public void getStudy() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

        // Get the study
        restStudyMockMvc.perform(get("/api/studys/{id}", study.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(study.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.objective").value(DEFAULT_OBJECTIVE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStudy() throws Exception {
        // Get the study
        restStudyMockMvc.perform(get("/api/studys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStudy() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

		int databaseSizeBeforeUpdate = studyRepository.findAll().size();

        // Update the study
        study.setName(UPDATED_NAME);
        study.setDescription(UPDATED_DESCRIPTION);
        study.setObjective(UPDATED_OBJECTIVE);
        study.setType(UPDATED_TYPE);
        study.setStartDate(UPDATED_START_DATE);
        study.setEndDate(UPDATED_END_DATE);
        
        StudyDTO studyDTO = studyMapper.studyToStudyDTO(study);

        restStudyMockMvc.perform(put("/api/studys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(studyDTO)))
                .andExpect(status().isOk());

        // Validate the Study in the database
        List<Study> studys = studyRepository.findAll();
        assertThat(studys).hasSize(databaseSizeBeforeUpdate);
        Study testStudy = studys.get(studys.size() - 1);
        assertThat(testStudy.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStudy.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testStudy.getObjective()).isEqualTo(UPDATED_OBJECTIVE);
        assertThat(testStudy.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testStudy.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testStudy.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void deleteStudy() throws Exception {
        // Initialize the database
        studyRepository.saveAndFlush(study);

		int databaseSizeBeforeDelete = studyRepository.findAll().size();

        // Get the study
        restStudyMockMvc.perform(delete("/api/studys/{id}", study.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Study> studys = studyRepository.findAll();
        assertThat(studys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
