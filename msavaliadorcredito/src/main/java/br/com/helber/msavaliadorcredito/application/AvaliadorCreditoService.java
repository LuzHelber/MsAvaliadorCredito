package br.com.helber.msavaliadorcredito.application;

import br.com.helber.msavaliadorcredito.application.ex.DadosClienteNotFoundException;
import br.com.helber.msavaliadorcredito.application.ex.ErroComunicacaoMicroservicesException;
import br.com.helber.msavaliadorcredito.domain.model.*;
import br.com.helber.msavaliadorcredito.infra.clients.CartoesResourceClient;
import br.com.helber.msavaliadorcredito.infra.clients.ClienteResourceClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient clientesClient;
    private final CartoesResourceClient cartoesClient;

    public SituacaoCliente obterSituacaoCliente(String cpf)
            throws DadosClienteNotFoundException, ErroComunicacaoMicroservicesException{
        try {
            ResponseEntity<DadosCliente> dadosClienteResponse = clientesClient.dadosCliente(cpf);
            ResponseEntity<List<CartaoCliente>> cartoesResponse = cartoesClient.getCartoesByCliente(cpf);

            return SituacaoCliente
                    .builder()
                    .cliente(dadosClienteResponse.getBody())
                    .cartoes(cartoesResponse.getBody())
                    .build();
        }catch (FeignException.FeignClientException e){
            int status = e.status();
            if(HttpStatus.NOT_FOUND.value() == status){
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicroservicesException(e.getMessage(), status);
        }
    }

    public RetornoAvaliacaoCliente realizarAvaliacao(String cpf, Long renda)
            throws DadosClienteNotFoundException, ErroComunicacaoMicroservicesException {
      try{
        ResponseEntity<DadosCliente> dadosClienteResponse = clientesClient.dadosCliente(cpf);
          ResponseEntity<List<Cartao>> cartoesResponse = cartoesClient.getCartoesRendaAteh(renda);

          List<Cartao> cartoes = cartoesResponse.getBody();
        var listaCartoesAprovados = cartoes.stream().map(cartao -> {

            DadosCliente dadosCliente = dadosClienteResponse.getBody();

            BigDecimal limiteBasico = cartao.getLimiteBasico();
            BigDecimal idadeBD = BigDecimal.valueOf(dadosCliente.getIdade());
            var fator = idadeBD.divide(BigDecimal.valueOf(10));
            BigDecimal limiteAprovado = fator.multiply(limiteBasico);

            CartaoAprovado aprovado = new CartaoAprovado();
            aprovado.setCartao(cartao.getNome());
            aprovado.setBandeira(cartao.getBandeira());
            aprovado.setLimiteAprovado(limiteAprovado);

            return aprovado;
        }).collect(Collectors.toList());

            return new RetornoAvaliacaoCliente(listaCartoesAprovados);


        }catch (FeignException.FeignClientException e){
            int status = e.status();
            if(HttpStatus.NOT_FOUND.value() == status){
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicroservicesException(e.getMessage(), status);
        }
    }
}
