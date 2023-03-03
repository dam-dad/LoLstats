# LoLstats

Estadísticas y calculadora de victorias de LoL

## APIs utilizadas

- MatchV5 API

- SummonerV4 API

- LeagueV4 API

## Cómo usar

Para usar la aplicación, hay que insertar una API key que dura 24h y es personal. Para obtenerla, al arrancar la app se hace click en el botón "GET" y serás redirigido a la página donde se obtiene ésta. Simplemente iniciar sesión con una cuenta de Riot Games.

![](C:\Users\elson\AppData\Roaming\marktext\images\2023-03-03-12-19-29-image.png)

Una vez copiada la API key, la pegamos en el recuadro que vemos en la imagen y pulsamos next. Aparecerá la siguiente pantalla

![](C:\Users\elson\AppData\Roaming\marktext\images\2023-03-03-12-21-08-image.png)

Esta es la pantalla de búsqueda de cuenta. En el seleccionador de servidor, elegimos el servidor en el que se haya creado la cuenta que vamos a buscar. Si no se cambia, buscará en "West Europe" por defecto. Al pulsar "Search" se nos lleva a la siguiente pantalla.



![](C:\Users\elson\AppData\Roaming\marktext\images\2023-03-03-12-24-18-image.png)

A la derecha se mostrarán las 20 últimas partidas jugadas por el jugador, con su build, kda, runas, summoners, farm... A la izquierda podemos ver el rango del jugador y el winrate de las partidas clasificatorias, que sólo se muestra si el jugador tiene rando en "RANKED SOLO/DUO". Si deseamos ver los datos de otra cuenta, pulsamos en el botón rojo y nos devolverá a la pantalla de Login, para buscar otra cuenta.

Los botones verdes de la izquierda son las otras 2 funcionalidades de la aplicación.

![](C:\Users\elson\AppData\Roaming\marktext\images\2023-03-03-12-31-23-image.png)

Si pulsamos este botón, se generará un informe de las 20 partidas jugadas. 

![](C:\Users\elson\AppData\Roaming\marktext\images\2023-03-03-12-32-45-image.png)

Si pulsamos este otro, se nos llevará a la última función de la aplicación, la calculadora de victorias.

![](C:\Users\elson\AppData\Roaming\marktext\images\2023-03-03-12-34-18-image.png)

En esta pantalla, debemos elegir el icono de la liga deseada, division que se desea llegar, agregar el número de LP ganados por partida y por último ya podemos pulsar en el botón de calcular.



En el botón de arriba a la izquierda, podemos volver a la visualización de partidas. Mientras no cerremos la aplicación ni cambiemos de usuario, los datos no se perderán.

## Tecnologías usadas

* JavaFX

* Maven

* RetroFit2

* JasperReports

* JFoenix

* Ejecutable generado con launch4j
