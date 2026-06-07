package client.gui;

import common.model.*;
import common.enums.*;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.List;

/**
 * Панель для визуализации коллекции работников.
 */
public class CanvasPanel extends Pane {

    private final Canvas canvas;
    private final Localization localization;
    private List<Worker> workers;

    private double scale = 1.0;
    private double offsetX = 0;
    private double offsetY = 0;
    private Point2D lastMousePosition;

    private static final Color COLOR_FIRED = Color.RED;
    private static final Color COLOR_RECOMMENDED = Color.PURPLE;
    private static final Color COLOR_REGULAR = Color.BLUE;
    private static final Color COLOR_PROBATION = Color.ORANGE;
    private static final Color COLOR_DEFAULT = Color.GRAY;

    public CanvasPanel(Localization localization) {
        this.localization = localization;
        this.workers = List.of();

        canvas = new Canvas(800, 600);
        getChildren().add(canvas);

        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());

        setupMouseHandlers();

        widthProperty().addListener((obs, oldVal, newVal) -> redraw());
        heightProperty().addListener((obs, oldVal, newVal) -> redraw());
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers != null ? workers : List.of();
        redraw();
    }

    private void setupMouseHandlers() {
        canvas.setOnMousePressed(this::handleMousePressed);
        canvas.setOnMouseDragged(this::handleMouseDragged);
        canvas.setOnScroll(this::handleScroll);
    }

    private void handleMousePressed(MouseEvent event) {
        lastMousePosition = new Point2D(event.getX(), event.getY());
    }

    private void handleMouseDragged(MouseEvent event) {
        if (lastMousePosition != null) {
            double deltaX = event.getX() - lastMousePosition.getX();
            double deltaY = event.getY() - lastMousePosition.getY();
            offsetX += deltaX;
            offsetY += deltaY;
            lastMousePosition = new Point2D(event.getX(), event.getY());
            redraw();
        }
    }

    private void handleScroll(ScrollEvent event) {
        double zoomFactor = event.getDeltaY() > 0 ? 1.1 : 0.9;
        scale *= zoomFactor;
        scale = Math.max(0.1, Math.min(scale, 5.0));
        redraw();
    }

    public void redraw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        if (workers.isEmpty()) {
            drawEmptyMessage(gc);
            return;
        }

        gc.save();
        gc.translate(offsetX, offsetY);
        gc.scale(scale, scale);

        drawWorkers(gc);

        gc.restore();
    }

    private void drawEmptyMessage(GraphicsContext gc) {
        gc.setFill(Color.GRAY);
        gc.setFont(Font.font(18));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(localization.get("canvas.empty"), canvas.getWidth() / 2, canvas.getHeight() / 2);
    }

    private void drawWorkers(GraphicsContext gc) {
        double centerX = canvas.getWidth() / 2;
        double centerY = canvas.getHeight() / 2;
        double radius = Math.min(canvas.getWidth(), canvas.getHeight()) * 0.35;

        int count = workers.size();
        for (int i = 0; i < count; i++) {
            Worker worker = workers.get(i);

            double angle = (2 * Math.PI * i) / count;
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);

            drawWorker(gc, worker, x, y);
        }
    }

    private void drawWorker(GraphicsContext gc, Worker worker, double x, double y) {
        Color color = getStatusColor(worker.getStatus());

        double circleSize = 50;
        gc.setFill(color);
        gc.fillOval(x - circleSize / 2, y - circleSize / 2, circleSize, circleSize);

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeOval(x - circleSize / 2, y - circleSize / 2, circleSize, circleSize);

        gc.setFill(Color.BLACK);
        gc.setFont(Font.font(11));
        gc.setTextAlign(TextAlignment.CENTER);

        String displayName = worker.getName().length() > 10 ?
                worker.getName().substring(0, 10) + "..." : worker.getName();
        gc.fillText(displayName, x, y - circleSize / 2 - 5);

        if (worker.getSalary() != null) {
            gc.fillText("$" + worker.getSalary().intValue(), x, y + circleSize / 2 + 15);
        }
    }

    private Color getStatusColor(Status status) {
        if (status == null) {
            return COLOR_DEFAULT;
        }

        return switch (status) {
            case FIRED -> COLOR_FIRED;
            case RECOMMENDED_FOR_PROMOTION -> COLOR_RECOMMENDED;
            case REGULAR -> COLOR_REGULAR;
            case PROBATION -> COLOR_PROBATION;
        };
    }
}