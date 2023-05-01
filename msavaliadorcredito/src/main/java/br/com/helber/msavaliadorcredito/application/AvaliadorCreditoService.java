package br.com.helber.msavaliadorcredito.application;

import br.com.helber.msavaliadorcredito.domain.model.DadosCleinte;
import br.com.helber.msavaliadorcredito.domain.model.SituacaoCliente;
import br.com.helber.msavaliadorcredito.infra.clients.ClienteResourceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient clientesClient;

    public SituacaoCliente obterSituacaoCliente(String cpf){

        ResponseEntity<DadosCleinte> dadosClienteResponse = clientesClient.dadosCliente(cpf);

        return SituacaoCliente
                .builder()
                .cliente(dadosClienteResponse.getBody())
                .build();


    }
}
