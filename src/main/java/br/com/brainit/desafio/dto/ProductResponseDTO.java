package br.com.brainit.desafio.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductResponseDTO {

    @ApiModelProperty(position = 0)
    private Long id;

    @ApiModelProperty(position = 1)
    private String name;

    @ApiModelProperty(position = 2)
    private String description;

    @ApiModelProperty(position = 3)
    private double price;
}