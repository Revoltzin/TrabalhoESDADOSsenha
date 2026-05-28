package estruturas;

import model.Senha;

// Pilha responsavel por armazenar todas as senhas que ja foram atendidas.
//
// A estrutura de pilha trabalha com a regra LIFO:
// Last In, First Out.
//
// o ultimo elemento que entra e o primeiro que sai.
//
// Isso combina com o requisito do enunciado, que pede para mostrar as senhas
// atendidas da ultima para a primeira no encerramento do atendimento.
public class PilhaAtendidas {
    // No interno da pilha encadeada.
    // Cada no guarda uma senha atendida e aponta para a senha atendida antes dela.
    private static class No {
        private final Senha senha;
        private final No anterior;

        private No(Senha senha, No anterior) {
            this.senha = senha;
            this.anterior = anterior;
        }
    }

    // Topo da pilha.
    // O topo sempre representa a ultima senha atendida.
    private No topo;

    // Quantidade total de senhas atendidas armazenadas na pilha.
    private int quantidade;

    // Empilha uma senha atendida.
    //
    // Sempre que uma pessoa terminar o atendimento, o sistema deve chamar
    // este metodo para guardar a senha no historico de atendimentos.
    public void empilhar(Senha senha) {
        if (senha == null) {
            throw new IllegalArgumentException("A senha atendida nao pode ser nula.");
        }

        // O novo no passa a apontar para o topo atual.
        // Depois disso, o novo no vira o novo topo da pilha.
        topo = new No(senha, topo);
        quantidade++;
    }

    // Remove e retorna a senha que esta no topo da pilha.
    //
    // Como a pilha mostra primeiro o ultimo atendimento, este metodo retorna
    // a ultima senha atendida ainda armazenada.
    public Senha desempilhar() {
        if (estaVazia()) {
            return null;
        }

        Senha senha = topo.senha;
        topo = topo.anterior;
        quantidade--;

        return senha;
    }

    // Retorna a senha do topo sem remover da pilha.
    //
    // Esse metodo e util quando queremos consultar qual foi a ultima senha
    // atendida, mas sem alterar o historico.
    public Senha consultarTopo() {
        if (estaVazia()) {
            return null;
        }

        return topo.senha;
    }

    public boolean estaVazia() {
        return topo == null;
    }

    public int tamanho() {
        return quantidade;
    }

    // Retorna todas as senhas atendidas em um vetor,
    // comecando pela ultima atendida e terminando na primeira.
    //
    // Importante: este metodo nao remove nada da pilha.
    // Ele apenas percorre os nos para montar uma lista de visualizacao.
    public Senha[] listarDaUltimaParaPrimeira() {
        Senha[] senhas = new Senha[quantidade];
        No atual = topo;
        int indice = 0;

        while (atual != null) {
            senhas[indice] = atual.senha;
            atual = atual.anterior;
            indice++;
        }

        return senhas;
    }

    // Imprime no console todas as senhas atendidas,
    // da ultima para a primeira, como o enunciado solicita no encerramento.
    public void exibirDaUltimaParaPrimeira() {
        if (estaVazia()) {
            System.out.println("Nenhuma senha foi atendida.");
            return;
        }

        System.out.println("Senhas atendidas da ultima para a primeira:");

        No atual = topo;
        while (atual != null) {
            System.out.println(atual.senha);
            atual = atual.anterior;
        }
    }
}
