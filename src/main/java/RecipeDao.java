import java.sql.*;
import java.util.Optional;

public class RecipeDao {

    //https://javastart.pl/kurs/technologie-webowe/web-bazy-java/lekcja/web-sql-dao

    private final Connection connection;

    public RecipeDao() {
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/cookbook?serverTimezone=UTC", "root", "admin");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //metoda do zapisu danych
    void save(Recipe recipe) {
        final String sql = String.format(
                "INSERT INTO recipe (title, preptime, ingredients, description) VALUES ('%s', %d, '%s', '%s')",
                recipe.getTitle(), recipe.getPrepTime(), recipe.getIngredients(), recipe.getDescription());
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                recipe.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Metoda save() przyjmuje parametr typu Recipe. Na początku definiujemy szablon zapytania w języku SQL, które
        // chcemy wykonać. Wykorzystuję metodę String.format(), która pozwoli uniknąć nieczytelnej konkatenacji,
    }

    //wyszukanie na podstawie tytułu
    Optional<Recipe> findByTitle(String searchTitle) {
        final String sql = "SELECT id, title, description, ingredients, preptime FROM recipe WHERE title = '" + searchTitle + "'";
        // można też tak String sql = "SELECT id, title, description, ingredients, preptime FROM recipe WHERE title LIKE '%" + searchTitle + "%'";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String ingredients = resultSet.getString("ingredients");
                String description = resultSet.getString("description");
                int prepTime = resultSet.getInt("preptime");
                return Optional.of(new Recipe(id, title, description, ingredients, prepTime));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    //My oczekujemy pojedynczego wyniku, więc tworzymy obiekt Statement, wykonujemy zapytanie i jeżeli ResultSet przechowuje
    // jakiś wynik, to tworzymy na jego podstawie obiekt typu Recipe, który opakowujemy w Optionala i zwracamy z metody.
    // Jeżeli przepis o wskazanej nazwie nie zostanie odnaleziony, to na końcu zwracamy pustego Optionala.

    //usówanie
    boolean delete(int id) {
        final String sql = "DELETE FROM recipe WHERE id = " + id;
        try (Statement statement = connection.createStatement()) {
            int updatedRows = statement.executeUpdate(sql);
            return updatedRows != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // aktualizacja
    boolean update(Recipe recipe) {
        final String sql = String.format("""
                        UPDATE
                            recipe
                        SET
                            title = '%s',
                            description = '%s',
                            ingredients = '%s',
                            preptime = %d
                        WHERE
                            id = %d
                        """,
                recipe.getTitle(),
                recipe.getDescription(),
                recipe.getIngredients(),
                recipe.getPrepTime(),
                recipe.getId());
        try (Statement statement = connection.createStatement()) {
            int updatedRows = statement.executeUpdate(sql);
            return updatedRows != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}

