package com.fabribh.crudcidades.cidade;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class CidadeController {

    private Set<Cidade> cidades;

    private CidadeRepository cidadeRepository;

    public CidadeController(CidadeRepository cidadeRepository) {
        this.cidadeRepository = cidadeRepository;
        cidades = new HashSet<>();
    }

    @GetMapping("/")
    public String listar(Model memoria) {

        memoria.addAttribute("listaCidades", cidadeRepository
                                                .findAll()
                                                .stream()
                                                .map(cidade -> new Cidade(cidade.getNome(),cidade.getEstado()))
                                                .collect(Collectors.toList()));

        return "/crud";
    }

    @PostMapping("/criar")
    public String criar(@Valid Cidade cidade, BindingResult validacao, Model memoria) {

        if (validacao.hasErrors()) {
            validacao
                    .getFieldErrors()
                    .forEach(error ->
                            memoria.addAttribute(
                                    error.getField(),
                                    error.getDefaultMessage())
                    );

            memoria.addAttribute("nomeInformado", cidade.getNome());
            memoria.addAttribute("estadoInformado", cidade.getEstado());
            memoria.addAttribute("listaCidades", cidades);

            return ("/crud");
        } else {
            cidadeRepository.save(cidade.clonar());
        }

        return "redirect:/";
    }

    @GetMapping("/excluir")
    public String excluir(
            @RequestParam String nome,
            @RequestParam String estado) {

        var cidadeEncontrada = cidadeRepository.findByNomeAndEstado(nome, estado);

        cidadeEncontrada.ifPresent(cidadeRepository::delete);

        return "redirect:/";
    }

    @GetMapping("/preparaAlterar")
    public String preparaAlterar(
            @RequestParam String nome,
            @RequestParam String estado,
            Model memoria) {

        var cidadeAtual = cidadeRepository.findByNomeAndEstado(nome, estado);

        cidadeAtual.ifPresent(cidadeEntidade -> {
            memoria.addAttribute("cidadeAtual", cidadeEntidade);
            memoria.addAttribute("listaCidades", cidadeRepository.findAll());
        });
        return "/crud";
    }

    @PostMapping("/alterar")
    public String alterar(
            @RequestParam String nomeAtual,
            @RequestParam String estadoAtual,
            Cidade cidade) {

        var cidadeAtual = cidadeRepository.findByNomeAndEstado(nomeAtual, estadoAtual);

        if(cidadeAtual.isPresent()) {
            var cidadeEncontrada = cidadeAtual.get();
            cidadeEncontrada.setNome(cidade.getNome());
            cidadeEncontrada.setEstado(cidade.getEstado());

            cidadeRepository.saveAndFlush(cidadeEncontrada);
        }

        return "redirect:/";
    }
}
