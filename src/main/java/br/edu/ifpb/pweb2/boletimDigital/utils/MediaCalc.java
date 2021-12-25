package br.edu.ifpb.pweb2.boletimDigital.utils;

import br.edu.ifpb.pweb2.boletimDigital.model.Estudante;
import br.edu.ifpb.pweb2.boletimDigital.repository.EstudanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class MediaCalc {

    @Autowired
    private EstudanteRepository estudanteRepository;

    public BigDecimal media(Estudante estudante){
        BigDecimal media = new BigDecimal(0);
        BigDecimal divisor = new BigDecimal(3);


        if(estudante.getNota1() == null || estudante.getNota2() == null || estudante.getNota3() == null){
            if (estudante.getNota1() == null && estudante.getNota2() != null && estudante.getNota3() != null){
                media = estudante.getNota2().add(estudante.getNota3()).divide(divisor,0 , RoundingMode.HALF_UP);
            }else if(estudante.getNota2() == null && estudante.getNota1() != null && estudante.getNota3() != null){
                media = estudante.getNota1().add(estudante.getNota3()).divide(divisor,0 , RoundingMode.HALF_UP);
            }else if(estudante.getNota3() == null && estudante.getNota2() != null && estudante.getNota1() != null){
                media = estudante.getNota1().add(estudante.getNota2()).divide(divisor,0 , RoundingMode.HALF_UP);
            }else if(estudante.getNota2()  == null && estudante.getNota3() == null && estudante.getNota1() != null){
                media = estudante.getNota1().divide(divisor,0 , RoundingMode.HALF_UP);
            }else if(estudante.getNota3() == null && estudante.getNota2()  != null){
                media = estudante.getNota2() .divide(divisor,0 , RoundingMode.HALF_UP);
            }else if(estudante.getNota3() != null){
                media = estudante.getNota3().divide(divisor,0 , RoundingMode.HALF_UP);
            }else if (estudante.getNota1() != null && estudante.getNota2()  != null && estudante.getNota3() != null) {
                media = new BigDecimal(101);
            }
        }else {
            media = estudante.getNota1().add(estudante.getNota2() .add(estudante.getNota3())).divide(divisor,0 , RoundingMode.HALF_UP);
        }

        return media;

    }
}
