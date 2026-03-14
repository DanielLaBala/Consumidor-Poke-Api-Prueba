package org.example;

import java.util.List;

class Pokemon {
    int id;
    int weight;
    List<TypeWrapper> types;
    List<StatWrapper> stats;

    PokemonSprites sprites;
}

class PokemonSprites {
    String front_default;
}

class TypeWrapper { // Esto es porque tiene que reflejar la estructura real de lo que devuelve la api, como hay un array dentro de un array pues..
    Type type;
}

class Type {
    String name;
}

class StatWrapper { // Esto es porque tiene que reflejar la estructura real de lo que devuelve la api, como hay un array dentro de un array pues..
    int base_stat;
    Stat stat;
}

class Stat {
    String name;
}