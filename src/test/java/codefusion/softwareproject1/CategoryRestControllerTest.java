package codefusion.softwareproject1;


import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import codefusion.softwareproject1.repo.CategoryRepo;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepo categoryRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();
    }

    @Test
    public void getAllCategoriesReturnsEmptyListWhenNoCategoriesExist() throws Exception {
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // Add more tests here following the Arrange-Act-Assert pattern
}
