# Aplicación de vinilos MISW-4203 Grupo 6

<div align="start">
 <code><img width="30" src="https://user-images.githubusercontent.com/25181517/192108895-20dc3343-43e3-4a54-a90e-13a4abbc57b9.png" alt="Android Studio" title="Android Studio"/></code>
 <code><img width="30" src="https://user-images.githubusercontent.com/25181517/117269608-b7dcfb80-ae58-11eb-8e66-6cc8753553f0.png" alt="Android" title="Android"/></code>
</div>

## Instalación de IDE

1. Instalar el JDK 11 LTS lo puede encontrar en el siguiente [enlace](https://www.oracle.com/co/java/technologies/downloads/).
2. Instalar Android Studio en su más reciente versión, puede revisar la guía oficial de instalación [aquí](https://developer.android.com/studio/install?gad_source=1&gclid=Cj0KCQjw_qexBhCoARIsAFgBlevTCx3tup9CejhultYVgJQRzspfciF8NnMqF1ay8bPddEbF410KqLsaApadEALw_wcB&gclsrc=aw.ds).
3. Clonar el proyecto con el comando `git clone https://github.com/Vinilos-MISW4203/VinilosApp.git` en su carpeta de preferencia.
4. Abrir el proyecto en Android Studio **File > Open** y escoge la ruta en la cual clono el repositorio, selecciona el repo **VinilosApp** y da clic en OK

<div align="center">
    <img src="https://raw.githubusercontent.com/wiki/Vinilos-MISW4203/VinilosApp/assets/readme/01.gif" width="500" height="300">
</div>

5. Ir a la opción **File > Sync project with gradle file** para descargar las dependencias de la aplicación u oprime la combinación **Ctrl + Mayusc + O**, esperar que se instalen todas las dependencias.

## Inicializar App en dispositivo físico y virtual desde el código

1. Si tienes un dispositivo físico Android conecta lo al equipo. *Previamente debes habilitar las opciones de desarrollador*, una vez conectado el dispositivo, en Android Studio ve a **Run > Run 'app'** y la aplicación se instalará automáticamente en el dispositivo conectado.

2. Si no tienes un dispositivo puedes instalar un dispositivo virtual como se ve en la siguiente imagen

<div align="center">
    <img src="https://raw.githubusercontent.com/wiki/Vinilos-MISW4203/VinilosApp/assets/readme/02.gif" width="500" height="300">
</div>

una vez instalado ve a **Run > Run 'app'** y la aplicación se instalará y ejecutará automáticamente, puedes ver el dispositivo ejecutándose en el menú de la derecha en *Running Devices*.

## Inicializar App en dispositivo virtual desde el APK

1. En el repositorio descargado anteriormente encontraras un archivo en la carpeta **APK** llamado **appVinilo.apk**
2. Si vas a usar un dispositivo virtual puedes pasar el archivo arrastrándolo al dispositivo virtual como se ve en la siguiente Imagen, la aplicación se instalará automáticamente.

<div align="center">
    <img src="https://raw.githubusercontent.com/wiki/Vinilos-MISW4203/VinilosApp/assets/readme/03.gif" width="500" height="300">
</div>

## Inicializar App en dispositivo físico desde el APK

1. Si usas un dispositivo físico conecta el dispositivo y habilita la transferencia de archivos,
2. una vez pases el archivo al dispositivo desconecta lo y desde el dispositivo ejecuta el archivo en donde lo guardaste.
3. El dispositivo te pedirá autorización para instalar la app.
4. la app se ejecutará automáticamente.

## Ejecutar las pruebas

1. En la parte superior derecha del proyecto ve a la carpeta Kotlin+java y encontraras dos carpetas de color verde con los nombres **com.vinilos.misw4203.grupo6_202412**. La carpeta con la etiqueta *(androidTest)* son las pruebas E2E generadas con la librería de compose y la carpeta con la etiqueta *(test)* son las pruebas unitarias, puedes dar clic derecho a una de las dos y da clic en *Run 'Test in vinilo..'* o *Ctrl+Mayús+F10*, las pruebas se ejecutaran automáticamente.

2. si quieres ejecutar la prueba especifica expande la carpeta y da clic en *Run 'Test in vinilo..'* o *Ctrl+Mayús+F10* al archivo que se desea ejecutar, las pruebas se ejecutaran automáticamente como se ve en la siguiente imagen.

<div align="center">
    <img src="https://raw.githubusercontent.com/wiki/Vinilos-MISW4203/VinilosApp/assets/readme/04.gif" width="500" height="300">
</div>
