
# Cloración

En este documento intentamos definir los requisitos y la funcionalidad del sistema.

## Requisitos

Actualmente, los cálculos para los sistemas de cloración de las JASS en Perú, se realizan a través de un conjunto de hojas de Excel, un proceso farragoso y propenso a error.

Para mejorlarlo, se propone el desarrollo de una aplicación que permita:
  -  Calcular las necesidades quincenales de cloro de un sistema de agua de acuerdo a valores medidos
  -  Calcular los necesidades de desinfección bianual de un sistema de agua
  -  Diseñar parámetros para nuevos sistemas
  -  Validar los datos introducidos antes de realizar el cálculo.
  -  Presentar de forma sencilla y fácil de entender información sobre la forma de tomar las medidas.
  -  Informar de errores en valores introducidos, y el motivo.
  -  Permitir diferenciar entre los diferentes sistemas de agua potable, alamacenando información de cada uno de ellos.
  -  Generar informes con los resultados de cloración

Los usuarios emplean mayoritariamente sistemas operativos Microsoft Windows, variando desde XP a 8.1, tanto de 32 como de 64bit.

## Funcionalidad

### Gestión: cloración

Se realiza quincenalemente, y son necesarios datos estáticos del sistema, que varían poco a lo largo del tiempo, así como mediciones específicas para cada momento.
 - Datos estáticos:
   - Datos de cuenca (subcuenca, localidad, sistema) - No afectan directamente al calculo
   - Datos del sistema: Número de familias y de habitantes ( Habitantes = familias x 5)
 - Mediciones:
   -  Caudal Natural.
   -  Caudal a clorar - menor que el natural.
   -  Volumen del tanque de almacenamiento.
   -  Pureza de cloro.
   -  Horas de goteo diario.
 
Con estos datos, se generan dos resultados:
 - Demanda de cloro - Cantidad de cloro comercial necesario
 - Caudal de goteo

### Gestión: Desinfección

Al menos dos veces al año, se debe realizar una desinfección del sistema, con una cantidad mayor de cloro introducida en el origen. Para ello se debe tener el cuenta el tamaño total del sistema y sus diferentes elementos
   - Captación: Recepción de agua del manantial
   - Tuberías / red de distribución
   - Camaras Rompe Presión
   - Reservario
  
 Se deben considerar el tamaño y volumen de todos los elementos, pudiendo haber varios similares. También debe ser posible introducir una única vez las dimensiones del sistema y que se guarden para la siguiente

Se debe generar:
  - Cucharadas de cloro por elemento
  - Kilogramos totales de cloro comercial
  - Tiempo de retención

### Diseño

Además de los calculos anteriores, debe ser posible diseñar nuevos sistemas, así como experimentar con los diferentes parámetros de diseño.
  - Parametros de cuenca: Cuenca, localidad y (nuevo) sistema
    - Número de familias y habitantes actuales
    - Tasa de crecimiento esperada (2% habitual) que permita dimensionar el sistema a largo plazo, contando con que serán necesarios 80 litros por persona y día
  - Calidad del agua: Medidas realizadas sobre la fuente de agua
    - Turbidez (necesariamente menor que 5)
    - PH ( debe estar entre 6.5 y 7-5 ?)
    - Temperatura del agua - para ajustar el volumen del tanque (mejor más pequeño)
  - Caudal natural de la fuente 
  - Parámetros económicos:
    - Precio del cloro
    - Desinfecciones al año
    - Gestión JASS
    - Repuestos
    - Pago a operadores

Con estos datos, se deberá obtener la viabilidad del sistema, así como los costes de operación, y las cantidades aproximadas de cloro necesarias:
  - Cuotas familiares, en soles
  - Cloro necesario para operación (quincenal)
  - Cloro neceasario para desinfecciones (bianual)

