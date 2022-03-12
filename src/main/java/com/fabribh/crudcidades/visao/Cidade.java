package com.fabribh.crudcidades.visao;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public final class Cidade {

    @NotBlank(message = "{app.nome.blank}")
    @Size(min = 5, max = 60, message = "{app.nome.size}")
    private final String nome;

    @NotBlank(message = "{app.estado.blank}")
    @Size(min = 2, max = 2, message = "{app.estado.size}")
    private final String estado;

    public Cidade(final String nome, final String estado) {
        this.nome = nome;
        this.estado = estado;
    }

    public String getNome() {
        return nome;
    }

    public String getEstado() {
        return estado;
    }

}
