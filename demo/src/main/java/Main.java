import java.util.Scanner;

import view.ConsolaUI;
import view.SwingUI;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Seleccione una opción:");
        System.out.println("1. Interfaz de consola");
        System.out.println("2. Interfaz gráfica");

        int opcion = scanner.nextInt();

        if (opcion == 1) {
            ConsolaUI ui = new ConsolaUI();
            ui.iniciar();
        } else if (opcion == 2) {
            SwingUI ventana = new SwingUI();
            ventana.start();
        } else {
            System.out.println("Opción no válida");
        }
    }
}
