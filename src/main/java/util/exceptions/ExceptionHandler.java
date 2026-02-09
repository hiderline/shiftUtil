package util.exceptions;

import java.io.PrintStream;

public class ExceptionHandler {

    // ANSI коды для цветного вывода в консоль
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String GREEN = "\u001B[32m";
    private static final String BLUE = "\u001B[34m";
    private static final String RESET = "\u001B[0m";

    private static boolean coloredOutput = true;
    private static PrintStream errorStream = System.out;

    public static void handleException(Exception e) {
        handleException(e, null, null);
    }

    /**
     * Обрабатывает исключение без падения программы
     */
    public static void handleException(Exception e, String context) {
        handleException(e, context, null);
    }

    /**
     * Обрабатывает исключение без падения программы с дополнительными данными
     */
    public static void handleException(Exception e, String context, String additionalInfo) {
        String message = buildErrorMessage(e, context, additionalInfo);
        printError(message);
    }

    /**
     * Обрабатывает исключение валидации
     */
    public static void handleValidationException(String context, String input) {
        String message = buildValidationErrorMessage(context, input);
        printWarning(message);
        printInfo("Используйте --help для получения справки");
    }

    /**
     * Выводит сообщение об ошибке валидации параметров
     */
    public static void printParameterError(String error, String recommendation) {
        if (coloredOutput) {
            errorStream.println(RED + "[ОШИБКА ПАРАМЕТРОВ] " + error + RESET);
            if (recommendation != null) {
                System.out.println(BLUE + "[ПОДСКАЗКА] " + recommendation + RESET);
            }
        } else {
            errorStream.println("[ОШИБКА ПАРАМЕТРОВ] " + error);
            if (recommendation != null) {
                System.out.println("[ПОДСКАЗКА] " + recommendation);
            }
        }
    }

    /**
     * Выводит информационное сообщение
     */
    public static void printInfo(String message) {
        if (coloredOutput) {
            System.out.println(BLUE + "[INFO] " + message + RESET);
        } else {
            System.out.println("[INFO] " + message);
        }
    }

    /**
     * Выводит предупреждение
     */
    public static void printWarning(String message) {
        if (coloredOutput) {
            System.out.println(YELLOW + "[WARNING] " + message + RESET);
        } else {
            System.out.println("[WARNING] " + message);
        }
    }

    /**
     * Выводит сообщение об успехе
     */
    public static void printSuccess(String message) {
        if (coloredOutput) {
            System.out.println(GREEN + "[SUCCESS] " + message + RESET);
        } else {
            System.out.println("[SUCCESS] " + message);
        }
    }

    /**
     * Выводит отладочное сообщение
     */
    public static void printDebug(String message) {
        if (coloredOutput) {
            System.out.println(BLUE + "[DEBUG] " + message + RESET);
        } else {
            System.out.println("[DEBUG] " + message);
        }
    }

    /**
     * Выводит ошибку (красный цвет)
     */
    public static void printError(String message) {
        if (coloredOutput) {
            errorStream.println(RED + "[ERROR] " + message + RESET);
        } else {
            errorStream.println("[ERROR] " + message);
        }
    }

    // Приватные вспомогательные методы
    private static String buildErrorMessage(Exception e, String context, String additionalInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append("Ошибка при ");
        sb.append(context);
        sb.append(": ");
        sb.append(e.getMessage());

        if (additionalInfo != null && !additionalInfo.isEmpty()) {
            sb.append("\n  Дополнительная информация: ");
            sb.append(additionalInfo);
        }

        return sb.toString();
    }

    private static String buildValidationErrorMessage(String context, String input) {
        return String.format("Ошибка валидации: %s\n\t\t\t\t\tДетали: '%s'",
                context, input);
    }

    /**
     * Отключает цветной вывод (для перенаправления в файл)
     */
    public static void disableColoredOutput() {
        coloredOutput = false;
    }

    /**
     * Включает цветной вывод
     */
    public static void enableColoredOutput() {
        coloredOutput = true;
    }
}
