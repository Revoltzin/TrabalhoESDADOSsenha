package model;

// Enum usado para limitar os tipos possiveis de senha.
// Isso evita valores invalidos, como "VIP", "Comum", "Urgente" etc.
public enum TipoSenha {
    // Senha normal, identificada pelo prefixo N.
    NORMAL("N"),

    // Senha prioritaria, identificada pelo prefixo P.
    PRIORITARIA("P");

    private final String prefixo;

    TipoSenha(String prefixo) {
        this.prefixo = prefixo;
    }

    public String getPrefixo() {
        return prefixo;
    }
}
