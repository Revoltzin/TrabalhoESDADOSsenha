package sistema;

import estruturas.FilaPrioritaria;
import estruturas.PilhaAtendidas;
import model.Posto;
import model.Senha;
import model.TipoSenha;

import java.util.Random;

// Classe principal responsavel por controlar toda a simulacao. 
// Aqui ficam as regras de chegada de clientes, atendimento, desistencias e encerramento.
public class SistemaAtendimento {
    private final FilaPrioritaria fila;
    private final PilhaAtendidas pilhaAtendidas;
    private final Posto[] postos;
    private final Senha[] senhasEmAtendimento;
    private final int[] tempoRestante;

    private final Random random;
    
    //Contadores: 
    private int totalDesistencias;  //Guarda o total de desistências 
    private int iteracao; //Guarda a rodada atual da simulação.


    // Construtor principal do sistema. 
    // Responsavel por inicializar todas as estruturas.
    public SistemaAtendimento() {

        fila = new FilaPrioritaria(); // Inicializa fila.
        pilhaAtendidas = new PilhaAtendidas();// Inicializa pilha.
        
        // Cria vetores de controle.
        postos = new Posto[Posto.QUANTIDADE_MAXIMA_POSTOS];
        senhasEmAtendimento = new Senha[Posto.QUANTIDADE_MAXIMA_POSTOS];
        tempoRestante = new int[Posto.QUANTIDADE_MAXIMA_POSTOS];

        random = new Random();

        for (int i = 0; i < postos.length; i++) {
            postos[i] = new Posto(i + 1);
        }
    }


    // Metodo principal da simulacao. 
    // Cada repeticao representa um ciclo do sistema.
    public void iniciarSimulacao(int quantidadeIteracoes) {
        System.out.println("=== SISTEMA DE GERENCIAMENTO DE SENHAS ===");

        for (iteracao = 1; iteracao <= quantidadeIteracoes; iteracao++) {
            System.out.println("\n--- Iteracao " + iteracao + " ---");

            simularChegadaDeClientes();
            simularDesistencia();
            finalizarAtendimentos();
            alterarPostosDinamicamente();
            chamarSenhasParaAtendimento();
            exibirStatus();
        }

        encerrarAtendimento();
    }

    // Simula chegada aleatoria de clientes.
    private void simularChegadaDeClientes() {
        int quantidadeChegadas = random.nextInt(3) + 1;

        for (int i = 0; i < quantidadeChegadas; i++) {
            TipoSenha tipo;

            // Define aleatoriamente o tipo da senha. 
            // 30% de chance de ser prioritaria.
            if (random.nextInt(100) < 30) {
                tipo = TipoSenha.PRIORITARIA;
            } else {
                tipo = TipoSenha.NORMAL;
            }

            Senha novaSenha = new Senha(tipo);
            fila.adicionar(novaSenha);

            System.out.println("Nova senha gerada: " + novaSenha + " (" + tipo + ")");
        }
    }

    // Simula clientes desistindo da fila.
    private void simularDesistencia() {
        if (fila.estaVazia()) {
            return;
        }

        // 20% de chance de desistência.
        if (random.nextInt(100) < 20) {
            // Remove uma senha da fila.
            Senha desistente = fila.removerDesistenteAleatorio(random);
            totalDesistencias++;

            System.out.println("Cliente desistiu da senha: " + desistente);
        }
    }

    private void finalizarAtendimentos() {
        for (int i = 0; i < postos.length; i++) {
            if (postos[i].isOcupado()) {
                tempoRestante[i]--;

                if (tempoRestante[i] <= 0) {
                    Senha senhaFinalizada = senhasEmAtendimento[i];

                    pilhaAtendidas.empilhar(senhaFinalizada);
                    postos[i].liberar();
                    senhasEmAtendimento[i] = null;

                    System.out.println("Atendimento finalizado: " + senhaFinalizada + " no Posto " + postos[i].getNumero());
                }
            }
        }
    }

    private void alterarPostosDinamicamente() {
        if (iteracao % 5 != 0) {
            return;
        }

        int indice = random.nextInt(postos.length);
        Posto posto = postos[indice];

        if (posto.isAtivo()) {
            boolean desativou = posto.desativar();

            if (desativou) {
                System.out.println("Posto " + posto.getNumero() + " foi desativado.");
            }
        } else {
            posto.ativar();
            System.out.println("Posto " + posto.getNumero() + " foi ativado.");
        }
    }

    private void chamarSenhasParaAtendimento() {
        while (!fila.estaVazia()) {
            Posto postoLivre = buscarPostoLivre();

            if (postoLivre == null) {
                break;
            }

            Senha senha = fila.chamarProxima();

            postoLivre.ocupar();

            int indicePosto = postoLivre.getNumero() - 1;
            senhasEmAtendimento[indicePosto] = senha;
            tempoRestante[indicePosto] = random.nextInt(3) + 1;

            System.out.println("Senha " + senha + " direcionada para o Posto " + postoLivre.getNumero());
        }
    }

    private Posto buscarPostoLivre() {
        for (Posto posto : postos) {
            if (posto.isLivre()) {
                return posto;
            }
        }

        return null;
    }

    private void exibirStatus() {
        System.out.println("\nStatus da simulacao:");
        System.out.println("Total de pessoas na fila: " + fila.tamanho());

        Senha[] proximas = fila.visualizarProximas(2);

        if (proximas.length == 0) {
            System.out.println("Proximas senhas: nenhuma senha aguardando.");
        } else {
            System.out.print("Proximas senhas: ");

            for (Senha senha : proximas) {
                System.out.print(senha + " ");
            }

            System.out.println();
        }

        Posto proximoPosto = buscarPostoLivre();

        if (fila.estaVazia()) {
            System.out.println("Nao ha proxima senha aguardando atendimento.");
        } else if (proximoPosto != null) {
            System.out.println("Proxima senha deve se dirigir ao Posto " + proximoPosto.getNumero());
        } else {
            System.out.println("Nenhum posto livre para a proxima senha no momento.");
        }

        System.out.println("\nPostos:");
        for (Posto posto : postos) {
            System.out.println(posto);
        }

        System.out.println("Senhas atendidas: " + pilhaAtendidas.tamanho());
        System.out.println("Desistencias: " + totalDesistencias);
    }

    private void encerrarAtendimento() {
        System.out.println("\n=== ENCERRAMENTO DO ATENDIMENTO ===");

        while (!fila.estaVazia() || existePostoOcupado()) {
            finalizarAtendimentos();
            chamarSenhasParaAtendimento();
        }

        System.out.println("Total de senhas atendidas: " + pilhaAtendidas.tamanho());
        System.out.println("Total de desistencias: " + totalDesistencias);

        pilhaAtendidas.exibirDaUltimaParaPrimeira();
    }

    private boolean existePostoOcupado() {
        for (Posto posto : postos) {
            if (posto.isOcupado()) {
                return true;
            }
        }

        return false;
    }
}
