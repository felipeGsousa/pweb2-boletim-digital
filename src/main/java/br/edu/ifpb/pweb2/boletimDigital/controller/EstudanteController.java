package br.edu.ifpb.pweb2.boletimDigital.controller;

import br.edu.ifpb.pweb2.boletimDigital.model.Estudante;
import br.edu.ifpb.pweb2.boletimDigital.repository.EstudanteRepository;
import br.edu.ifpb.pweb2.boletimDigital.utils.MediaCalc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import java.util.Map;

@Controller
@RequestMapping("/estudantes")
public class EstudanteController {

    @Autowired
    private EstudanteRepository estudanteRepository;

    @Autowired
    private MediaCalc mediaCalc;

    @RequestMapping("/form")
    public String getForm(Estudante estudante, Model model){
        model.addAttribute("estudante", estudante);
        return "estudantes/form";
    }

    @RequestMapping(value = "/relatorio", method = RequestMethod.GET)
    public ModelAndView getRelatorio(ModelAndView modelAndView){
        modelAndView.addObject("estudantes", estudanteRepository.findAll());
        modelAndView.setViewName("estudantes/relatorio");
        return modelAndView;
    }


    @RequestMapping("/estudante/{id}")
    public String getEstudante(@PathVariable(value = "id") Integer id, Model model){
        Estudante estudante = estudanteRepository.getById(id);
        model.addAttribute("estudante", estudante);
        return "estudantes/estudante";
    }

    @RequestMapping("/notas/{id}")
    public String getNotas(@PathVariable(value = "id") Integer id, Model model) {
        Estudante estudante = estudanteRepository.getById(id);
        model.addAttribute("estudante", estudante);
        return "estudantes/notas";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@Valid Estudante estudante, BindingResult result, RedirectAttributes redirectAttributes){
        if (result.hasErrors()) {
            return "estudantes/form";
        }

        defineSituacao(estudante, mediaCalc.media(estudante));
        estudanteRepository.save(estudante);

        redirectAttributes.addFlashAttribute("mensagem", "Estudante cadastrado com sucesso!");

        return "redirect:/estudantes/list";
    }


    @RequestMapping(value = "/{id}/notas", method = RequestMethod.POST)
    public String notas(@PathVariable(value = "id") Integer id,
                        @RequestParam Map<String,String> allParams,
                         RedirectAttributes redirectAttributes){

        Estudante estudante = estudanteRepository.getById(id);

        if (allParams.get("nota1")!=""){
            estudante.setNota1(new BigDecimal(allParams.get("nota1")));
        }
        if (allParams.get("nota2")!="") {
            estudante.setNota2(new BigDecimal(allParams.get("nota2")));
        }
        if (allParams.get("nota3")!="") {
            estudante.setNota3(new BigDecimal(allParams.get("nota3")));
        }
        if (allParams.get("notaFinal")!="") {
            estudante.setNotaFinal(new BigDecimal(allParams.get("notaFinal")));
        }
        if (allParams.get("faltas")!="") {
            estudante.setFaltas(Integer.parseInt(allParams.get("faltas")));
        }

        defineSituacao(estudante, mediaCalc.media(estudante));
        estudanteRepository.save(estudante);

        redirectAttributes.addFlashAttribute("mensagem", "Notas adicionadas!");

        return "redirect:/estudantes/list";
    }

    @RequestMapping(value = "/{id}/editar", method = RequestMethod.POST)
    public String editar(@PathVariable(value = "id") Integer id,
                         @RequestParam Map<String,String> allParams,
                         RedirectAttributes redirectAttributes){

        Estudante estudante = estudanteRepository.getById(id);

        estudante.setNome(allParams.get("nome"));

        if(allParams.get("nascimento")!="") {

            Integer dia = Integer.parseInt(allParams.get("nascimento").split("-")[2]);
            Integer mes = Integer.parseInt(allParams.get("nascimento").split("-")[1]);
            Integer ano = Integer.parseInt(allParams.get("nascimento").split("-")[0]);

            LocalDate data = LocalDate.of(ano, mes, dia);

            Date nascimento = Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant());

            estudante.setNascimento(nascimento);
        }

        estudanteRepository.save(estudante);

        redirectAttributes.addFlashAttribute("mensagem", "Dados do estudante foram editados!");

        return "redirect:/estudantes/list";
    }

    @RequestMapping("/{id}/delete")
    public ModelAndView apagaEstudante(@PathVariable(value = "id") Integer id, ModelAndView modelAndView, RedirectAttributes attr) {
        estudanteRepository.deleteById(id);
        attr.addFlashAttribute("mensagem", "Estudante removido com sucesso!");
        modelAndView.setViewName("redirect:/estudantes/list");
        return modelAndView;
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public ModelAndView listAll(ModelAndView modelAndView){
        modelAndView.addObject("estudantes", estudanteRepository.findAll());
        modelAndView.setViewName("estudantes/list");
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
            } else if (faltaNota && estudante.getFaltas() == null || !faltaNota && estudante.getFaltas() == null) {
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
