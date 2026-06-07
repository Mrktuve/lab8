package client.gui;

import common.model.*;
import common.enums.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Утилитарный класс для работы с коллекцией работников на стороне клиента.
 */
public class CollectionUtils {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    /**
     * Форматирует объект Worker в читаемую строку для вывода в TextArea или консоль.
     */
    public static String formatWorker(Worker worker, Localization loc) {
        if (worker == null) return "";

        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(worker.getId()).append("\n");
        sb.append(loc.get("worker.name")).append(": ").append(worker.getName()).append("\n");

        Coordinates coords = worker.getCoordinates();
        if (coords != null) {
            sb.append(loc.get("worker.coordinates")).append(": (")
                    .append(coords.getX()).append(", ").append(coords.getY()).append(")\n");
        }

        sb.append(loc.get("worker.creationDate")).append(": ")
                .append(worker.getCreationDate() != null ? worker.getCreationDate().format(DATE_FORMATTER) : "N/A").append("\n");

        sb.append(loc.get("worker.salary")).append(": ")
                .append(worker.getSalary() != null ? worker.getSalary() : "N/A").append("\n");

        sb.append(loc.get("worker.startDate")).append(": ")
                .append(worker.getStartDate() != null ? worker.getStartDate().format(DATE_FORMATTER) : "N/A").append("\n");

        sb.append(loc.get("worker.endDate")).append(": ")
                .append(worker.getEndDate() != null ? worker.getEndDate().format(DATE_FORMATTER) : "N/A").append("\n");

        sb.append(loc.get("worker.status")).append(": ")
                .append(worker.getStatus() != null ? formatStatus(worker.getStatus(), loc) : "N/A").append("\n");

        Person person = worker.getPerson();
        if (person != null) {
            sb.append(loc.get("worker.person")).append(":\n");
            sb.append("  - ").append(loc.get("person.height")).append(": ").append(person.getHeight()).append("\n");
            sb.append("  - ").append(loc.get("person.eyeColor")).append(": ")
                    .append(person.getEyeColor() != null ? person.getEyeColor().name() : "N/A").append("\n");
            sb.append("  - ").append(loc.get("person.hairColor")).append(": ")
                    .append(person.getHairColor() != null ? person.getHairColor().name() : "N/A").append("\n");
            sb.append("  - ").append(loc.get("person.nationality")).append(": ")
                    .append(person.getNationality() != null ? person.getNationality().name() : "N/A").append("\n");
        }

        sb.append(loc.get("worker.owner")).append(": ").append(worker.getOwnerLogin() != null ? worker.getOwnerLogin() : "N/A").append("\n");
        sb.append("--------------------------------------------------\n");

        return sb.toString();
    }

    /**
     * Форматирует статус работника с учетом локализации.
     */
    private static String formatStatus(Status status, Localization loc) {
        return switch (status) {
            case HIRED -> loc.get("status.hired");
            case FIRED -> loc.get("status.fired");
            case REGULAR -> loc.get("status.regular");
            case PROBATION -> loc.get("status.probation");
            default -> status.name();
        };
    }

    /**
     * Сортирует коллекцию по убыванию имени (для команды print_descending, если сервер возвращает всё).
     */
    public static List<Worker> sortDescendingByName(List<Worker> workers) {
        if (workers == null) return List.of();
        return workers.stream()
                .sorted(Comparator.comparing(Worker::getName).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Фильтрует коллекцию, оставляя только тех, чье имя начинается с заданной подстроки.
     */
    public static List<Worker> filterStartsWithName(List<Worker> workers, String prefix) {
        if (workers == null || prefix == null) return List.of();
        String lowerPrefix = prefix.toLowerCase();
        return workers.stream()
                .filter(w -> w.getName() != null && w.getName().toLowerCase().startsWith(lowerPrefix))
                .collect(Collectors.toList());
    }

    /**
     * Находит работника по ID в списке.
     */
    public static Worker findById(List<Worker> workers, long id) {
        if (workers == null) return null;
        return workers.stream()
                .filter(w -> w.getId() == id)
                .findFirst()
                .orElse(null);
    }
}