package br.edu.ifpb.pweb2.boletimDigital.repository;

import br.edu.ifpb.pweb2.boletimDigital.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer> {

    Admin findByNome(String nome);
}
