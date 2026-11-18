import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BinaryTree tree = new BinaryTree();
        int option = 0;

        while (option != 8) {
            System.out.println();
            System.out.println("========== MENU ==========");
            System.out.println("1. Construir arbol");
            System.out.println("2. Mostrar arbol");
            System.out.println("3. Recorrido Preorden");
            System.out.println("4. Recorrido Inorden");
            System.out.println("5. Recorrido Posorden");
            System.out.println("6. Mostrar factores de equilibrio");
            System.out.println("7. Balancear arbol (AVL)");
            System.out.println("8. Salir");
            System.out.println("==========================");
            System.out.print("Seleccione una opcion: ");

            if (!scanner.hasNextLine()) {
                break;
            }
            try {
                option = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                option = 0;
            }

            switch (option) {
                case 1:
                    tree.buildTree(scanner);
                    if (tree.root != null) {
                        System.out.println();
                        tree.printTree();
                    }
                    break;
                case 2:
                    tree.printTree();
                    break;
                case 3:
                    System.out.println(tree.root == null ? "El arbol esta vacio." : "Preorden: " + tree.preorder());
                    break;
                case 4:
                    System.out.println(tree.root == null ? "El arbol esta vacio." : "Inorden: " + tree.inorder());
                    break;
                case 5:
                    System.out.println(tree.root == null ? "El arbol esta vacio." : "Posorden: " + tree.postorder());
                    break;
                case 6:
                    tree.printBalanceFactors();
                    break;
                case 7:
                    tree.balance();
                    break;
                case 8:
                    System.out.println("Hasta luego.");
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        }
    }
}
