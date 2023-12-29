package br.com.brainit.desafio.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.brainit.desafio.dto.AuthResponseDTO;
import br.com.brainit.desafio.dto.ProductDataDTO;
import br.com.brainit.desafio.dto.ProductResponseDTO;
import br.com.brainit.desafio.dto.UserDataDTO;
import br.com.brainit.desafio.model.Role;
import br.com.brainit.desafio.service.ProductService;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    private ProductDataDTO productDataDTO;
        
    private String adminAccessToken;
    private String userAccessToken;

    @BeforeEach
    public void setup() throws Exception {
    	productDataDTO = new ProductDataDTO();
        productDataDTO.setName("Produto Teste");
        productDataDTO.setDescription("Descrição do Produto Teste");
        productDataDTO.setPrice(100.0);
        
    	registerUser(Role.ROLE_ADMIN);
    	registerUser(Role.ROLE_USER);
    }
    
    private void registerUser(Role role) throws Exception {
        UserDataDTO userDataDTO = new UserDataDTO();
        userDataDTO.setName("Usuário " + role.name());
        userDataDTO.setEmail(UUID.randomUUID().toString() + "@teste.com");
        userDataDTO.setPassword("Senha@102030");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        userDataDTO.setRoles(roles);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDataDTO)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.auth").value(true))
                .andExpect(jsonPath("$.token").exists())
                .andDo(result -> {
                    String response = result.getResponse().getContentAsString();
                    AuthResponseDTO authResponse = objectMapper.readValue(response, AuthResponseDTO.class);
                    if (role == Role.ROLE_ADMIN) {
                        adminAccessToken = authResponse.getToken();
                    } else {
                        userAccessToken = authResponse.getToken();
                    }
                });
    }
    
    @Test
    public void testAdminCanAddProduct() throws Exception {
        when(productService.addProduct(any(ProductDataDTO.class))).thenReturn(new ProductResponseDTO());
        
        mockMvc.perform(post("/products")
                .header("Authorization", "Bearer " + adminAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDataDTO)))
                .andExpect(status().is2xxSuccessful());

        verify(productService, times(1)).addProduct(any(ProductDataDTO.class));
    }
    
    @Test
    public void testUserCannotAddProduct() throws Exception {
    	mockMvc.perform(post("/products")
                .header("Authorization", "Bearer " + userAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDataDTO)))
                .andExpect(status().isForbidden());

        verify(productService, times(0)).addProduct(any(ProductDataDTO.class));
    }

    @Test
    public void testAddProduct() throws Exception {
        when(productService.addProduct(any(ProductDataDTO.class))).thenReturn(new ProductResponseDTO());
        
        mockMvc.perform(post("/products")
                .header("Authorization", "Bearer " + adminAccessToken)
                .content(objectMapper.writeValueAsString(productDataDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        verify(productService, times(1)).addProduct(any(ProductDataDTO.class));
    }

    @Test
    public void testGetAllProducts() throws Exception {
        List<ProductResponseDTO> products = Arrays.asList(new ProductResponseDTO());

        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/products")
                .header("Authorization", "Bearer " + adminAccessToken))
                .andExpect(status().is2xxSuccessful());

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    public void testGetProductById() throws Exception {
        when(productService.getProduct(1L)).thenReturn(new ProductResponseDTO());

        mockMvc.perform(get("/products/1")
                .header("Authorization", "Bearer " + adminAccessToken))
                .andExpect(status().is2xxSuccessful());

        verify(productService, times(1)).getProduct(1L);
    }
    
    @Test
    public void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/products/1")
                .header("Authorization", "Bearer " + adminAccessToken))
                .andExpect(status().is2xxSuccessful());

        verify(productService, times(1)).deleteProduct(1L);
    }
}