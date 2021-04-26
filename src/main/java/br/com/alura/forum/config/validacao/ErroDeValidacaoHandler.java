package br.com.alura.forum.config.validacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

/*
    oda vez que acontecer uma exception, em qualquer método, o Spring automaticamente vai chamar esse interceptador,
    onde fazemos o tratamento apropriado. Esse interceptador é chamado de controller advice

    anotação @RestControllerAdvice, porque estamos usando REST controllers. Agora, precisamos ensinar para o Spring que
    esse controller advice vai fazer tratamentos de erros, para quando tiver uma exceção.
 */
@RestControllerAdvice
public class ErroDeValidacaoHandler {

    @Autowired
    private MessageSource messageSource; // serve para identificar a linguagem da mensagem que deve ser lida;

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    /*
        Toda vez que acontecer alguma exception desse tipo em qualquer REST Controller do projeto, o Spring vai chamar
        esse método handle passando como parâmetro a exception que aconteceu. Ele considera que deu um erro, mas que não
        vai devolver o 400 para o cliente. Ele vai chamar o interceptador, e aqui dentro você está fazendo o tratamento.
        Ele considera que fizemos o tratamento e por padrão vai devolver 200 para o cliente. Mas não quero isso, quero que e
        le devolva 400.

        Por mais que eu tenha tratado o erro, não é para devolver 200. É para continuar devolvendo 400, porque meu tratamento
        é só para mudar as mensagens. Agora, aqui dentro faço o tratamento.

        Só que o Spring, além de devolver o @ResponseStatus, devolve o que o método estiver retornando. Nosso método não pode
        ser void. Ele tem que devolver alguma coisa, que no nosso caso vai ser uma lista com as mensagens de erro.
     */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    /*
     Uma exception chamada MethodArgumentNotValidException. Temos que passar o nome da classe. Agora o Spring sabe que se
     acontecer essa execption em qualquer REST controller, ele vai cair nesse método.
     */
    public List<ErroDeFormularioDto> handle(MethodArgumentNotValidException exception) {
        /*
            Esse método vai devolver um list, para representar o erro um classe DTO foi criada.

            O MethodArgumentNotValidException vai nos dar qual e o erro que acontece. Dentro desse objeto tem todos os erros
            que aconteceu.

            Essa variável tem erros de formulários, so que preciso devolver essa em formato de lista de field. Para isso
            foi utilizar o construtor no DTO


         */
        List<ErroDeFormularioDto> dto = new ArrayList<>();

        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        fieldErrors.forEach(e -> {
            String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
            ErroDeFormularioDto erro = new ErroDeFormularioDto(e.getField(), mensagem);
            dto.add(erro);
        });

        return dto;
    }

}
