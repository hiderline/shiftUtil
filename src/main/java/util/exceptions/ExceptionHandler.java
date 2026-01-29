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
    private static PrintStream errorStream = System.err;

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

    public static void printError(String message) {
        if (coloredOutput) {
            errorStream.println(RED + "[ERROR] " + message + RESET);
        } else {
            errorStream.println("[ERROR] " + message);
        }
    }

    /*public static void setErrorStream(PrintStream stream) {
        errorStream = stream;
    }*/
}
