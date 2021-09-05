package awt.nhl.dao;

import awt.nhl.Team;
import awt.nhl.Teams;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

public class TeamsDao {
    private final String baseUrl;
    private HttpDao httpDao = new HttpDao();

    public TeamsDao() {
        this("https://statsapi.web.nhl.com");
    }

    TeamsDao(final String baseUrl) {
        this.baseUrl = Objects.requireNonNull(baseUrl);
    }

    public Collection<Team> loadAll() throws IOException {
        final String url = this.baseUrl + "/api/v1/teams";
        return this.httpDao.get(url, Teams.class).getTeams();
    }

    public Team loadByName(final Team.Name teamName) throws IOException {
        if (teamName == null) {
            throw new NullPointerException("teamName cannot be null.");
        }

        final String url = this.baseUrl + "/api/v1/teams/" + teamName.getId();
        final Collection<Team> teams = this.httpDao.get(url, Teams.class).getTeams();

        // should be exactly one
        if (teams == null || teams.size() != 1) {
            throw new IllegalStateException();
        }

        return teams.stream().findFirst().get();
    }
}
