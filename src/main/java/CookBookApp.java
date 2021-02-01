import java.util.function.Function;

public class CookBookApp {

    //klasa uruchomieniowa
    private static final RecipeDao DAO = new RecipeDao();

    public static void main(String[] args) {
        create();
        read();
        update();
        delete();
        DAO.close();
    }

    private static void create() {
        Recipe recipe = new Recipe("Kurczak z frytkami", """
                Ziemniaki pokrój w słupki o grubości ok 1cm, możesz je zamarynować w oleju z ziołami i papryką,
                Pierś z kurczaka oczyść z błonek, pokrój w plastry i delikatnie rozbij. Zamarynuj w ulubionych przyprawach.
                Rozgrzej piekarnik do temperatury 180 stopni, najpierw umieść na blaszce frytki, a po 20 minutach dorzuć
                kurczaka i piecz jeszcze 20 minut (łącznie 40 minut). Podawaj z ulubionym sosem lub keczupem.
                """, "ziemniaki, pierś z kurczaka, zioła prowansalskie, oliwa", 30);
        System.out.println("Zapisuję przepis na kurczaka z frytkami");
        DAO.save(recipe);
        System.out.println("Przepis zapisany, jego id to: " + recipe.getId());
    }

    private static void read() {
        DAO.findByTitle("Jajecznica").ifPresentOrElse(
                recipe -> System.out.println("Szukany przepis:\n" + recipe),
                () -> System.out.println("Brak przepisu o takiej nazwie")
        );
    }

    private static void update() {
        Function<Recipe, Recipe> updateRecipePrepTime = recipe -> {
            recipe.setPrepTime(60);
            return recipe;
        };
        DAO.findByTitle("Kurczak z frytkami")
                .map(updateRecipePrepTime)
                .map(DAO::update)
                .filter(b -> b)
                .ifPresent(updated -> System.out.println("Czas przygotowania został zaktualizowany"));
    }

    private static void delete() {
        System.out.println("Usuwam przepis na rosół");
        DAO.findByTitle("Rosół")
                .map(Recipe::getId)
                .map(DAO::delete)
                .ifPresentOrElse(removed -> System.out.println("Przepis został usunięty"),
                        () -> System.out.println("W bazie nie ma przepisu do usunięcia"));
    }

    //W klasie zdefiniowane są cztery metody:
    //
    //    create() - tworzymy w niej nowy obiekt Recipe i zapisujemy go w bazie danych,
    //    read() - wyszukujemy przepis na Jajecznicę i jeżeli zostanie on odnaleziony, to wyświetlamy o nim informacje.
    //    Jeżeli go nie odnajdziemy, to wyświetlamy komunikat o braku takiego przepisu. Metoda findByTitle() zwraca obiekt
    //    typu Optional, więc można to zapisać w wygodny sposób, bez wykorzystania instrukcji if.
    //    update() - zauważyliśmy, że w przepisie na kurczaka z frytkami popełniliśmy błąd i czas przygotowania ustawiliśmy
    //    na 30, a nie 60 minut. W tej metodzie to poprawiamy. Najpierw zdefiniowana jest funkcja zmieniająca czas
    //    przygotowania na 60 minut. Dalej wyszukujemy przepis na podstawie nazwy, jeżeli zostanie on odnaleziony to
    //    poprawiamy własność prepTime. Na końcu wywołujemy metodę ifPresent(), z referencją do metody update(),
    //    co spowoduje zaktualizowanie rekordu w bazie.
    //    delete() - w metodzie usuwamy przepis na rosół. Robimy to w taki sposób, że najpierw wyszukujemy przepis,
    //    jeżeli zostanie on odnaleziony to wykonujemy mapowanie z typu Recipe na Integer, ponieważ do usunięcia obiektu
    //    potrzebny jest nam jedynie klucz w postaci liczby całkowitej. W kolejnym kroku usuwamy obiekt, wywołując metodę
    //    delete() z klasy DAO. W zależności od tego, czy znaleziono taki przepis, czy nie, to wyświetlamy użytkownikowi
    //    odpowiedni komunikat.
}
