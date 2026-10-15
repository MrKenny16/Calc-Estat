import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CalculadoraEstadistica {

    public static void main(String[] args) {

        if (args.length < 2 || args.length > 3) {
            System.err.println("Uso incorrecto. Sintaxis esperada:");
            System.err.println("java CalculadoraEstadistica <archivo.xml> <variable1> [variable2]");
            System.exit(1);
        }

        String filePath = args[0];
        String var1Name = args[1];
        String var2Name = args.length == 3 ? args[2] : null;

        File xmlFile = new File(filePath);
        if (!xmlFile.exists() || !xmlFile.canRead()) {
            System.err.println("Error: No se puede abrir o encontrar el documento XML especificado: " + filePath);
            System.exit(1);
        }

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();


            System.out.println("--- Estadísticas para la variable: '" + var1Name + "' ---");
            List<Double> datosVar1 = extraerDatosNumericos(document, var1Name);
            imprimirEstadisticas(datosVar1);


            if (var2Name != null) {
                System.out.println("\n--- Estadísticas para la variable: '" + var2Name + "' ---");
                List<Double> datosVar2 = extraerDatosNumericos(document, var2Name);
                imprimirEstadisticas(datosVar2);

                System.out.println("\n--- Análisis Bivariable ---");
                calcularEImprimirCorrelacion(datosVar1, datosVar2, var1Name, var2Name);
            }

        } catch (Exception e) {
            System.err.println("Error fatal al procesar el documento XML: " + e.getMessage());
            System.exit(1);
        }
    }


    private static List<Double> extraerDatosNumericos(Document doc, String tagName) {
        NodeList nodeList = doc.getElementsByTagName(tagName);

        if (nodeList.getLength() == 0) {
            System.err.println("Error: La variable '" + tagName + "' no existe en el documento o está escrita incorrectamente.");
            System.exit(1);
        }

        List<Double> datos = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            String textValue = nodeList.item(i).getTextContent().trim();
            try {
                double valor = Double.parseDouble(textValue);
                datos.add(valor);
            } catch (NumberFormatException e) {
                System.err.println("Error: La variable '" + tagName + "' contiene un valor no numérico: '" + textValue + "'.");
                System.exit(1);
            }
        }
        return datos;
    }


    private static void imprimirEstadisticas(List<Double> datos) {
        if (datos.isEmpty()) return;

        double suma = 0;
        for (double num : datos) {
            suma += num;
        }
        double media = suma / datos.size();

        double sumaDiferenciasCuadradas = 0;
        for (double num : datos) {
            sumaDiferenciasCuadradas += Math.pow(num - media, 2);
        }


        double varianza = datos.size() > 1 ? sumaDiferenciasCuadradas / (datos.size() - 1) : 0;
        double desviacionEstandar = Math.sqrt(varianza);


        double max = Collections.max(datos);
        double min = Collections.min(datos);

        System.out.printf("Número de datos (n) : %d%n", datos.size());
        System.out.printf("Media               : %.4f%n", media);
        System.out.printf("Varianza            : %.4f%n", varianza);
        System.out.printf("Desviación estándar : %.4f%n", desviacionEstandar);
        System.out.printf("Valor Máximo        : %.4f%n", max);
        System.out.printf("Valor Mínimo        : %.4f%n", min);
    }


    private static void calcularEImprimirCorrelacion(List<Double> var1, List<Double> var2, String nombre1, String nombre2) {
        if (var1.size() != var2.size()) {
            System.err.println("Advertencia: Las variables tienen diferente número de datos (" +
                    var1.size() + " vs " + var2.size() + "). No se puede calcular la correlación exacta " +
                    "sin emparejamiento. Se calculará hasta el límite del conjunto más pequeño.");
        }

        int n = Math.min(var1.size(), var2.size());
        if (n < 2) {
            System.err.println("Error: No hay suficientes pares de datos para calcular la correlación.");
            return;
        }

        double sumX = 0, sumY = 0;
        for (int i = 0; i < n; i++) {
            sumX += var1.get(i);
            sumY += var2.get(i);
        }
        double meanX = sumX / n;
        double meanY = sumY / n;

        double numerador = 0;
        double sumaCuadradosX = 0;
        double sumaCuadradosY = 0;

        for (int i = 0; i < n; i++) {
            double diffX = var1.get(i) - meanX;
            double diffY = var2.get(i) - meanY;
            numerador += (diffX * diffY);
            sumaCuadradosX += (diffX * diffX);
            sumaCuadradosY += (diffY * diffY);
        }

        double denominador = Math.sqrt(sumaCuadradosX * sumaCuadradosY);

        if (denominador == 0) {
            System.out.println("Coeficiente de correlación: Indefinido (una o ambas variables no tienen variación).");
        } else {
            double correlacion = numerador / denominador;
            System.out.printf("Coeficiente de correlación de Pearson (r): %.4f%n", correlacion);
        }
    }
}