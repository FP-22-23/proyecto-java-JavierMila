# Proyecto del Segundo Cuatrimestre Fundamentos de Programación (Curso 2022/23)
Autor/a: Javier Ignacio Milá de la Roca Dos Santos
uvus: PMN9817

## Estructura de las carpetas del proyecto

* `/src`: Directorio con el código fuente.
  * `fp.hearthstone`: Paquete que contiene los tipos de dominio de trabajo del proyecto.
  * `fp.commons`: Paquete que contiene las clases auxiliares.
  * `fp.hearthstone.test`: Paquete que contiene las clases de prueba del proyecto.
  * `fp.utiles`: Paquete que contiene las clases de utilidad. 
* `/data`: Contiene el dataset del proyecto.
  * `hearthstone_standard_cards.csv`: Dataset que contiene datos sobre las cartas de Hearthstone. 
    
## Estructura del *dataset*

El dataset Hearthstone: Heroes of Warcraft Standard Cards se puede obtener de la url [https://www.kaggle.com/datasets/davbyron/hearthstone-standard-cards](https://www.kaggle.com/datasets/davbyron/hearthstone-standard-cards). Originalmente tenía 26 columnas, y cada fila contiene datos sobre una carta de las que se encuentran en el juego base conseguidas usando la API de Blizzard, la desarrolladora del juego. El dataset utilizado en este proyecto tiene 15 columnas: 14 se han tomado del dataset original, y se ha generado aleatoriamente la fecha de salida de la carta. A continuación se describen 

* `id`: de tipo entero, un número de identificación única para la carta.
* `collectible`: de tipo booleano (1 o 0). Representa si la carta es coleccionable o no.
* `classId`: de tipo entero, el número de identificación de la clase principal a la que corresponde.
* `multiClassIds`: un set de enteros. En caso de que la carta solo tenga una clase, está vacío. En caso de que tenga varias, las incluye todas.
* `rarity`: un entero correspondiendo a la rareza de la carta. 1 para Common, 2 para Free, 3 para Rare, 4 para Epic, 5 para Legendary.
* `manaCost`: de tipo entero, el coste de maná de la carta.
* `health`: de tipo double, los puntos de vida de la carta.
* `attack`: de tipo double, el ataque de la carta.
* `name`: de tipo string, el nombre de la carta.
* `text`: de tipo string, el texto presente en combate de la carta.
* `flavorText`: de tipo string, el texto visible fuera de combate de la carta.
* `artistName`: de tipo string, el nombre del artista que creó la imagen de la carta.
* `image`: de tipo string, un link a la imagen de la carta completa.
* `cropImage`: de tipo string, un link a una versión recortada de la carta.
* `releaseDate`: de tipo fecha, la fecha de salida de la carta.

## Tipos implementados

Los tipos que se han implementado en el proyecto son los siguientes:

### Tipo Base - `Card`
Representa una carta concreta.
**Propiedades**:
Todas las propiedades son consultables pero no modificables.

- `id`: `Integer`, un número de identificación única para la carta.
- `collectible`: `Boolean`, representa si la carta es coleccionable o no.
- `slug`: `String`, propiedad derivada. Resumen de la carta con formato `id-nombre`.
- `classIds`: `Set<Integer>`, el conjunto de clases a las que corresponde la carta. Puede ser solo una.
- `rarity`: `Rarity`, la rareza de la carta.
- `manaCost`: `Integer`, el coste de maná de la carta.
- `health`: `Double`, los puntos de vida de la carta.
- `attack`: `Double`, el ataque de la carta.
- `name`: `String`, el nombre de la carta.
- `text`: `String`, el texto presente en combate de la carta.
- `flavorText`: `String`, el texto visible fuera de combate de la carta.
- `graphics`: `Graphics`, contiene el link a la imagen completa, el link a la imagen recortada y el nombre del artista de una carta.
- `releaseDate`: `LocalDate`, la fecha de salida de la carta.

**Constructores**: 

- C1: Tiene un parámetro por cada propiedad básica del tipo.
- C2: Tiene un parámetro por cada propiedad básica del tipo, excepto que collectible se pasa como un entero (1 o 0), rarity se pasa como un entero (1-5) y que en lugar de graphics se pasan el nombre del artista, el link a la imagen completa y el link a la imagen recortada.

**Restricciones**:
 
- R1: id ha de ser positivo o 0.
- R2: ha de tener alguna clase.
- R3: ha de tener un nombre.

**Criterio de igualdad**: Dos cartas son iguales si tienen la misma id y rareza. 

**Criterio de ordenación**: Por rareza e id.

#### Tipos auxiliares

- Rarity, enumerado. Puede tomar los valores `COMMON`, `FREE`, `RARE`, `EPIC` o `LEGENDARY`.
- Grahpics, record. Contiene la imagen sin recortar, la imagen recortada y el nombre del artista de una carta.

### Factoría - CardFactory

Clase de factoría para construir listas de cartas.

- `Card parseCard(String line)`: pasa de una línea en el formato del CSV a `Card`.
- `List<Card> readCards(String filePath)`: lee el CSV especificado en `filePath` y genera una lista de cartas, usando `parseCard` en cada línea.

### Contenedor - Cards

Clase contenedora de los objetos de tipo Card.

**Propiedades**:

- `cards`: `List<Card>`. Consultable, lista de cartas.

**Constructores**:

- C1: No toma ningún parámetro, resulta en una lista vacía.
- C2: Recibe una `List<Cards>`, y la usa para inicializar `cards`.

**Funciones**:
Implementa Collection<Card>, por lo que implementa:
- `int size()`: Devuelve el tamaño de la lista.
- `boolean add(Card card)`: Intenta añadir un elemento a la lista y devuelve `true` si lo logra.
- `boolean addAll(Collection<? extends Card> c)`: Intenta añadir todos los elementos de la lista y devuelve `true` si logra añadir alguno.
- `boolean remove(Object o)`: Intenta eliminar el objeto de la lista y devuelve `true` si lo logra.
entre otras.

**Tratamientos Secuenciales**:
- `boolean exists(Function<Card, Boolean> filter)`: devuelve `true` si algún elemento de la lista hace que `filter` devuelva `true`, false si no.
- `<T extends Number> Double average(Function<Card, T> key)`: devuelve el promedio de los valores que devuelve `key` a lo largo de la lista.
- `Cards filter(Function<Card, Boolean> filter)`: devuelve una nueva lista que contiene todos los elementos que hacen que `filter` devuelva `true`.
- `<T> Map<T, Cards> groupedBy(Function<Card, T> groupBy)`: Agrupa las cartas según el valor que devuelve `groupBy`.
- `<T, N extends Number> T accumulate(Function<Card, T> key, BinaryOperator<T> sumOp)`: Usando `sumOp` calcula la suma de los valores que devuelve `key`. `sumOp` es necesario ya que Java no implementa una interfaz para tipos sumables. Generalmente, se desea  usar `Integer::sum` o `Double::sum` como sumOp.
- `<T, N extends Number> Map<T,N> accumulateGroups(Function<Card, T> groupBy, Function<Card,N> key, BinaryOperator<N> sumOp)`: Agrupa las cartas usando como llaves lo que devuelve `groupBy`, y como valores correspondientes la suma usando `sumOp` de lo que devuelve `key`.

