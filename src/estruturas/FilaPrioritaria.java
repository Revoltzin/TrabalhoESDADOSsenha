package estruturas;

import model.Senha;

import java.util.Random;

// Fila responsavel por controlar a ordem de chamada das senhas.
//
// A regra principal do enunciado e:
// a cada 2 senhas normais chamadas, 1 senha prioritaria deve ser chamada.
//
// Para deixar essa regra mais simples de controlar, a classe usa duas filas:
// uma fila para senhas normais e uma fila para senhas prioritarias.
public class FilaPrioritaria {
    // No interno da fila encadeada.
    // Cada no guarda uma senha e uma referencia para o proximo no.
    private static class No {
        private final Senha senha;
        private No proximo;

        private No(Senha senha) {
            this.senha = senha;
        }
    }

    // Inicio e fim da fila de senhas normais.
    private No inicioNormais;
    private No fimNormais;

    // Inicio e fim da fila de senhas prioritarias.
    private No inicioPrioritarias;
    private No fimPrioritarias;

    // Quantidades separadas ajudam a saber rapidamente se cada fila esta vazia.
    private int quantidadeNormais;
    private int quantidadePrioritarias;

    // Conta quantas senhas normais foram chamadas desde a ultima prioritaria.
    // Quando chegar em 2, a proxima chamada deve tentar chamar uma prioritaria.
    private int normaisChamadasSeguidas;

    // Adiciona uma senha na fila correta, de acordo com o tipo dela.
    public void adicionar(Senha senha) {
        if (senha == null) {
            throw new IllegalArgumentException("A senha nao pode ser nula.");
        }

        if (senha.isPrioritaria()) {
            adicionarPrioritaria(senha);
        } else {
            adicionarNormal(senha);
        }
    }

    // Chama a proxima senha respeitando a regra 2N:1P.
    //
    // Casos principais:
    // - Se ja foram chamadas 2 normais e existe prioritaria, chama prioritaria.
    // - Se ainda nao foram chamadas 2 normais e existe normal, chama normal.
    //
    // Casos de excecao:
    // - Se chegou a vez da prioritaria, mas nao ha prioritarias, chama normal.
    // - Se chegou a vez da normal, mas nao ha normais, chama prioritaria.
    public Senha chamarProxima() {
        if (estaVazia()) {
            return null;
        }

        if (deveChamarPrioritaria()) {
            if (quantidadePrioritarias > 0) {
                return chamarPrioritaria();
            }

            return chamarNormal();
        }

        if (quantidadeNormais > 0) {
            return chamarNormal();
        }

        return chamarPrioritaria();
    }

    // Remove uma senha da fila por desistência.
    //
    // Este metodo NAO usa chamarProxima(), porque uma desistência não é um
    // atendimento. Se usasse chamarProxima(), a fila iria alterar o contador
    // da regra 2N:1P como se a senha tivesse sido realmente chamada.
    //
    // Quando existem senhas dos dois tipos, a escolha do tipo removido é
    // aleatória, simulando qualquer cliente da fila desistindo.
    public Senha removerDesistenteAleatorio(Random random) {
        if (estaVazia()) {
            return null;
        }

        if (random == null) {
            throw new IllegalArgumentException("O gerador aleatorio nao pode ser nulo.");
        }

        if (quantidadeNormais > 0 && quantidadePrioritarias > 0) {
            if (random.nextBoolean()) {
                return removerInicioNormal();
            }

            return removerInicioPrioritaria();
        }

        if (quantidadeNormais > 0) {
            return removerInicioNormal();
        }

        return removerInicioPrioritaria();
    }

    // Mostra a proxima senha sem remover da fila.
    public Senha visualizarProxima() {
        Senha[] proximas = visualizarProximas(1);

        if (proximas.length == 0) {
            return null;
        }

        return proximas[0];
    }

    // Mostra as proximas senhas sem remover nenhuma delas.
    // Esse metodo sera util para o requisito de exibir as proximas duas senhas.
    public Senha[] visualizarProximas(int quantidade) {
        if (quantidade <= 0 || estaVazia()) {
            return new Senha[0];
        }

        int limite = Math.min(quantidade, tamanho());
        Senha[] proximas = new Senha[limite];

        // Ponteiros temporarios usados apenas para simular a ordem de chamada.
        // Eles nao alteram as filas reais.
        No normalAtual = inicioNormais;
        No prioritariaAtual = inicioPrioritarias;
        int normaisRestantes = quantidadeNormais;
        int prioritariasRestantes = quantidadePrioritarias;
        int normaisSimuladas = normaisChamadasSeguidas;

        for (int i = 0; i < limite; i++) {
            boolean vezPrioritaria = normaisSimuladas >= 2;

            if (vezPrioritaria && prioritariasRestantes > 0) {
                proximas[i] = prioritariaAtual.senha;
                prioritariaAtual = prioritariaAtual.proximo;
                prioritariasRestantes--;
                normaisSimuladas = 0;
            } else if (normaisRestantes > 0) {
                proximas[i] = normalAtual.senha;
                normalAtual = normalAtual.proximo;
                normaisRestantes--;
                normaisSimuladas++;
            } else {
                proximas[i] = prioritariaAtual.senha;
                prioritariaAtual = prioritariaAtual.proximo;
                prioritariasRestantes--;
                normaisSimuladas = 0;
            }
        }

        return proximas;
    }

    public boolean estaVazia() {
        return quantidadeNormais == 0 && quantidadePrioritarias == 0;
    }

    public int tamanho() {
        return quantidadeNormais + quantidadePrioritarias;
    }

    public int getQuantidadeNormais() {
        return quantidadeNormais;
    }

    public int getQuantidadePrioritarias() {
        return quantidadePrioritarias;
    }

    public int getNormaisChamadasSeguidas() {
        return normaisChamadasSeguidas;
    }

    private boolean deveChamarPrioritaria() {
        return normaisChamadasSeguidas >= 2;
    }

    private void adicionarNormal(Senha senha) {
        No novoNo = new No(senha);

        if (quantidadeNormais == 0) {
            inicioNormais = novoNo;
            fimNormais = novoNo;
        } else {
            fimNormais.proximo = novoNo;
            fimNormais = novoNo;
        }

        quantidadeNormais++;
    }

    private void adicionarPrioritaria(Senha senha) {
        No novoNo = new No(senha);

        if (quantidadePrioritarias == 0) {
            inicioPrioritarias = novoNo;
            fimPrioritarias = novoNo;
        } else {
            fimPrioritarias.proximo = novoNo;
            fimPrioritarias = novoNo;
        }

        quantidadePrioritarias++;
    }

    private Senha chamarNormal() {
        Senha senha = removerInicioNormal();
        normaisChamadasSeguidas++;
        return senha;
    }

    private Senha chamarPrioritaria() {
        Senha senha = removerInicioPrioritaria();
        normaisChamadasSeguidas = 0;
        return senha;
    }

    private Senha removerInicioNormal() {
        Senha senha = inicioNormais.senha;
        inicioNormais = inicioNormais.proximo;
        quantidadeNormais--;

        if (quantidadeNormais == 0) {
            fimNormais = null;
        }

        return senha;
    }

    private Senha removerInicioPrioritaria() {
        Senha senha = inicioPrioritarias.senha;
        inicioPrioritarias = inicioPrioritarias.proximo;
        quantidadePrioritarias--;

        if (quantidadePrioritarias == 0) {
            fimPrioritarias = null;
        }

        return senha;
    }
}
