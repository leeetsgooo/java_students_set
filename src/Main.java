import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String DB_USERNAME = "alexey";
    private static final String DB_PASSWORD = "111";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/javatestapp1";

    public static class StudentSearch {
        private Connection connection;

        public StudentSearch(Connection connection) {
            this.connection = connection;
        }

        public void showAllStudents() {
            try {
                Statement statement = connection.createStatement();
                String SQL_SELECT_TASKS = "SELECT id, name, age FROM students";
                ResultSet result = statement.executeQuery(SQL_SELECT_TASKS);
                while (result.next()) {
                    System.out.println("ID: " + result.getInt("id"));
                    System.out.println("Имя: " + result.getString("name"));
                    System.out.println("Возраст: " + result.getInt("age"));
                    System.out.println("");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void showStudents35AndOlder() {
            try {
                Statement statement = connection.createStatement();
                String SQL_SELECT_TASKS = "SELECT id, name, age FROM students WHERE age > 35";
                ResultSet result = statement.executeQuery(SQL_SELECT_TASKS);
                while (result.next()) {
                    System.out.println("ID: " + result.getInt("id"));
                    System.out.println("Имя: " + result.getString("name"));
                    System.out.println("Возраст: " + result.getInt("age"));
                    System.out.println("");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void addNewStudent(String имя, int возраст) {
            try {

                String SQL_INSERT_STUDENT = "INSERT INTO students (name, age) VALUES (?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_STUDENT);

                // Устанавливаем значения параметров
                preparedStatement.setString(1, имя);
                preparedStatement.setInt(2, возраст);

                // запрос на вставку
                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Студент успешно добавлен в базу данных.");
                } else {
                    System.out.println("Ошибка при добавлении студента в базу данных.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void deleteStudent(int studentId) {
            try {
                String SQL_DELETE_STUDENT = "DELETE FROM students WHERE id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_STUDENT);

                // Устанавливаем значение параметра
                preparedStatement.setInt(1, studentId);

                //запрос на удаление
                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Студент успешно удален из базы данных.");
                } else {
                    System.out.println("Студент с указанным ID не найден.");
                }
            } catch (SQLException e) {
                System.out.println("Ошибка при удалении студента из базы данных.");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        StudentSearch studentSearch = new StudentSearch(connection);

        while (true) {
            System.out.println("1: Показать всех студентов");
            System.out.println("2: Добавить нового студента");
            System.out.println("3: Показать студентов, которым больше 35 лет");
            System.out.println("4: Удалить студента");
            System.out.println("5: Выход");
            int command = scanner.nextInt();

            switch (command) {
                case 1:
                    studentSearch.showAllStudents();
                    break;
                case 2:
                    System.out.println("Введите имя нового студента:");
                    String newName = scanner.next();
                    System.out.println("Введите возраст нового студента:");
                    int newAge = scanner.nextInt();
                    studentSearch.addNewStudent(newName, newAge);
                    break;
                case 3:
                    studentSearch.showStudents35AndOlder();
                    break;
                case 4:
                    System.out.println("Введите ID студента для удаления (или введите 0 для возврата в меню):");
                    int studentIdToDelete = scanner.nextInt();
                    if (studentIdToDelete == 0) {
                        continue; // Вернуться в меню
                    }
                    studentSearch.deleteStudent(studentIdToDelete);
                    break;
                case 5:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Неверная команда. Пожалуйста, введите корректную команду.");

            }
        }
    }
}
