package br.com.helber.msavaliadorcredito.domain.model;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class DadosSolicitacaoEmissaoCartao {
    private Long IdCartao;
    private String cpf;
    private String endereco;
    private BigDecimal limiteLiberado;

}
