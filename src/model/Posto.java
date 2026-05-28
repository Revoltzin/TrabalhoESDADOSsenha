package model;

// Representa um posto de atendimento individual.
// Exemplo: Posto 1, Posto 2, Posto 3, Posto 4 ou Posto 5.
public class Posto {
    // O enunciado limita o sistema a, no maximo, 5 postos.
    public static final int QUANTIDADE_MAXIMA_POSTOS = 5;

    // O enunciado exige que pelo menos 3 postos estejam sempre ativos.
    // Por isso, os postos 1, 2 e 3 sao considerados obrigatorios.
    public static final int QUANTIDADE_MINIMA_ATIVOS = 3;

    // Numero que identifica o posto dentro do sistema.
    // Como o numero nao deve mudar depois da criacao, ele e final.
    private final int numero;

    // Indica se o posto esta disponivel para uso no sistema.
    // Um posto inativo nao pode receber atendimento.
    private boolean ativo;

    // Indica se o posto esta atendendo alguem no momento.
    // true  = ocupado
    // false = livre
    private boolean ocupado;

    // Cria um posto a partir do seu numero.
    // Os postos 1, 2 e 3 ja nascem ativos, pois sao obrigatorios.
    // Os postos 4 e 5 nascem inativos, pois podem ser ativados dinamicamente.
    public Posto(int numero) {
        validarNumero(numero);

        this.numero = numero;
        this.ativo = numero <= QUANTIDADE_MINIMA_ATIVOS;
        this.ocupado = false;
    }

    // Garante que nenhum posto seja criado fora do limite permitido pelo enunciado.
    private void validarNumero(int numero) {
        if (numero < 1 || numero > QUANTIDADE_MAXIMA_POSTOS) {
            throw new IllegalArgumentException("O posto deve estar entre 1 e 5.");
        }
    }

    public int getNumero() {
        return numero;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    // Um posto so esta livre se estiver ativo e nao estiver ocupado.
    public boolean isLivre() {
        return ativo && !ocupado;
    }

    // Os tres primeiros postos nao podem ser desativados,
    // porque eles garantem o minimo de 3 postos ativos.
    public boolean isObrigatorio() {
        return numero <= QUANTIDADE_MINIMA_ATIVOS;
    }

    // Ativa um posto para que ele possa receber atendimentos.
    // Esse metodo sera util principalmente para os postos 4 e 5.
    public void ativar() {
        ativo = true;
    }

    // Tenta desativar o posto.
    // Retorna true se conseguiu desativar e false se nao conseguiu.
    public boolean desativar() {
        // Postos obrigatorios precisam ficar sempre ativos.
        if (isObrigatorio()) {
            return false;
        }

        // Um posto ocupado nao deve ser desativado no meio de um atendimento.
        if (ocupado) {
            return false;
        }

        ativo = false;
        return true;
    }

    // Tenta ocupar o posto com um atendimento.
    // Retorna true se o posto estava ativo e livre.
    // Retorna false se o posto estava inativo ou ja ocupado.
    public boolean ocupar() {
        if (!isLivre()) {
            return false;
        }

        ocupado = true;
        return true;
    }

    // Libera o posto depois que o atendimento termina.
    public void liberar() {
        ocupado = false;
    }

    // Texto usado para mostrar o estado atual do posto na saida do programa.
    public String getStatus() {
        if (!ativo) {
            return "Inativo";
        }

        if (ocupado) {
            return "Ocupado";
        }

        return "Livre";
    }

    @Override
    public String toString() {
        return "Posto " + numero + " - " + getStatus();
    }
}
