package br.com.brainit.desafio.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.brainit.desafio.dto.ProductDataDTO;
import br.com.brainit.desafio.dto.ProductResponseDTO;
import br.com.brainit.desafio.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping("/products")
@Api(tags = "products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    @ApiOperation(
        value = "Adicionar um novo produto",
        response = ProductResponseDTO.class,
        authorizations = {@Authorization(value="apiKey")}
    )
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Produto criado com sucesso"),
        @ApiResponse(code = 400, message = "Dados inválidos do produto")
    })
    public ResponseEntity<ProductResponseDTO> addProduct(@Valid @RequestBody ProductDataDTO productDataDTO) {
        ProductResponseDTO product = productService.addProduct(productDataDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }
    
    @GetMapping
    @ApiOperation(
        value = "Listar todos os produtos",
        response = ProductResponseDTO.class,
        responseContainer = "List",
        authorizations = {@Authorization(value="apiKey")}
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Busca realizada com sucesso")
    })
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/{id}")
    @ApiOperation(
        value = "Obter um produto pelo ID",
        response = ProductResponseDTO.class,
        authorizations = {@Authorization(value="apiKey")}
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Produto encontrado"),
        @ApiResponse(code = 404, message = "Produto não encontrado")
    })
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable Long id) {
        ProductResponseDTO product = productService.getProduct(id);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    @ApiOperation(
        value = "Atualizar um produto",
        response = ProductResponseDTO.class,
        authorizations = {@Authorization(value="apiKey")}
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Produto atualizado com sucesso"),
        @ApiResponse(code = 400, message = "Dados inválidos do produto"),
        @ApiResponse(code = 404, message = "Produto não encontrado")
    })
    public ResponseEntity<ProductResponseDTO> updateProduct(@Valid @PathVariable Long id, @RequestBody ProductDataDTO productDataDTO) {
        ProductResponseDTO product = productService.updateProduct(id, productDataDTO);
        return ResponseEntity.ok(product);
    }
    
    @DeleteMapping("/{id}")
    @ApiOperation(
        value = "Deletar um produto",
        authorizations = {@Authorization(value="apiKey")}
    )
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Produto excluído com sucesso"),
        @ApiResponse(code = 404, message = "Produto não encontrado")
    })
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
