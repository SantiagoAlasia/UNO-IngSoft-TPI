package domain.card;

// -----------------------------
// Patrón Strategy en cartas UNO
// -----------------------------
// Este enum define los tipos de cartas posibles en UNO.
// Es fundamental para el patrón Strategy, ya que permite identificar
// el tipo de carta y seleccionar la estrategia (comportamiento) adecuada
// en tiempo de ejecución.

public enum CardType {
    NUMBER,
    SKIP,
    REVERSE,
    DRAW_TWO,
    WILD_COLOR,
    WILD_DRAW_FOUR
}
