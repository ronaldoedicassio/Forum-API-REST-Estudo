package br.com.alura.forum.config.validacao;

/*
    Nessa classe precisa passar primeiro o campo que deu erro e o segundo qual e a mensagem, qual é o erro.
 */
public class ErroDeFormularioDto {
    private String campo;
    private String erro;

    public ErroDeFormularioDto(String campo, String erro) {
        this.campo = campo;
        this.erro = erro;
    }

    public String getCampo() {
        return campo;
    }

    public String getErro() {
        return erro;
    }
}
