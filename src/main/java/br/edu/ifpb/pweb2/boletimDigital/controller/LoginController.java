package br.edu.ifpb.pweb2.boletimDigital.controller;

import br.edu.ifpb.pweb2.boletimDigital.model.Admin;
import br.edu.ifpb.pweb2.boletimDigital.repository.AdminRepository;
import br.edu.ifpb.pweb2.boletimDigital.utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private AdminRepository adminRepository;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView getForm(ModelAndView modelAndView, Admin admin) {
        modelAndView.setViewName("login/login");
        modelAndView.addObject("admin", admin);

        if (adminRepository.findByNome("admin")==null){
            Admin createAdmin = new Admin();

            createAdmin.setNome("admin");
            createAdmin.setSenha(PasswordUtil.criptografaSenha("admin1234567890"));

            adminRepository.save(createAdmin);
        }
        return modelAndView;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(Admin admin, HttpSession session, ModelAndView modelAndView, RedirectAttributes redirectAttributes){

        if((admin = this.adminIsValido(admin)) != null){
            session.setAttribute("admin", admin);
            modelAndView.setViewName("redirect:/home");
        } else{
            redirectAttributes.addFlashAttribute("mensagem", "Login e senha inválidos!");
            modelAndView.setViewName("redirect:/login");
        }
        return modelAndView;
    }

    @RequestMapping("/login/out")
    public ModelAndView logout(ModelAndView modelAndView, HttpSession session) {
        session.invalidate();
        modelAndView.setViewName("redirect:/login");
        return modelAndView;
    }

    public Admin adminIsValido(Admin admin) {
        Admin adminBD = adminRepository.findByNome(admin.getNome());
        System.out.println(adminBD);
        boolean valido = false;
        if(adminBD != null){
            if (PasswordUtil.verificaSenha(admin.getSenha(), adminBD.getSenha())){
                valido = true;
            }
        }
        return valido ? admin: null;
    }
}
