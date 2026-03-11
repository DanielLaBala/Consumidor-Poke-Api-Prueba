package org.example;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

public class Main {
    private static HttpURLConnection conexion(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        return conn;
    }

    static String tipoEspañol(String tipoIngles) {
        String resultado = "";

        switch (tipoIngles) {
            case "normal" -> resultado = "Normal";
            case "fire" -> resultado = "Fuego";
            case "water" -> resultado = "Agua";
            case "electric" -> resultado = "Eléctrico";
            case "grass" -> resultado = "Planta";
            case "ice" -> resultado = "Hielo";
            case "fighting" -> resultado = "Lucha";
            case "poison" -> resultado = "Veneno";
            case "ground" -> resultado = "Tierra";
            case "flying" -> resultado = "Volador";
            case "psychic" -> resultado = "Psíquico";
            case "bug" -> resultado = "Bicho";
            case "rock" -> resultado = "Roca";
            case "ghost" -> resultado = "Fantasma";
            case "dragon" -> resultado = "Dragón";
            case "dark" -> resultado = "Siniestro";
            case "steel" -> resultado = "Acero";
            case "fairy" -> resultado = "Hada";
        }

        return resultado;
    }

    static StringBuilder leerDatosConexion(HttpURLConnection conn) {
        StringBuilder respuesta = new StringBuilder();

        try(BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String linea;

            while ((linea = br.readLine()) != null) { // br.ready no suele llegar al final real en archivos grandes
                respuesta.append(linea);
            }
        } catch (IOException ignored) {}

        return respuesta;
    }

    static void imprimirDatosDefault(HttpURLConnection conn) {
        System.out.println("DATOS");

        Gson gson = new Gson();

        StringBuilder respuesta = leerDatosConexion(conn);
        Pokemon p = gson.fromJson(respuesta.toString(), Pokemon.class);

        System.out.println("\t Nº de Pokedex: " + p.name);

        if (p.types.size() > 1) {
            System.out.println("\t Tipos: " + tipoEspañol(p.types.get(0).type.name) + ", " + tipoEspañol(p.types.get(1).type.name));
        } else {
            System.out.println("\t Tipo: " + tipoEspañol(p.types.get(0).type.name));
        }

        System.out.println("\t Peso: " + p.weight / 10f + " kg");
    }

    static void menu() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("¿De que pokemon quieres información? (0 para salir)");

            String pokemonNombre = sc.nextLine().toLowerCase();

            if (pokemonNombre.equals("0")) break;

            try {
                URI uri = new URI("https", "pokeapi.co", "/api/v2/pokemon/" + pokemonNombre, null);
                URL url = uri.toURL();

                HttpURLConnection conn = conexion(url);

                int responseCode = conn.getResponseCode();

                if (responseCode == 200) { // 200 = OK, todo bien
                    imprimirDatosDefault(conn);
                } else {
                    System.err.println("Ese pokemon no existe.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        menu();
    }
}