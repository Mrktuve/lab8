package client.gui;

import common.model.*;
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

import java.time.LocalDate;
import java.util.List;

/**
 * Панель для визуализации коллекции работников на Canvas.
 */
public class CanvasPanel extends Pane {

    private final Canvas canvas;
    private final Localization localization;
    private List<Worker> workers;

    // Параметры масштабирования и перемещения
    private double scale = 1.0;
    private double offsetX = 0;
    private double offsetY = 0;
    private Point2D lastMousePosition;

    // Цвета для статусов
    private static final Color COLOR_HIRED = Color.GREEN;
    private static final Color COLOR_FIRED = Color.RED;
    private static final Color COLOR_REGULAR = Color.BLUE;
    private static final Color COLOR_PROBATION = Color.ORANGE;
    private static final Color COLOR_DEFAULT = Color.GRAY;

    public CanvasPanel(Localization localization) {
        this.localization = localization;
        this.workers = List.of();

        canvas = new Canvas(800, 600);
        getChildren().add(canvas);

        // Привязываем размер canvas к размеру панели
        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());

        // Обработчики событий мыши
        setupMouseHandlers();

        // Перерисовка при изменении размера
        widthProperty().addListener((obs, oldVal, newVal) -> redraw());
        heightProperty().addListener((obs, oldVal, newVal) -> redraw());
    }

    /**
     * Устанавливает список работников для отображения.
     */
    public void setWorkers(List<Worker> workers) {
        this.workers = workers != null ? workers : List.of();
        redraw();
    }

    /**
     * Настраивает обработчики событий мыши для масштабирования и перемещения.
     */
    private void setupMouseHandlers() {
        // Перемещение canvas
        canvas.setOnMousePressed(this::handleMousePressed);
        canvas.setOnMouseDragged(this::handleMouseDragged);

        // Масштабирование колесом мыши
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
        scale = Math.max(0.1, Math.min(scale, 5.0)); // Ограничиваем масштаб
        redraw();
    }

    /**
     * Перерисовывает canvas.
     */
    public void redraw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Очищаем canvas
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        if (workers.isEmpty()) {
            drawEmptyMessage(gc);
            return;
        }

        // Применяем трансформации
        gc.save();
        gc.translate(offsetX, offsetY);
        gc.scale(scale, scale);

        // Рисуем работников
        drawWorkers(gc);

        gc.restore();
    }

    /**
     * Рисует сообщение о пустой коллекции.
     */
    private void drawEmptyMessage(GraphicsContext gc) {
        gc.setFill(Color.GRAY);
        gc.setFont(Font.font(18));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(localization.get("canvas.empty"), canvas.getWidth() / 2, canvas.getHeight() / 2);
    }

    /**
     * Рисует всех работников на canvas.
     */
    private void drawWorkers(GraphicsContext gc) {
        double centerX = canvas.getWidth() / 2;
        double centerY = canvas.getHeight() / 2;
        double radius = 200;

        int count = workers.size();
        for (int i = 0; i < count; i++) {
            Worker worker = workers.get(i);

            // Распределяем работников по кругу
            double angle = (2 * Math.PI * i) / count;
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);

            drawWorker(gc, worker, x, y);
        }
    }

    /**
     * Рисует одного работника.
     */
    private void drawWorker(GraphicsContext gc, Worker worker, double x, double y) {
        // Определяем цвет по статусу
        Color color = getStatusColor(worker);

        // Рисуем круг
        double circleSize = 40;
        gc.setFill(color);
        gc.fillOval(x - circleSize / 2, y - circleSize / 2, circleSize, circleSize);

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeOval(x - circleSize / 2, y - circleSize / 2, circleSize, circleSize);

        // Рисуем ID
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font(12));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("ID: " + worker.getId(), x, y - circleSize / 2 - 5);

        // Рисуем имя
        gc.fillText(worker.getName(), x, y + circleSize / 2 + 15);

        // Рисуем зарплату
        if (worker.getSalary() != null) {
            gc.fillText("$" + worker.getSalary(), x, y + circleSize / 2 + 30);
        }
    }

    /**
     * Возвращает цвет для статуса работника.
     */
    private Color getStatusColor(Worker worker) {
        if (worker.getStatus() == null) {
            return COLOR_DEFAULT;
        }

        return switch (worker.getStatus().name().toUpperCase()) {
            case "HIRED" -> COLOR_HIRED;
            case "FIRED" -> COLOR_FIRED;
            case "REGULAR" -> COLOR_REGULAR;
            case "PROBATION" -> COLOR_PROBATION;
            default -> COLOR_DEFAULT;
        };
    }
}