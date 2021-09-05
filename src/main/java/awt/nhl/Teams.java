package awt.nhl;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

import java.util.Collection;

public class Teams {
    private final Collection<Team> teams;

    @JsonbCreator
    public Teams(@JsonbProperty("teams") final Collection<Team> teams) {
        this.teams = teams;
    }

    public Collection<Team> getTeams() {
        return this.teams;
    }
}
