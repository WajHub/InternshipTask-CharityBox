package pl.wajhub.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.wajhub.server.service.CollectionBoxService;


@WebMvcTest(CollectionBoxController.class)
public class CollectionBoxControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CollectionBoxService collectionBoxService;

    @Autowired
    private ObjectMapper objectMapper;


}