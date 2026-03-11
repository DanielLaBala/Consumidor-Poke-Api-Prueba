package org.example;

import java.util.List;

class Pokemon {
    int id;
    int weight;
    List<TypeWrapper> types;
}

class TypeWrapper { // Esto es porque tiene que reflejar la estructura real de lo que devuelve la api, como hay un array dentro de un array pues..
    Type type;
}

class Type {
    String name;
}