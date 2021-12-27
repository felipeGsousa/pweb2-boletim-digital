package br.edu.ifpb.pweb2.boletimDigital.controller;

import br.edu.ifpb.pweb2.boletimDigital.model.Estudante;
import br.edu.ifpb.pweb2.boletimDigital.repository.EstudanteRepository;
import br.edu.ifpb.pweb2.boletimDigital.utils.MediaCalc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.validation.Valid;

import java.math.BigDecimal;


import java.util.Locale;


@Controller
@RequestMapping("/estudantes")
public class EstudanteController {

    @Autowired
    private EstudanteRepository estudanteRepository;

    @Autowired
    private MediaCalc mediaCalc;

    private Integer idEst;

    @RequestMapping("/form")
    public ModelAndView getForm(Estudante estudante, ModelAndView modelAndView){
        modelAndView.setViewName("/estudantes/form");
        modelAndView.addObject("estudante", estudante);
        return modelAndView;
    }

    @RequestMapping(value = "/relatorio", method = RequestMethod.GET)
    public ModelAndView getRelatorio(ModelAndView modelAndView){
        modelAndView.addObject("estudantes", estudanteRepository.findAll(Sort.by(Sort.Direction.ASC, "nome")));
        modelAndView.setViewName("/estudantes/relatorio");
        return modelAndView;
    }


    @RequestMapping("/estudante/{id}")
    public ModelAndView getEstudante(@PathVariable(value = "id") Integer id,Estudante estudante, ModelAndView modelAndView){
        estudante = estudanteRepository.getById(id);
        idEst = id;
        modelAndView.setViewName("/estudantes/estudante");
        modelAndView.addObject("estudante", estudante);
        return modelAndView;
    }

    @RequestMapping(value = "/notas/{id}", method = RequestMethod.GET)
    public ModelAndView getNotas(@PathVariable Integer id, Estudante estudante, ModelAndView modelAndView) {
        estudante = estudanteRepository.getById(id);
        idEst = id;
        modelAndView.setViewName("/estudantes/notas");
        modelAndView.addObject("estudante", estudante);
        return modelAndView;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView save(@Valid Estudante estudante, BindingResult result, ModelAndView modelAndView, RedirectAttributes redirectAttributes){
        if (result.hasErrors()) {
            modelAndView.addObject("estudante", estudante);
            modelAndView.setViewName("/estudante/form");
            return modelAndView;
        }

        estudante.setNome(estudante.getNome().toUpperCase(Locale.ROOT));

        defineSituacao(estudante, mediaCalc.media(estudante));
        estudanteRepository.save(estudante);

        redirectAttributes.addFlashAttribute("mensagem", "Estudante cadastrado com sucesso!");
        modelAndView.setViewName("redirect:/estudantes/list");
        return modelAndView;
    }
    
    @RequestMapping(value = "/salvanotas", method = RequestMethod.POST)
    public ModelAndView notas(@Valid Estudante estudante,
                        BindingResult result,ModelAndView modelAndView,
                        RedirectAttributes redirectAttributes
                        ){


        if (result.hasErrors()){
            modelAndView.addObject("estudante", estudante);
            modelAndView.setViewName("/estudantes/notas");
            return modelAndView;
        }



        Estudante estudanteEdit = estudanteRepository.getById(idEst);

        if (estudante.getNota1() != null) {
            estudanteEdit.setNota1(estudante.getNota1());
        }
        if (estudante.getNota2() != null) {
            estudanteEdit.setNota2(estudante.getNota2());
        }
        if (estudante.getNota3() != null) {
            estudanteEdit.setNota3(estudante.getNota3());
        }
        if (estudante.getNotaFinal() != null) {
            estudanteEdit.setNotaFinal(estudante.getNotaFinal());
        }
        if (estudante.getFaltas() != null) {
            estudanteEdit.setFaltas(estudante.getFaltas());
        }

        defineSituacao(estudanteEdit, mediaCalc.media(estudanteEdit));
        estudanteRepository.save(estudanteEdit);

        redirectAttributes.addFlashAttribute("mensagem", "Notas adicionadas!");
        modelAndView.setViewName("redirect:/estudantes/list");
        return modelAndView;
    }

    @RequestMapping(value = "/editar", method = RequestMethod.POST)
    public ModelAndView editar(@Valid Estudante estudante,
                         BindingResult result,ModelAndView modelAndView,
                         RedirectAttributes redirectAttributes){


        if (result.hasErrors()){
            modelAndView.addObject("estudante", estudante);
            modelAndView.setViewName("/estudantes/estudante");
            return modelAndView;
        }

        Estudante estudanteSalvo = estudanteRepository.getById(idEst);

        estudanteSalvo.setNome(estudante.getNome().toUpperCase(Locale.ROOT));
        estudanteSalvo.setNascimento(estudante.getNascimento());

        estudanteRepository.save(estudanteSalvo);

        redirectAttributes.addFlashAttribute("mensagem", "Dados do estudante foram editados!");
        modelAndView.setViewName("redirect:/estudantes/list");
        return modelAndView;
    }

    @RequestMapping("/delete/{id}")
    public ModelAndView apagaEstudante(@PathVariable(value = "id") Integer id, ModelAndView modelAndView, RedirectAttributes attr) {
        estudanteRepository.deleteById(id);
        attr.addFlashAttribute("mensagem", "Estudante removido com sucesso!");
        modelAndView.setViewName("redirect:/estudantes/list");
        return modelAndView;
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public ModelAndView listAll(ModelAndView modelAndView){
        modelAndView.addObject("estudantes", estudanteRepository.findAll(Sort.by(Sort.Direction.ASC, "nome")));
        modelAndView.setViewName("/estudantes/list");
        return modelAndView;
    }

    public void defineSituacao(Estudante estudante, BigDecimal media){
        boolean faltaNota = false;

        if(estudante.getNota1() == null || estudante.getNota2() == null || estudante.getNota3() == null){
            faltaNota = true;
        }

        if (media.compareTo(new BigDecimal(101)) == -1) {
            if (estudante.getFaltas()!= null && estudante.getFaltas() >= 25) {
                estudante.setSituacao(Estudante.EnumSituacao.RF);
            } else if (faltaNota && estudante.getFaltas() != null || faltaNota && estudante.getFaltas() == null || !faltaNota && estudante.getFaltas() == null) {
                estudante.setSituacao(Estudante.EnumSituacao.MT);
            } else if (media.compareTo(new BigDecimal(69)) == 1 && !faltaNota && estudante.getFaltas() != null) {
                estudante.setSituacao(Estudante.EnumSituacao.AP);
            } else if (media.compareTo(new BigDecimal(40)) == -1 && !faltaNota && estudante.getFaltas() != null) {
                estudante.setSituacao(Estudante.EnumSituacao.RP);
            } else if (estudante.getNotaFinal() != null && media.multiply(new BigDecimal(60)).add(estudante.getNotaFinal().multiply(new BigDecimal(40))).divide(new BigDecimal(100)).compareTo(new BigDecimal(49.999)) == 1) {
                estudante.setSituacao(Estudante.EnumSituacao.AP);
            } else if (media.compareTo(new BigDecimal(39)) == 1 && media.compareTo(new BigDecimal(70)) == -1 && !faltaNota && estudante.getFaltas() != null && estudante.getNotaFinal() == null) {
                estudante.setSituacao(Estudante.EnumSituacao.FN);
            } else {
                estudante.setSituacao(Estudante.EnumSituacao.RP);
            }
        } else {
            estudante.setSituacao(Estudante.EnumSituacao.MT);
        }
        estudanteRepository.save(estudante);
    }
}
