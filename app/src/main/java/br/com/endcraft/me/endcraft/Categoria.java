package br.com.endcraft.me.endcraft;

/**
 * Created by JonasXPX on 20.jul.2017.
 */
public enum Categoria {
    AÇAO("Ação", 1),
    AVENTURA("Aventura", 2),
    FICÇAO("Ficção", 3),
    ANIMAÇAO("Animação", 4),
    DOCUMENTARIO("Documentário", 5),
    FANTASIA("Fantasia", 6),
    TERROR("Terror", 7),
    SUSPENSE("Suspense", 8),
    INGLES("Inglês", 9),
    DRAMA("Drama", 10),
    GUERRA("Guerra", 11),
    COMEDIA("Comédia", 12);

    private final String nome;
    public final int id;

    Categoria(String nome, int id) {
        this.nome = nome;
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public static Categoria byName(String name){
        for(Categoria value : Categoria.values()){
            if(value.getNome().equalsIgnoreCase(name) || value.getNome().contains(name))
                return value;
        }
        return null;
    }
    public static Categoria byId(int id){
        for (Categoria value : Categoria.values()){
            if(value.id == id){
                return value;
            }
        }
        return null;
    }
}
