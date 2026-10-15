

Validación inicial: Verifica que al ejecutar el programa se haya proveído el nombre del archivo y al menos una variable. Si falta información o el archivo no existe, detiene la ejecución para evitar fallos del sistema.

Lectura y Parseo del XML: Utiliza la librería nativa javax.xml.parsers para cargar el archivo (por ejemplo, datos.xml) en la memoria y recorrer su estructura de árbol nodo por nodo.

Manejo de colecciones: Al encontrar la etiqueta solicitada por el usuario (ej. <temperatura>), extrae su texto, lo convierte a tipo numérico (Double) y lo almacena en un ArrayList<Double>. Esta clase del Java Collections API permite almacenar una cantidad indefinida de datos sin necesidad de declarar el tamaño exacto del arreglo previamente.

Cálculos estadísticos: Una vez llena la lista, un bucle for-each recorre el ArrayList para sumar los valores y obtener la media, la varianza y la desviación estándar. Para los valores extremos, se ahorra código utilizando los métodos integrados Collections.max() y Collections.min().

Correlación de Pearson: Si el usuario solicita analizar una segunda variable, el programa extrae esos datos en un segundo ArrayList, comprueba que ambas listas tengan tamaños compatibles, y aplica la fórmula de suma de cuadrados y diferencias para obtener la correlación.

Para usar el programa desde Bash primero se compila el código, y luego se ejecuta el archivo resultante
Traduce el código escrito en texto:

javac CalculadoraEstadistica.java datos.xml temperatura humedad


