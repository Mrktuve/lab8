package client.gui;

import common.model.*;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import java.util.*;
import java.util.function.Consumer;

public class CanvasPanel extends Pane {
    private final Canvas canvas;
    private final Localization localization;
    private final Session session;
    private List<Worker> workers = List.of();
    private double scale = 1.0;
    private double offsetX = 0, offsetY = 0;
    private Point2D lastMouse;
    private Consumer<Worker> onWorkerClick;

    // Цвета по владельцу
    private final Map<String, Color> ownerColors = new HashMap<>();
    private static final Color[] PALETTE = {
            Color.CORAL, Color.DODGERBLUE, Color.MEDIUMSEAGREEN, Color.DARKORANGE,
            Color.MEDIUMPURPLE, Color.GOLD, Color.DEEPPINK, Color.TEAL,
            Color.CRIMSON, Color.STEELBLUE, Color.OLIVEDRAB, Color.SLATEBLUE
    };

    public CanvasPanel(Localization localization, Session session) {
        this.localization = localization;
        this.session = session;
        canvas = new Canvas(800, 600);
        getChildren().add(canvas);
        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());

        canvas.setOnMousePressed(e -> lastMouse = new Point2D(e.getX(), e.getY()));
        canvas.setOnMouseDragged(e -> {
            if (lastMouse != null) {
                offsetX += e.getX() - lastMouse.getX();
                offsetY += e.getY() - lastMouse.getY();
                lastMouse = new Point2D(e.getX(), e.getY());
                redraw();
            }
        });
        canvas.setOnScroll(this::handleScroll);
        canvas.setOnMouseClicked(this::handleClick);

        widthProperty().addListener((o, ov, nv) -> redraw());
        heightProperty().addListener((o, ov, nv) -> redraw());
    }

    public void setOnWorkerClick(Consumer<Worker> handler) { this.onWorkerClick = handler; }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers != null ? workers : List.of();
        assignOwnerColors();
        redraw();
    }

    private void assignOwnerColors() {
        ownerColors.clear();
        int idx = 0;
        for (Worker w : workers) {
            String owner = w.getOwnerLogin() != null ? w.getOwnerLogin() : "unknown";
            if (!ownerColors.containsKey(owner)) {
                ownerColors.put(owner, PALETTE[idx % PALETTE.length]);
                idx++;
            }
        }
    }

    private Color getColorFor(Worker w) {
        String owner = w.getOwnerLogin() != null ? w.getOwnerLogin() : "unknown";
        return ownerColors.getOrDefault(owner, Color.GRAY);
    }

    private void handleScroll(ScrollEvent e) {
        scale *= e.getDeltaY() > 0 ? 1.1 : 0.9;
        scale = Math.max(0.1, Math.min(scale, 5.0));
        redraw();
    }

    private void handleClick(MouseEvent e) {
        double mx = (e.getX() - offsetX) / scale;
        double my = (e.getY() - offsetY) / scale;
        for (Worker w : workers) {
            double[] pos = getWorkerScreenPos(w);
            double dx = mx - pos[0], dy = my - pos[1];
            if (dx * dx + dy * dy < 900) {
                if (onWorkerClick != null) onWorkerClick.accept(w);
                return;
            }
        }
    }

    public void redraw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        if (workers.isEmpty()) {
            gc.setFill(Color.GRAY);
            gc.setFont(Font.font(18));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText(localization.get("canvas.empty"), canvas.getWidth() / 2, canvas.getHeight() / 2);
            return;
        }
        gc.save();
        gc.translate(offsetX, offsetY);
        gc.scale(scale, scale);
        drawWorkers(gc);
        gc.restore();
    }

    private void drawWorkers(GraphicsContext gc) {
        for (Worker w : workers) {
            double[] pos = getWorkerScreenPos(w);
            drawWorker(gc, w, pos[0], pos[1]);
        }
    }

    private double[] getWorkerScreenPos(Worker w) {
        double cx = canvas.getWidth() / 2;
        double cy = canvas.getHeight() / 2;
        double x = cx, y = cy;
        Coordinates c = w.getCoordinates();
        if (c != null) {
            x = cx + c.getX();
            y = cy - c.getY();
        }
        return new double[]{x, y};
    }

    private void drawWorker(GraphicsContext gc, Worker worker, double x, double y) {
        Color color = getColorFor(worker);
        double size = 50;

        gc.setFill(color);
        gc.fillOval(x - size / 2, y - size / 2, size, size);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeOval(x - size / 2, y - size / 2, size, size);

        gc.setFill(Color.BLACK);
        gc.setFont(Font.font(11));
        gc.setTextAlign(TextAlignment.CENTER);
        String name = worker.getName().length() > 10 ?
                worker.getName().substring(0, 10) + "..." : worker.getName();
        gc.fillText(name, x, y - size / 2 - 5);
        if (worker.getSalary() != null) {
            gc.fillText("$" + worker.getSalary().intValue(), x, y + size / 2 + 15);
        }
        gc.setFill(Color.DARKGRAY);
        gc.setFont(Font.font(9));
        gc.fillText(worker.getOwnerLogin(), x, y + size / 2 + 28);
    }
}