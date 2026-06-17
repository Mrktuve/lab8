package client.gui;

import client.gui.localization.localization;
import common.model.Coordinates;
import common.model.Worker;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.*;
import java.util.function.Consumer;

public class CanvasPanel extends Pane {

    private static final Color[] PALETTE = {Color.CORAL, Color.DODGERBLUE, Color.MEDIUMSEAGREEN, Color.DARKORANGE, Color.MEDIUMPURPLE, Color.GOLD, Color.DEEPPINK, Color.TEAL, Color.CRIMSON, Color.STEELBLUE, Color.OLIVEDRAB, Color.SLATEBLUE};
    private final Canvas canvas;
    private final localization localization;
    private final Session session;
    // Minecraft карта
    private final Image mapImage;
    // Цвета пользователей
    private final Map<String, Color> ownerColors = new HashMap<>();
    private List<Worker> workers = List.of();
    private double scale = 1.0;
    private double offsetX = 0;
    private double offsetY = 0;
    private Point2D lastMouse;
    private Consumer<Worker> onWorkerClick;

    public CanvasPanel(localization localization, Session session) {
        this.localization = localization;
        this.session = session;

        canvas = new Canvas(800, 600);
        getChildren().add(canvas);

        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());

        // Загрузка minecraft map
        mapImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/minecraft_map.png")));

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

    public void setOnWorkerClick(Consumer<Worker> handler) {
        this.onWorkerClick = handler;
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers != null ? workers : List.of();

        assignOwnerColors();
        redraw();
    }

    private void assignOwnerColors() {
        ownerColors.clear();

        int index = 0;

        for (Worker worker : workers) {

            String owner = worker.getOwnerLogin() != null ? worker.getOwnerLogin() : "unknown";

            if (!ownerColors.containsKey(owner)) {

                ownerColors.put(owner, PALETTE[index % PALETTE.length]);

                index++;
            }
        }
    }

    private Color getColorFor(Worker worker) {
        String owner = worker.getOwnerLogin() != null ? worker.getOwnerLogin() : "unknown";

        return ownerColors.getOrDefault(owner, Color.GRAY);
    }

    private void handleScroll(ScrollEvent e) {

        scale *= e.getDeltaY() > 0 ? 1.1 : 0.9;

        scale = Math.max(0.3, Math.min(scale, 5.0));

        redraw();
    }

    private void handleClick(MouseEvent e) {

        for (Worker worker : workers) {

            double[] pos = calculatePosition(worker);

            double dx = e.getX() - pos[0];

            double dy = e.getY() - pos[1];

            if (dx * dx + dy * dy < 900) {

                if (onWorkerClick != null) {
                    onWorkerClick.accept(worker);
                }

                return;
            }
        }
    }

    public void redraw() {

        GraphicsContext gc = canvas.getGraphicsContext2D();

        double width = canvas.getWidth();

        double height = canvas.getHeight();

        // Minecraft background
        gc.drawImage(mapImage, 0, 0, width, height);

        if (workers.isEmpty()) {

            gc.setFill(Color.WHITE);

            gc.setFont(Font.font(20));

            gc.setTextAlign(TextAlignment.CENTER);

            gc.fillText(localization.get("canvas.empty"), width / 2, height / 2);

            return;
        }

        drawGrid(gc);
        drawWorkers(gc);
    }

    private void drawGrid(GraphicsContext gc) {

        gc.setStroke(Color.rgb(255, 255, 255, 0.15));

        gc.setLineWidth(1);

        for (int x = 0; x < canvas.getWidth(); x += 50) {

            gc.strokeLine(x, 0, x, canvas.getHeight());
        }

        for (int y = 0; y < canvas.getHeight(); y += 50) {

            gc.strokeLine(0, y, canvas.getWidth(), y);
        }
    }

    private void drawWorkers(GraphicsContext gc) {

        for (Worker worker : workers) {

            double[] pos = calculatePosition(worker);

            drawWorker(gc, worker, pos[0], pos[1]);
        }
    }

    private double[] calculatePosition(Worker worker) {

        Coordinates c = worker.getCoordinates();

        double x = c != null ? c.getX() : 0;

        double y = c != null ? c.getY() : 0;

        double canvasWidth = canvas.getWidth();

        double canvasHeight = canvas.getHeight();

        double drawX = (canvasWidth / 2) + (x * scale) + offsetX;

        double drawY = (canvasHeight / 2) - (y * scale) + offsetY;

        return new double[]{drawX, drawY};
    }

    private void drawWorker(GraphicsContext gc, Worker worker, double x, double y) {

        Color color = getColorFor(worker);

        double size = 35;

        gc.setFill(color);

        gc.fillOval(x - size / 2, y - size / 2, size, size);

        gc.setStroke(Color.BLACK);

        gc.setLineWidth(2);

        gc.strokeOval(x - size / 2, y - size / 2, size, size);

        gc.setFill(Color.WHITE);

        gc.setFont(Font.font(12));

        gc.setTextAlign(TextAlignment.CENTER);

        String name = worker.getName().length() > 10 ? worker.getName().substring(0, 10) + "..." : worker.getName();

        gc.fillText(name, x, y - 25);
    }
}