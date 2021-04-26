package br.com.alura.forum.controller.form;

import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TopicoForm {

    /*
    Para fazer esse tipo de validação de formulários, de campo obrigatório, tamanho mínimo, tamanho máximo, letra, número e etc, existe uma especificação do Java, que é o tal do Bean Validation. E o Spring se integra com essa especificação.
    Podemos utilizá-lo, a validação é toda feita por anotações, fica muito mais fácil.

    No nosso método cadastrar, o objeto que estamos recebendo é o tal do topicoform. O cliente mandou o JSON e o Spring chama o Jackson para pegar e converter no topicoform. Na classe topicoform estão os atributos. Estão os campos que quero
    validar. Então aqui mesmo, no próprio objeto form, em cima dos atributos, posso colocar as anotações do Bean Validation, um: @NotNull e @NotEmpty, para dizer que não pode ser um campo nulo e não pode ser vazio. Posso também colocar um
    número mínimo de caracteres: @Lengh(min = 5).

    Essas anotações fazem parte do Bean Validation. E aí posso utilizar isso para fazer a validação. Posso colocar que a mensagem tem que ter no mínimo 5 caracteres, o título no mínimo 5 caracteres. Tudo como não nulo e não vazio.
    A ideia é essa. Vou anotando os atributos com as anotações do Bean Validation. Tem anotações para string, para campo decimal, campo inteiro, data.

    O Bean Validation também é flexível. Você pode criar uma nova anotação, caso não tenha. Por exemplo, você quer validar um campo CPF. Você pode criar uma anotação e ensinar como é essa validação. É uma especificação bem bacana e simples
    de trabalhar.

    Só que não basta apenas anotar os atributos da classe. Além de anotar os atributos, preciso dizer para o Spring que quero que ele chame o Bean Validation e gere um erro caso algum parâmetro esteja invalido de acordo com as anotações que
    foram colocadas.

    Para fazer isso, no topicoform, além de estar anotado com @RequestBody, para dizer para o Spring que isso está vindo no corpo da requisição, temos que colocar mais uma anotação, que é o @Valid, que é do próprio Bean Validation, para
    avisar para o Spring rodar as validações.

    O próprio Spring vai rodar as validações para nós. Se estiver tudo ok, ele vai executar linha por linha do método cadastrar. Se tiver alguma coisa invalida, ele nem vai entrar no método. Já vai devolver o código 400, que é o código de
    bad request, que o cliente mandou uma requisição inválida.
     */
    @NotNull
    @NotEmpty
    @Length(min = 5)
    private String titulo;
    @NotNull
    @NotEmpty
    private String mensagem;
    @NotNull
    @NotEmpty
    private String nomeCurso;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getNomeCurso() {
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    public Topico converter(CursoRepository cursoRepository) {
        Curso curso = cursoRepository.findByNome(nomeCurso);
        return new Topico(titulo, mensagem, curso);
    }
}
