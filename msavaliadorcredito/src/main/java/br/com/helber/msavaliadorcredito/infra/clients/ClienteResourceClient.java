package br.com.helber.msavaliadorcredito.infra.clients;

import br.com.helber.msavaliadorcredito.domain.model.DadosCleinte;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "msclientes", path = "/clientes")
public interface ClienteResourceClient {

    @GetMapping(params = "cpf")
    ResponseEntity<DadosCleinte> dadosCliente(@RequestParam("cpf") String cpf);
}
