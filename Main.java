import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// --- MODEL ---
class Coordinate {
    double x, y;
    int label;

    public Coordinate(double x, double y, int label) {
        this.x = x;
        this.y = y;
        this.label = label;
    }
}

// --- DATASET ---
class DatasetManager {
    private List<Coordinate> dataset = new ArrayList<>();

    public void loadDummyData() {
        Random rand = new Random();

        generateData(rand, 5.0, 1, 20);
        generateData(rand, 1.0, -1, 20);
    }

    private void generateData(Random rand, double offset, int label, int count) {
        // İstenen sayıda geçerli nokta üretilene kadar döngü devam eder
        int i = 0;
        while (i < count) {
            double x = offset + rand.nextGaussian();
            double y = offset + rand.nextGaussian();

            // Filtreleme: Hesaplama ve tabloyu bozmaması için sıfır veya boş değerler dışarıda bırakılır
            if (x != 0.0 && y != 0.0) {
                dataset.add(new Coordinate(x, y, label));
                i++;
            }
        }
    }

    public double[][] getX() {
        double[][] X = new double[dataset.size()][2];
        for (int i = 0; i < dataset.size(); i++) {
            X[i][0] = dataset.get(i).x;
            X[i][1] = dataset.get(i).y;
        }
        return X;
    }

    public int[] getY() {
        int[] y = new int[dataset.size()];
        for (int i = 0; i < dataset.size(); i++) {
            y[i] = dataset.get(i).label;
        }
        return y;
    }
}

// --- SVM SOLVER ---
class SVMSolver {
    double lr, lambda;
    int epochs;
    double[] w;
    double b;

    public SVMSolver(double lr, double lambda, int epochs) {
        this.lr = lr;
        this.lambda = lambda;
        this.epochs = epochs;
    }

    public void fit(double[][] X, int[] y) {
        int nFeatures = X[0].length;
        w = new double[nFeatures];
        b = 0;

        for (int epoch = 0; epoch < epochs; epoch++) {
            for (int i = 0; i < X.length; i++) {

                double dot = X[i][0] * w[0] + X[i][1] * w[1];

                // DOĞRU FORMÜL
                if (y[i] * (dot + b) >= 1) {
                    w[0] -= lr * (2 * lambda * w[0]);
                    w[1] -= lr * (2 * lambda * w[1]);
                } else {
                    w[0] -= lr * (2 * lambda * w[0] - y[i] * X[i][0]);
                    w[1] -= lr * (2 * lambda * w[1] - y[i] * X[i][1]);
                    // DÜZELTİLEN KISIM: Türev kuralı gereği eksi ile çarpımdan dolayı artı olmalıdır.
                    b += lr * y[i];
                }
            }
        }
    }
}

// --- GUI ---
class NavigationGUI extends JPanel {
    double[][] X;
    int[] y;
    SVMSolver solver;

    public NavigationGUI(double[][] X, int[] y, SVMSolver solver) {
        this.X = X;
        this.y = y;
        this.solver = solver;
        setPreferredSize(new Dimension(800, 600));
    }

    // DOĞRU HYPERPLANE DENKLEMİ
    private double getY(double x, double offset) {
        return (-solver.w[0] * x - solver.b + offset) / solver.w[1];
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int scale = 60;
        int originX = getWidth() / 4;
        int originY = (getHeight() * 3) / 4;

        // POINTS
        for (int i = 0; i < X.length; i++) {
            int px = originX + (int)(X[i][0] * scale);
            int py = originY - (int)(X[i][1] * scale);

            g2.setColor(y[i] == 1 ? Color.BLUE : Color.RED);
            g2.fillOval(px - 5, py - 5, 10, 10);
        }

        double minX = -2, maxX = 8;

        int x1 = originX + (int)(minX * scale);
        int x2 = originX + (int)(maxX * scale);

        // MAIN LINE (Hiperdüzlem)
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        int y1 = originY - (int)(getY(minX, 0) * scale);
        int y2 = originY - (int)(getY(maxX, 0) * scale);
        g2.draw(new Line2D.Double(x1, y1, x2, y2));

        // MARGIN -1 (Sol Güvenlik Sınırı)
        g2.setColor(Color.RED);
        int ym1 = originY - (int)(getY(minX, -1) * scale);
        int ym2 = originY - (int)(getY(maxX, -1) * scale);
        g2.draw(new Line2D.Double(x1, ym1, x2, ym2));

        // MARGIN +1 (Sağ Güvenlik Sınırı)
        g2.setColor(Color.BLUE);
        int yp1 = originY - (int)(getY(minX, 1) * scale);
        int yp2 = originY - (int)(getY(maxX, 1) * scale);
        g2.draw(new Line2D.Double(x1, yp1, x2, yp2));
    }
}

// --- MAIN ---
public class Main {
    public static void main(String[] args) {

        DatasetManager dm = new DatasetManager();
        dm.loadDummyData();

        double[][] X = dm.getX();
        int[] y = dm.getY();

        SVMSolver svm = new SVMSolver(0.001, 0.01, 2000);
        svm.fit(X, y);

        JFrame frame = new JFrame("Otonom Araç SVM Güvenlik Koridoru - Amine Aksu (1240505053)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new NavigationGUI(X, y, svm));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}