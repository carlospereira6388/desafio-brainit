package br.com.brainit.desafio.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDataDTO {

    @ApiModelProperty(position = 0)
    private String name;

    @ApiModelProperty(position = 1)
    private String description;

    @ApiModelProperty(position = 2)
    private double price;
}