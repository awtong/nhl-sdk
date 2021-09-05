package awt.nhl;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

public class Team {
    public enum Name {
        DETROIT(17);

        private final int id;

        Name(final int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }
    }

    private final String name;
    private final String abbreviation;

    @JsonbCreator
    public Team(@JsonbProperty("name") final String name,
                @JsonbProperty("abbreviation") final String abbreviation) {
        this.name = name;
        this.abbreviation = abbreviation;
    }

    public String getName() {
        return name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }
}
