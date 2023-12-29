package br.com.brainit.desafio.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.brainit.desafio.dto.ProductDataDTO;
import br.com.brainit.desafio.dto.ProductResponseDTO;
import br.com.brainit.desafio.exception.CustomException;
import br.com.brainit.desafio.model.Product;
import br.com.brainit.desafio.repository.ProductRepository;
import br.com.brainit.desafio.utils.Mapper;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private Mapper mapper;

    public ProductResponseDTO addProduct(ProductDataDTO productDTO) {
        try {
            Product product = mapper.convert(productDTO, Product.class);
            product = productRepository.save(product);
            return mapper.convert(product, ProductResponseDTO.class);
        } catch (Exception e) {
            throw new CustomException("Erro ao adicionar produto", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<ProductResponseDTO> getAllProducts() {
        try {
            return productRepository.findAll().stream()
                    .map(product -> mapper.convert(product, ProductResponseDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("Erro ao recuperar produtos", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ProductResponseDTO getProduct(Long id) {
        try {
            Product product = productRepository.findById(id).orElseThrow(() ->
                    new CustomException("Produto não encontrado", HttpStatus.NOT_FOUND));
            return mapper.convert(product, ProductResponseDTO.class);
        } catch (CustomException ce) {
            throw ce;
        } catch (Exception e) {
            throw new CustomException("Erro ao recuperar produto", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ProductResponseDTO updateProduct(Long id, ProductDataDTO productDTO) {
        try {
            Product product = productRepository.findById(id).orElseThrow(() ->
                    new CustomException("Produto não encontrado", HttpStatus.NOT_FOUND));
            product = mapper.convert(productDTO, Product.class);
            product.setId(id);
            product = productRepository.save(product);
            return mapper.convert(product, ProductResponseDTO.class);
        } catch (CustomException ce) {
            throw ce;
        } catch (Exception e) {
            throw new CustomException("Erro ao atualizar produto", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteProduct(Long id) {
        try {
            if (!productRepository.existsById(id)) {
                throw new CustomException("Produto não encontrado", HttpStatus.NOT_FOUND);
            }
            productRepository.deleteById(id);
        } catch (CustomException ce) {
            throw ce;
        } catch (Exception e) {
            throw new CustomException("Erro ao excluir produto", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
