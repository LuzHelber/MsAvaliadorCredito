package br.com.helber.msavaliadorcredito.application;

import br.com.helber.msavaliadorcredito.domain.model.CartaoCliente;
import br.com.helber.msavaliadorcredito.domain.model.DadosCleinte;
import br.com.helber.msavaliadorcredito.domain.model.SituacaoCliente;
import br.com.helber.msavaliadorcredito.infra.clients.CartoesResourceClient;
import br.com.helber.msavaliadorcredito.infra.clients.ClienteResourceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient clientesClient;
    private final CartoesResourceClient cartoesClient;

    public SituacaoCliente obterSituacaoCliente(String cpf){

        ResponseEntity<DadosCleinte> dadosClienteResponse = clientesClient.dadosCliente(cpf);
        ResponseEntity<List<CartaoCliente>> cartoesResponse  = cartoesClient.getCartoesByCliente(cpf);

        return SituacaoCliente
                .builder()
                .cliente(dadosClienteResponse.getBody())
                .cartoes(cartoesResponse.getBody())
                .build();

    }
}
