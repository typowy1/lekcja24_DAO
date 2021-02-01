public class Recipe {

    private Integer id;
    private String title;
    private String description;
    private String ingredients;
    private int prepTime;

    public Recipe(String title, String description, String ingredients, int prepTime) {
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
        this.prepTime = prepTime;
    }

    public Recipe(Integer id, String title, String description, String ingredients, int prepTime) {
        this(title, description, ingredients, prepTime);
        this.id = id;
    }

    //Stworzyłem dwa konstruktory, z których jeden pozwala ustawić id, a drugi nie. Wynika to z tego, że kolumna id w
    // bazie danych jest ustawiona jako AUTO_INCREMENT. Jeżeli będziemy więc zapisywali do bazy nowe przepisy, to nie
    // będziemy ustawiali tego pola, bo będzie ono generowane po stronie bazy danych, ale przy odczycie danych,
    // będziemy już znali jego wartość. Do pola id używamy typu opakowującego Integer, zamiast int, ponieważ możemy
    // dzięki temu reprezentować w czytelny sposób sytuację, w której klucz jest ustawiony lub nie (null).

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public int getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }
}
