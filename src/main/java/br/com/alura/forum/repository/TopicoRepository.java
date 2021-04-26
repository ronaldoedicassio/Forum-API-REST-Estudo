package br.com.alura.forum.repository;

import br.com.alura.forum.modelo.Topico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

    /*
        JpaRepository - e uma interface que temos que passar a entidade(classe) e qual e o tipo do atributo
        do id - como estamos herdando dessa classe, pelo conceito, todos os metodos passam a fazer parte
        da nossa interface
    */

    /*
        Para criar um metodo, caso ele nao exista:
            => findByNomedoAtributoqueQueremosFiltrardaEntidade

        Se for um atributo de um relacionamento
            => findBy -Nomedo Atributo do Relacionamento da Entidade Concatenado NomedoAtributo, que ele vai entender
               que deve ser filtrado pelo relacionamento

        Criação desse metodo, seguindo essa regra para os metodos que não existem, não vai dar erro.

     */
    List<Topico> findByCursoNome(String nomeCurso);

    /*
        Outro detalhe importante, quando dentro da Entidade Topico existir um atributo com o mesmo nome do relacionamento
        para diferenciar, colocar da seguinte forma:

            List<Topico> findByCurso_Nome(String nomeCurso); -> dessa forma o spring vai entender que para filtra o atributo pelo
            relacionamento.
            caso esteja sem o _ ele vai entender que o atributo da entidade.
     */
}
