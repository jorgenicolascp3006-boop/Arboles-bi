import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class BinaryTree {

    TreeNode root;

    public BinaryTree() {
        this.root = null;
    }

    public BinaryTree(TreeNode root) {
        this.root = root;
    }

    public void insertRoot(Object data) {
        if (root == null) {
            root = new TreeNode(data);
        }
    }

    public TreeNode search(Object data) {
        return search(root, data);
    }

    private TreeNode search(TreeNode node, Object data) {
        if (node == null) {
            return null;
        }
        if (node.data.equals(data)) {
            return node;
        }
        TreeNode found = search(node.left, data);
        if (found != null) {
            return found;
        }
        return search(node.right, data);
    }

    public boolean insertLeft(Object parent, Object data) {
        TreeNode p = search(parent);
        if (p == null || p.left != null) {
            return false;
        }
        p.left = new TreeNode(data);
        return true;
    }

    public boolean insertRight(Object parent, Object data) {
        TreeNode p = search(parent);
        if (p == null || p.right != null) {
            return false;
        }
        p.right = new TreeNode(data);
        return true;
    }

    private String readValue(Scanner scanner, String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            return null;
        }
        String s = scanner.nextLine().trim();
        if (s.isEmpty() || s.equals("-")) {
            return null;
        }
        return s;
    }

    public void buildTree(Scanner scanner) {
        root = null;
        String value = readValue(scanner, "Raiz: ");
        if (value == null) {
            return;
        }
        insertRoot(value);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode current = queue.poll();
            String left = readValue(scanner, "Hijo izquierdo de " + current.data + ": ");
            if (left != null) {
                current.left = new TreeNode(left);
                queue.add(current.left);
            }
            String right = readValue(scanner, "Hijo derecho de " + current.data + ": ");
            if (right != null) {
                current.right = new TreeNode(right);
                queue.add(current.right);
            }
        }
    }

    private static class Block {
        List<String> lines;
        int width;
        int pos;
    }

    private String label(TreeNode node, boolean withFactor) {
        String s = String.valueOf(node.data);
        if (withFactor) {
            s += "[" + balanceFactor(node) + "]";
        }
        return s;
    }

    private Block build(TreeNode node, boolean withFactor) {
        String text = label(node, withFactor);
        int n = text.length();
        Block l = node.left == null ? null : build(node.left, withFactor);
        Block r = node.right == null ? null : build(node.right, withFactor);
        Block b = new Block();
        if (l == null && r == null) {
            b.lines = new ArrayList<>();
            b.lines.add(text);
            b.width = n;
            b.pos = n / 2;
            return b;
        }
        int pc;
        int k = 1;
        boolean wide = false;
        int width;
        int rOff = 0;
        if (l != null && r != null) {
            int gap = 2;
            while (true) {
                int rr = l.width + gap + r.pos;
                if ((l.pos + rr) % 2 == 0 && rr - l.pos >= 2) {
                    break;
                }
                gap++;
            }
            rOff = l.width + gap;
            pc = (l.pos + rOff + r.pos) / 2;
            k = pc - l.pos;
            wide = k > 1;
            width = rOff + r.width;
        } else if (l != null) {
            pc = l.pos + 1;
            width = l.width;
        } else {
            pc = r.pos - 1;
            width = r.width;
        }
        int childRows = Math.max(l == null ? 0 : l.lines.size(), r == null ? 0 : r.lines.size());
        if (wide) {
            k = 1;
        }
        int h = 1 + k + childRows;
        char[][] grid = new char[h][width];
        for (char[] row : grid) {
            java.util.Arrays.fill(row, ' ');
        }
        int rootR = rOff + (r == null ? 0 : r.pos);
        if (wide) {
            k = 1;
            grid[1][l.pos + 1] = '/';
            grid[1][rootR - 1] = '\\';
        } else {
            for (int i = 1; i <= k; i++) {
                if (l != null) {
                    grid[i][pc - i] = '/';
                }
                if (r != null) {
                    grid[i][pc + i] = '\\';
                }
            }
        }
        if (l != null) {
            for (int j = 0; j < l.lines.size(); j++) {
                String line = l.lines.get(j);
                for (int c = 0; c < line.length(); c++) {
                    grid[1 + k + j][c] = line.charAt(c);
                }
            }
        }
        if (r != null) {
            for (int j = 0; j < r.lines.size(); j++) {
                String line = r.lines.get(j);
                for (int c = 0; c < line.length(); c++) {
                    grid[1 + k + j][rOff + c] = line.charAt(c);
                }
            }
        }
        int p = pc - n / 2;
        int shift = Math.max(0, -p);
        p += shift;
        int total = Math.max(width + shift, p + n);
        String pad = " ".repeat(shift);
        b.lines = new ArrayList<>();
        StringBuilder top = new StringBuilder(" ".repeat(total));
        top.replace(p, p + n, text);
        if (wide) {
            for (int c = l.pos + 1 + shift; c < p; c++) {
                top.setCharAt(c, '_');
            }
            for (int c = p + n; c < rootR - 1 + shift; c++) {
                top.setCharAt(c, '_');
            }
        }
        b.lines.add(top.toString());
        for (int i = 1; i < h; i++) {
            StringBuilder sb = new StringBuilder(pad).append(new String(grid[i]));
            while (sb.length() < total) {
                sb.append(' ');
            }
            b.lines.add(sb.toString());
        }
        b.width = total;
        b.pos = pc + shift;
        return b;
    }

    private void draw(boolean withFactor) {
        if (root == null) {
            System.out.println("El arbol esta vacio.");
            return;
        }
        Block b = build(root, withFactor);
        for (String line : b.lines) {
            System.out.println(line.stripTrailing());
        }
    }

    public void printTree() {
        draw(false);
    }

    public String preorder() {
        StringBuilder sb = new StringBuilder();
        preorder(root, sb);
        return sb.toString().trim();
    }

    private void preorder(TreeNode node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        sb.append(node.data).append(' ');
        preorder(node.left, sb);
        preorder(node.right, sb);
    }

    public String inorder() {
        StringBuilder sb = new StringBuilder();
        inorder(root, sb);
        return sb.toString().trim();
    }

    private void inorder(TreeNode node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        inorder(node.left, sb);
        sb.append(node.data).append(' ');
        inorder(node.right, sb);
    }

    public String postorder() {
        StringBuilder sb = new StringBuilder();
        postorder(root, sb);
        return sb.toString().trim();
    }

    private void postorder(TreeNode node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        postorder(node.left, sb);
        postorder(node.right, sb);
        sb.append(node.data).append(' ');
    }

    public int height(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }

    public int balanceFactor(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return height(node.right) - height(node.left);
    }

    public boolean isBalanced() {
        return isBalanced(root);
    }

    private boolean isBalanced(TreeNode node) {
        if (node == null) {
            return true;
        }
        int bf = balanceFactor(node);
        if (bf < -1 || bf > 1) {
            return false;
        }
        return isBalanced(node.left) && isBalanced(node.right);
    }

    public List<Object> unbalancedNodes() {
        List<Object> result = new ArrayList<>();
        collectUnbalanced(root, result);
        return result;
    }

    private void collectUnbalanced(TreeNode node, List<Object> result) {
        if (node == null) {
            return;
        }
        int bf = balanceFactor(node);
        if (bf < -1 || bf > 1) {
            result.add(node.data);
        }
        collectUnbalanced(node.left, result);
        collectUnbalanced(node.right, result);
    }

    public void printBalanceFactors() {
        draw(true);
    }

    public TreeNode rotateRight(TreeNode node) {
        if (node == null || node.left == null) {
            return node;
        }
        TreeNode pivot = node.left;
        node.left = pivot.right;
        pivot.right = node;
        return pivot;
    }

    public TreeNode rotateLeft(TreeNode node) {
        if (node == null || node.right == null) {
            return node;
        }
        TreeNode pivot = node.right;
        node.right = pivot.left;
        pivot.left = node;
        return pivot;
    }

    public TreeNode rotateLeftRight(TreeNode node) {
        if (node == null) {
            return null;
        }
        node.left = rotateLeft(node.left);
        return rotateRight(node);
    }

    public TreeNode rotateRightLeft(TreeNode node) {
        if (node == null) {
            return null;
        }
        node.right = rotateRight(node.right);
        return rotateLeft(node);
    }

    private TreeNode rebalance(TreeNode node) {
        if (node == null) {
            return null;
        }
        node.left = rebalance(node.left);
        node.right = rebalance(node.right);
        int bf = balanceFactor(node);
        Object data = node.data;
        if (bf < -1) {
            if (balanceFactor(node.left) <= 0) {
                System.out.println("Rotacion simple a la derecha en el nodo " + data);
                return rotateRight(node);
            }
            System.out.println("Rotacion doble izquierda-derecha en el nodo " + data);
            return rotateLeftRight(node);
        }
        if (bf > 1) {
            if (balanceFactor(node.right) >= 0) {
                System.out.println("Rotacion simple a la izquierda en el nodo " + data);
                return rotateLeft(node);
            }
            System.out.println("Rotacion doble derecha-izquierda en el nodo " + data);
            return rotateRightLeft(node);
        }
        return node;
    }

    public void balance() {
        if (root == null) {
            System.out.println("El arbol esta vacio.");
            return;
        }
        System.out.println("Arbol original (factor de equilibrio entre corchetes):");
        printBalanceFactors();
        List<Object> unbalanced = unbalancedNodes();
        if (unbalanced.isEmpty()) {
            System.out.println("Ningun nodo requiere balanceo; el arbol ya es AVL.");
            return;
        }
        System.out.println("Nodos que requieren balanceo: " + unbalanced);
        int passes = 0;
        while (!isBalanced() && passes < 1000) {
            root = rebalance(root);
            passes++;
        }
        System.out.println("Arbol despues del balanceo:");
        printBalanceFactors();
    }
}
