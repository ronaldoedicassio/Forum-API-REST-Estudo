package br.com.alura.forum.controller;

import br.com.alura.forum.controller.dto.DetalhesDoTopicoDTO;
import br.com.alura.forum.controller.dto.TopicoDTO;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
// @Controller - qdo usar esse precisa colocar ResponseBody no corpo acima do metodo.
@RequestMapping("/topicos")
// Esse controller responde as requisições que começa com /topicos
public class TopicosController {

    /*
        Autowired anotação para fazer a injeção dos dados
        O acesso ao banco de dados vai ser feito via padrão Repository
     */
    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    /*
        @ResponseBody Com RestController não e necessario essa anotação, aplicação ja vai entender
        que para ler o conteudo do metodo
     */
    @GetMapping // Verbos HTTPs
    public List<TopicoDTO> lista(String nomeCurso) {

        /*
            Função do metodo DTO e para não trabalhar com entidade
            nomeDTO => DTO - dados que sai da API para o cliente
         */

        /*
            Implementando para filtrar somente um item, por titulo, por exemplo
            Se não tiver o nome do curso, ira retornar a lista toda, caso tenha
            sera retornado somente o curso
         */

        if (nomeCurso == null) {
            // metodo findAll vai retornar a lista de todos os registros
            List<Topico> topicos = topicoRepository.findAll();

            // converter faz a conversão de topico para topicoDTO
            return TopicoDTO.converter(topicos);
        } else {
            List<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso);
            return TopicoDTO.converter(topicos);
        }
    }

    /*

     */
    @PostMapping
    public ResponseEntity<TopicoDTO> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {

        /*
            Ao cadastrar o ideal seria devolver o código 201, de quando tenho uma requisição processada com sucesso,
            mas um novo recurso foi criado no servidor

            E aí o Spring tem uma classe chamada ResponseEntity. Esse generic é o tipo de objeto que vou devolver no corpo
            da resposta, que no caso, seria o tópico. Mas lembre-se, nós não devolvemos a entidade, as classes de domínio,
            então na verdade vai ser um topicoDto.
         */

        /*
            Função do metodo DTO e para não trabalhar com entidade, porém para distuinguir o nome, iremos utilizar
            o padrão
            nomeForm => Form - dados que chegam do cliente para a API
         */

        /*
            public void cadastrar(@RequestBody TopicoForm form)

            @RequestBody

            Mas se deixarmos assim, vai ser parecido com o parâmetro do lista. No método lista não colocamos para filtrar
            pelo nome do curso? Só que dessa maneira ele vê como parâmetro da url. Mas esses parâmetros aqui, na hora de
            cadastrar, eles não vêm na barra de endereços, na url. Eles vêm no corpo da requisição. A requisição é via
            método POST, não método GET. Então eu não mando os parâmetros via url. Os parâmetros vão no corpo da requisição.
            Preciso avisar isso para o Spring. Temos que colocar uma anotação nesse parâmetro, que é o @RequestBody.
            Esse parâmetro, esse tópico form, é para pegar do corpo da requisição, e não das URLs, como parâmetro de url.
         */

        // preciso converter o objeto tipo form para topico
        Topico topico = form.converter(cursoRepository);

          /*
            Para validar os dados sera utilizado as validações pelo Bean Validation, antes de salvar os dados sera validado
            pela classe form. Dessa forma iremos manter o codigo mais limpo sem precisar colocar vários if nessa classe,
            tornando o codigo menos poluído.

            Para validar as anotações que foram colocadas no form, e necessário utilizar a anotação @Valid, que é do próprio
            Bean validation para avisa ao spring rodar as validações.

            Dessa forma o spring vai validar os dados que recebe, e vai executar a linha no método cadastrar. Se estiver alguma
            coisa errada, ele nem vai entrar no método. Ja vai devolver o código 400, que é o coódigo de bad request, que o cliente
            mandou uma requisição inválida.
         */

        topicoRepository.save(topico);


        /*
        E aí o Spring tem uma classe chamada ResponseEntity. Esse generic é o tipo de objeto que vou devolver no corpo da
        resposta, que no caso, seria o tópico. Mas lembre-se, nós não devolvemos a entidade, as classes de domínio, então
        na verdade vai ser um topicoDto.
         */
        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        // .buildAndExpand converte o ID para as {}
        return ResponseEntity.created(uri).body(new TopicoDTO(topico));
    }

    @GetMapping("/{id}")
    public DetalhesDoTopicoDTO detalhar(@PathVariable Long id) {
        /*
            @PathVariable, para dizer que é uma variável do path, da url. E aí o Spring por padrão vai associar.
            Ele sabe que é para pegar o que veio na url e jogar no parâmetro.
         */
        Topico topico = topicoRepository.getOne(id);
        return new DetalhesDoTopicoDTO(topico);
    }
}
