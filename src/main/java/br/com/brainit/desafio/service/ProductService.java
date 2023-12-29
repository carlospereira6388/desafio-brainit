package br.com.brainit.desafio.service;

import java.util.List;

import br.com.brainit.desafio.dto.ProductDataDTO;
import br.com.brainit.desafio.dto.ProductResponseDTO;

public interface ProductService {
    ProductResponseDTO addProduct(ProductDataDTO productDTO);
    List<ProductResponseDTO> getAllProducts();
    ProductResponseDTO getProduct(Long id);
    ProductResponseDTO updateProduct(Long id, ProductDataDTO productDTO);
    void deleteProduct(Long id);
}