# Proyecto 06 — Manejo de errores sin excepciones

## Objetivo
Controlar **errores previsibles** con validaciones, códigos de retorno, `Optional` y un objeto `Resultado<T>`, sin emplear `try/catch` para controlar el flujo normal del negocio.

**Requisitos:** JDK 21 y Maven 3.9+.

## Ejecutar

```bash
mvn clean compile
mvn exec:java -Dexec.args="todos"
mvn exec:java -Dexec.args="4"
```

Cambie el argumento por `1`, `2`, `3`, `4`, `5` o `6` para ejecutar un caso específico.

## Casos

1. **División entre cero:** comprobamos `divisor == 0` antes de dividir.
2. **Cliente inexistente:** comprobamos `null` antes de acceder al objeto.
3. **Índice inválido:** comprobamos `0 <= posicion < size` antes de consultar la lista.
4. **Retiro bancario:** devolvemos códigos 0, 1 y 2 para éxito, importe inválido y fondos insuficientes.
5. **Búsqueda de clientes:** `Optional<Cliente>` expresa si existe un cliente.
6. **Lote de importes:** `Resultado<T>` expresa éxito o fallo; un elemento inválido no impide procesar los siguientes.

## Resultado destacado del caso 06

```text
06 - LOTE CON RESULT
Procesado: 100
Procesado: 200
Rechazado: No es un entero positivo: INVALIDO
Procesado: 400
Rechazado: Importe fuera de rango: 9999999999999
Lote terminado: 3 procesados, 2 rechazados
```

## Actividad guiada

1. Ejecute el caso 04. Identifique qué significan los códigos de retorno.
2. Agregue un código `3` para cuenta bloqueada y actualice el mensaje del llamador.
3. Ejecute el caso 06. Explique por qué el lote continúa después de `INVALIDO`.
4. Modifique el caso 06 para rechazar importes superiores a 1.000.000.
5. Compare el caso 06 con el caso 05 del proyecto anterior, donde el lote se interrumpía.

## Retos

- **Reto básico:** crear `dividirSeguro(int a, int b)` que devuelva `Optional<Integer>`.
- **Reto intermedio:** diseñar un `enum EstadoRetiro` para reemplazar los números mágicos 0, 1 y 2.
- **Reto avanzado:** ampliar `Resultado<T>` para incorporar un código de error, y elaborar un resumen de procesados/rechazados.

## Preguntas para discusión

- ¿Por qué las validaciones no equivalen a capturar excepciones?
- ¿Qué desventajas tienen los códigos numéricos de retorno?
- ¿Cuándo conviene `Optional` y cuándo un objeto `Resultado`?
- ¿Qué ocurre si falla la red o el disco después de una validación? ¿Puede evitarse siempre una excepción?

## Advertencia conceptual

Esta técnica sirve para **condiciones esperadas de negocio**. No garantiza que nunca ocurran excepciones: fallos de infraestructura, errores de programación o condiciones de carrera pueden seguir provocándolas. La siguiente práctica muestra cuándo conviene el manejo de excepciones con `try/catch`, `throw` y `throws`.
