package br.edu.ifpb.pweb2.boletimDigital.repository;

import br.edu.ifpb.pweb2.boletimDigital.model.Estudante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstudanteRepository extends JpaRepository<Estudante, Integer> {
}
