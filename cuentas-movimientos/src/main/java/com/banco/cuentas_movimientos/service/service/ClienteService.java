package com.banco.cuentas_movimientos.service.service;

import com.banco.cuentas_movimientos.service.dto.ClienteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class ClienteService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${cliente.service.url}")
    private String clienteServiceUrl;

    public ClienteDTO getClienteById(Integer clienteId) {
        try {
            return restTemplate.getForObject(clienteServiceUrl + "/" + clienteId, ClienteDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Error al comunicarse con el servicio de clientes", e);
        }
    }
}

