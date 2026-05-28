// # Entidade senha
package model;

public class Senha {
    private static int contadorNormal = 1;
    private static int contadorPrioritaria = 1;

    private final String codigo;
    private final TipoSenha tipo;

    // Contadores separados para normal e prioritaria.
    // Assim, cada tipo de senha tem sua propria sequencia:
    // N001, N002, N003...
    // P001, P002, P003...
    public Senha(TipoSenha tipo) {
        this.tipo = tipo;

        if (tipo == TipoSenha.PRIORITARIA) {
            // O 0 diz "preenche com zero", o 3 diz "minimo 3 casas",
            // e o d diz que o valor formatado e um numero inteiro.
            this.codigo = String.format("P%03d", contadorPrioritaria++);
        } else {
            this.codigo = String.format("N%03d", contadorNormal++);
        }
    }

    public String getCodigo() {
        return codigo;
    }

    public TipoSenha getTipo() {
        return tipo;
    }

    // Verifica se a senha e prioritaria.
    // Retorna true para senha P e false para senha N.
    public boolean isPrioritaria() {
        return tipo == TipoSenha.PRIORITARIA;
    }

    @Override
    public String toString() {
        return codigo;
    }

    // resetarContadores() existe para facilitar testes.
    public static void resetarContadores() {
        contadorNormal = 1;
        contadorPrioritaria = 1;
    }
}
