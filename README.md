# Ongawa: Cloración

Programa para apoyar al proyecto de [Ongawa](http://www.ongawa.org) en Perú, en concreto al programa de cloración

## Entorno de desarrollo

Para el desarrollo de esta aplicación funciona utilizando Java 1.8 y [JavaFX](http://www.oracle.com/technetwork/java/javase/overview/javafx-overview-2158620.html). Los ejecutables se generan a través del lifecycle de Maven, que también gestiona dependencias y librerías.
Para editar el código, se recomienda el entorno de programación [Eclipse](https://eclipse.org/), 

###  Configurando el entorno
Al utilizar java y maven, es posible desarrollar la apliación en cualquier plataforma, ya sega Windows, Mac o GNU/Linux. En todos los casos se podrá generar un paquete .jar
con la aplicación y sus dependencias, ejecutable en todos los sistemas.

Sin embargo, es importante destacar que para generar los binarios nativos de cada sistema, como el .exe para windows, será necesario hacerlo desde el sistema deseado.

#### Windows 
_TODO_
Java JDK:https://docs.oracle.com/javase/8/docs/technotes/guides/install/windows_jdk_install.html#CHDEBCCJ
Maven: https://maven.apache.org/guides/getting-started/windows-prerequisites.html

#### GNU/Linux

En la mayoría de los sistemas basados en linux, tanto java 8 como maven están disponibles a través del gestor de paquetes del sistema. Por ejemplo, para sistemas basados en Debian:

```bash
root@ongawa:~/chlorination$ apt-get install openjdk-8-jdk maven 
``` 

#### Mac OSX
_TODO_
Java JDK: https://docs.oracle.com/javase/8/docs/technotes/guides/install/mac_jdk.html#CHDBADCG
Maven: http://stackoverflow.com/questions/8826881/maven-install-on-mac-os-x

### Compilar el código

Para compilar el proyecto y generar los ejecutables necesarios, es suficiente con ejecutar el goal _package_ de maven, que se encargará de configurar el entorno y generar los binarios empaquetados:

```bash
usuario@ongawa:~/chlorination$ mvn package
``` 

Para generar el jar empaquetado con dependencias, por el momento hay que ejecutar a la vez los goal _package_ y _assembly:single_, por una limitación en el assembly plugin de maven [StackOverflow](http://stackoverflow.com/questions/23777934/why-maven-assembly-plugin-does-not-include-my-project-files-in-the-jar-with-depe):

```bash
usuario@ongawa:~/chlorination$ mvn package assembly:single
``` 

## Autores

* Alberto Mardomingo
* Alfonso Portabales
* Ana Ortega-Gil
* Francisco Alario
* Grupo TIC de Ongawa

## Licencia
Copyright [2015] ONGAWA Ingeniería para el desarrollo humano

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
