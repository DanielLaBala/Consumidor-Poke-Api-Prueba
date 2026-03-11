package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

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
            while (br.ready()) {
                String linea = br.readLine();

                respuesta.append(linea);
            }
        } catch (IOException ignored) {}

        return respuesta;
    }

    static void imprimirDatosDefault(HttpURLConnection conn) {
        System.out.println("DATOS");

        StringBuilder respuesta = leerDatosConexion(conn);

        JSONObject json = new JSONObject(respuesta.toString());

        System.out.println("\t Nº de Pokedex: " + json.getInt("id"));

        JSONArray tipos = json.getJSONArray("types");
        JSONObject tipo1 = tipos.getJSONObject(0).getJSONObject("type");
        JSONObject tipo2;

        if (tipos.length() > 1) {
            tipo2 = tipos.getJSONObject(1).getJSONObject("type");

            System.out.println("\t Tipos: " + tipoEspañol(tipo1.getString("name")) + ", " + tipoEspañol(tipo2.getString("name")));
        } else {
            System.out.println("\t Tipo: " + tipoEspañol(tipo1.getString("name")));
        }

        int peso = json.getInt("weight");
        System.out.println("\t Peso: " + peso / 10f + " kg");
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