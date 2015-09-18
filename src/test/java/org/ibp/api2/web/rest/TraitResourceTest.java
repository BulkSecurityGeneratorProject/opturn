package org.ibp.api2.web.rest;

import org.ibp.api2.Application;
import org.ibp.api2.domain.Trait;
import org.ibp.api2.repository.TraitRepository;
import org.ibp.api2.repository.search.TraitSearchRepository;
import org.ibp.api2.web.rest.dto.TraitDTO;
import org.ibp.api2.web.rest.mapper.TraitMapper;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TraitResource REST controller.
 *
 * @see TraitResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TraitResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";
    private static final String DEFAULT_PROPERTY = "SAMPLE_TEXT";
    private static final String UPDATED_PROPERTY = "UPDATED_TEXT";
    private static final String DEFAULT_MEASUREMENT_METHOD = "SAMPLE_TEXT";
    private static final String UPDATED_MEASUREMENT_METHOD = "UPDATED_TEXT";
    private static final String DEFAULT_SCALE = "SAMPLE_TEXT";
    private static final String UPDATED_SCALE = "UPDATED_TEXT";

    @Inject
    private TraitRepository traitRepository;

    @Inject
    private TraitMapper traitMapper;

    @Inject
    private TraitSearchRepository traitSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restTraitMockMvc;

    private Trait trait;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TraitResource traitResource = new TraitResource();
        ReflectionTestUtils.setField(traitResource, "traitRepository", traitRepository);
        ReflectionTestUtils.setField(traitResource, "traitMapper", traitMapper);
        ReflectionTestUtils.setField(traitResource, "traitSearchRepository", traitSearchRepository);
        this.restTraitMockMvc = MockMvcBuilders.standaloneSetup(traitResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        trait = new Trait();
        trait.setName(DEFAULT_NAME);
        trait.setDescription(DEFAULT_DESCRIPTION);
        trait.setProperty(DEFAULT_PROPERTY);
        trait.setMeasurementMethod(DEFAULT_MEASUREMENT_METHOD);
        trait.setScale(DEFAULT_SCALE);
    }

    @Test
    @Transactional
    public void createTrait() throws Exception {
        int databaseSizeBeforeCreate = traitRepository.findAll().size();

        // Create the Trait
        TraitDTO traitDTO = traitMapper.traitToTraitDTO(trait);

        restTraitMockMvc.perform(post("/api/traits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(traitDTO)))
                .andExpect(status().isCreated());

        // Validate the Trait in the database
        List<Trait> traits = traitRepository.findAll();
        assertThat(traits).hasSize(databaseSizeBeforeCreate + 1);
        Trait testTrait = traits.get(traits.size() - 1);
        assertThat(testTrait.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTrait.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTrait.getProperty()).isEqualTo(DEFAULT_PROPERTY);
        assertThat(testTrait.getMeasurementMethod()).isEqualTo(DEFAULT_MEASUREMENT_METHOD);
        assertThat(testTrait.getScale()).isEqualTo(DEFAULT_SCALE);
    }

    @Test
    @Transactional
    public void getAllTraits() throws Exception {
        // Initialize the database
        traitRepository.saveAndFlush(trait);

        // Get all the traits
        restTraitMockMvc.perform(get("/api/traits"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(trait.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].property").value(hasItem(DEFAULT_PROPERTY.toString())))
                .andExpect(jsonPath("$.[*].measurementMethod").value(hasItem(DEFAULT_MEASUREMENT_METHOD.toString())))
                .andExpect(jsonPath("$.[*].scale").value(hasItem(DEFAULT_SCALE.toString())));
    }

    @Test
    @Transactional
    public void getTrait() throws Exception {
        // Initialize the database
        traitRepository.saveAndFlush(trait);

        // Get the trait
        restTraitMockMvc.perform(get("/api/traits/{id}", trait.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(trait.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.property").value(DEFAULT_PROPERTY.toString()))
            .andExpect(jsonPath("$.measurementMethod").value(DEFAULT_MEASUREMENT_METHOD.toString()))
            .andExpect(jsonPath("$.scale").value(DEFAULT_SCALE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTrait() throws Exception {
        // Get the trait
        restTraitMockMvc.perform(get("/api/traits/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTrait() throws Exception {
        // Initialize the database
        traitRepository.saveAndFlush(trait);

		int databaseSizeBeforeUpdate = traitRepository.findAll().size();

        // Update the trait
        trait.setName(UPDATED_NAME);
        trait.setDescription(UPDATED_DESCRIPTION);
        trait.setProperty(UPDATED_PROPERTY);
        trait.setMeasurementMethod(UPDATED_MEASUREMENT_METHOD);
        trait.setScale(UPDATED_SCALE);
        
        TraitDTO traitDTO = traitMapper.traitToTraitDTO(trait);

        restTraitMockMvc.perform(put("/api/traits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(traitDTO)))
                .andExpect(status().isOk());

        // Validate the Trait in the database
        List<Trait> traits = traitRepository.findAll();
        assertThat(traits).hasSize(databaseSizeBeforeUpdate);
        Trait testTrait = traits.get(traits.size() - 1);
        assertThat(testTrait.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTrait.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTrait.getProperty()).isEqualTo(UPDATED_PROPERTY);
        assertThat(testTrait.getMeasurementMethod()).isEqualTo(UPDATED_MEASUREMENT_METHOD);
        assertThat(testTrait.getScale()).isEqualTo(UPDATED_SCALE);
    }

    @Test
    @Transactional
    public void deleteTrait() throws Exception {
        // Initialize the database
        traitRepository.saveAndFlush(trait);

		int databaseSizeBeforeDelete = traitRepository.findAll().size();

        // Get the trait
        restTraitMockMvc.perform(delete("/api/traits/{id}", trait.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Trait> traits = traitRepository.findAll();
        assertThat(traits).hasSize(databaseSizeBeforeDelete - 1);
    }
}
