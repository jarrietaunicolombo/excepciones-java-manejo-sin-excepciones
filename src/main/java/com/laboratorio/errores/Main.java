package com.laboratorio.errores;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Laboratorio didáctico: errores previsibles controlados sin try/catch. */
public class Main {
    public static void main(String[] args) {
        String caso = args.length == 0 ? "todos" : args[0];
        switch (caso) {
            case "1" -> division();
            case "2" -> clienteNulo();
            case "3" -> indice();
            case "4" -> retiro();
            case "5" -> optional();
            case "6" -> lote();
            case "todos" -> { division(); clienteNulo(); indice(); retiro(); optional(); lote(); }
            default -> System.out.println("Caso desconocido. Usa 1 a 6 o 'todos'.");
        }
    }

    // 01: validación previa. No se realiza la operación si el divisor es cero.
    static void division() {
        System.out.println("\n01 - DIVISIÓN SEGURA");
        int numerador = 100, divisor = 0;
        if (divisor == 0) {
            System.out.println("No se puede dividir entre cero; operación omitida.");
            return;
        }
        System.out.println("Resultado: " + numerador / divisor);
    }

    // 02: comprobación explícita de ausencia.
    record Cliente(String nombre) {}
    static void clienteNulo() {
        System.out.println("\n02 - CLIENTE INEXISTENTE");
        Cliente cliente = null;
        if (cliente == null) {
            System.out.println("Cliente no encontrado; no se accede a sus datos.");
            return;
        }
        System.out.println(cliente.nombre());
    }

    // 03: comprobar límites antes de acceder a una colección.
    static void indice() {
        System.out.println("\n03 - ÍNDICE FUERA DE RANGO");
        List<String> movimientos = List.of("Abono", "Retiro");
        int posicion = 5;
        if (posicion < 0 || posicion >= movimientos.size()) {
            System.out.println("Posición inválida: " + posicion);
            return;
        }
        System.out.println(movimientos.get(posicion));
    }

    // 04: código de retorno; 0=éxito, 1=importe inválido, 2=fondos insuficientes.
    static final class Cuenta {
        private int saldo;
        Cuenta(int saldoInicial) { saldo = saldoInicial; }
        int retirar(int importe) {
            if (importe <= 0) return 1;
            if (importe > saldo) return 2;
            saldo -= importe;
            return 0;
        }
        int saldo() { return saldo; }
    }
    static void retiro() {
        System.out.println("\n04 - CÓDIGOS DE RETORNO");
        Cuenta cuenta = new Cuenta(100);
        for (int importe : new int[] {-10, 150, 40}) {
            int codigo = cuenta.retirar(importe);
            String mensaje = switch (codigo) {
                case 0 -> "Retiro exitoso";
                case 1 -> "Importe inválido";
                case 2 -> "Fondos insuficientes";
                default -> "Código desconocido";
            };
            System.out.printf("Importe %d: %s (código %d), saldo %d%n", importe, mensaje, codigo, cuenta.saldo());
        }
    }

    // 05: Optional representa ausencia sin usar null como contrato de retorno.
    static Optional<Cliente> buscarCliente(int id) {
        Map<Integer, Cliente> clientes = Map.of(1, new Cliente("Ana"), 2, new Cliente("Luis"));
        return Optional.ofNullable(clientes.get(id));
    }
    static void optional() {
        System.out.println("\n05 - OPTIONAL");
        for (int id : new int[] {1, 99}) {
            System.out.println("ID " + id + ": " + buscarCliente(id)
                .map(Cliente::nombre).orElse("Cliente no encontrado"));
        }
    }

    // 06: Result explícito para separar éxito y error de negocio.
    record Resultado<T>(boolean exitoso, T valor, String error) {
        static <T> Resultado<T> ok(T valor) { return new Resultado<>(true, valor, null); }
        static <T> Resultado<T> fallo(String error) { return new Resultado<>(false, null, error); }
    }
    static Resultado<Integer> convertirImporte(String texto) {
        // Solo enteros positivos representables: evita NumberFormatException por entradas inválidas.
        if (texto == null || texto.isEmpty()) return Resultado.fallo("Importe vacío");
        long acumulado = 0;
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c < '0' || c > '9') return Resultado.fallo("No es un entero positivo: " + texto);
            acumulado = acumulado * 10 + (c - '0');
            if (acumulado > Integer.MAX_VALUE) return Resultado.fallo("Importe fuera de rango: " + texto);
        }
        if (acumulado == 0) return Resultado.fallo("El importe debe ser mayor que cero");
        return Resultado.ok((int) acumulado);
    }
    static void lote() {
        System.out.println("\n06 - LOTE CON RESULT");
        List<String> importes = List.of("100", "200", "INVALIDO", "400", "9999999999999");
        List<Integer> procesados = new ArrayList<>();
        List<String> rechazados = new ArrayList<>();
        for (String texto : importes) {
            Resultado<Integer> resultado = convertirImporte(texto);
            if (resultado.exitoso()) {
                procesados.add(resultado.valor());
                System.out.println("Procesado: " + resultado.valor());
            } else {
                rechazados.add(resultado.error());
                System.out.println("Rechazado: " + resultado.error());
            }
        }
        System.out.printf("Lote terminado: %d procesados, %d rechazados%n", procesados.size(), rechazados.size());
    }
}
