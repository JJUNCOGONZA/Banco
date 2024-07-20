package com.banco.usuarios.service.dto;

import com.banco.usuarios.service.model.Cliente;

public class ClienteResponseDTO {

    private Integer idCliente;
    private Integer personaId;
    private String contrasena;
    private Boolean estado;

    public ClienteResponseDTO(Cliente cliente) {
        this.idCliente = cliente.getIdCliente();
        this.personaId = cliente.getPersona().getIdPersona();
        this.contrasena = cliente.getContrasena();
        this.estado = cliente.getEstado();
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Integer getPersonaId() {
        return personaId;
    }

    public void setPersonaId(Integer personaId) {
        this.personaId = personaId;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    

}
