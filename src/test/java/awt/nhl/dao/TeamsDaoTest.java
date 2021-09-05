package awt.nhl.dao;

import awt.nhl.Team;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@Testcontainers
public class TeamsDaoTest {

    @Container
    public MockServerContainer mockHttpServer =
        new MockServerContainer(DockerImageName.parse("mockserver/mockserver:mockserver-5.11.2"));

    private static final int EXPECTED_NUMBER_OF_NHL_TEAMS = 2;

    @Test
    public void when_valid_return_teams() throws IOException {
        new MockServerClient(this.mockHttpServer.getHost(), this.mockHttpServer.getServerPort())
            .when(request().withMethod("GET").withPath("/api/v1/teams"))
            .respond(response().withBody("""
                {
                  "copyright" : "NHL and the NHL Shield are registered trademarks of the National Hockey League. NHL and NHL team marks are the property of the NHL and its teams. © NHL 2021. All Rights Reserved.",
                  "teams" : [{
                    "id" : 16,
                    "name" : "Chicago Blackhawks",
                    "link" : "/api/v1/teams/16",
                    "venue" : {
                      "id" : 5092,
                      "name" : "United Center",
                      "link" : "/api/v1/venues/5092",
                      "city" : "Chicago",
                      "timeZone" : {
                        "id" : "America/Chicago",
                        "offset" : -5,
                        "tz" : "CDT"
                      }
                    },
                    "abbreviation" : "CHI",
                    "teamName" : "Blackhawks",
                    "locationName" : "Chicago",
                    "firstYearOfPlay" : "1926",
                    "division" : {
                      "id" : 16,
                      "name" : "Central",
                      "nameShort" : "CEN",
                      "link" : "/api/v1/divisions/16",
                      "abbreviation" : "C"
                    },
                    "conference" : {
                      "id" : 5,
                      "name" : "Western",
                      "link" : "/api/v1/conferences/5"
                    },
                    "franchise" : {
                      "franchiseId" : 11,
                      "teamName" : "Blackhawks",
                      "link" : "/api/v1/franchises/11"
                    },
                    "shortName" : "Chicago",
                    "officialSiteUrl" : "http://www.chicagoblackhawks.com/",
                    "franchiseId" : 11,
                    "active" : true
                  }, {
                    "id" : 17,
                    "name" : "Detroit Red Wings",
                    "link" : "/api/v1/teams/17",
                    "venue" : {
                      "id" : 5145,
                      "name" : "Little Caesars Arena",
                      "link" : "/api/v1/venues/5145",
                      "city" : "Detroit",
                      "timeZone" : {
                        "id" : "America/Detroit",
                        "offset" : -4,
                        "tz" : "EDT"
                      }
                    },
                    "abbreviation" : "DET",
                    "teamName" : "Red Wings",
                    "locationName" : "Detroit",
                    "firstYearOfPlay" : "1926",
                    "division" : {
                      "id" : 17,
                      "name" : "Atlantic",
                      "nameShort" : "ATL",
                      "link" : "/api/v1/divisions/17",
                      "abbreviation" : "A"
                    },
                    "conference" : {
                      "id" : 6,
                      "name" : "Eastern",
                      "link" : "/api/v1/conferences/6"
                    },
                    "franchise" : {
                      "franchiseId" : 12,
                      "teamName" : "Red Wings",
                      "link" : "/api/v1/franchises/12"
                    },
                    "shortName" : "Detroit",
                    "officialSiteUrl" : "http://www.detroitredwings.com/",
                    "franchiseId" : 12,
                    "active" : true
                  }]
                }"""));

        final TeamsDao dao = new TeamsDao(this.mockHttpServer.getEndpoint());
        final Collection<Team> teams = dao.loadAll();
        assertNotNull(teams);
        assertEquals(EXPECTED_NUMBER_OF_NHL_TEAMS, teams.size());
    }

    @Test
    public void when_server_down_throw_exception() {
        // MUST CALL #getEndpoint BEFORE #stop.
        final TeamsDao dao = new TeamsDao(this.mockHttpServer.getEndpoint());
        this.mockHttpServer.stop();
        assertThrows(IOException.class, dao::loadAll);
    }

    @Test
    public void when_team_found_return_value() throws IOException {
        new MockServerClient(this.mockHttpServer.getHost(), this.mockHttpServer.getServerPort())
            .when(request().withMethod("GET").withPath("/api/v1/teams/" + Team.Name.DETROIT.getId()))
            .respond(response().withBody("""
                {
                  "copyright" : "NHL and the NHL Shield are registered trademarks of the National Hockey League. NHL and NHL team marks are the property of the NHL and its teams. © NHL 2021. All Rights Reserved.",
                  "teams" : [{
                    "id" : 17,
                    "name" : "Detroit Red Wings",
                    "link" : "/api/v1/teams/17",
                    "venue" : {
                      "id" : 5145,
                      "name" : "Little Caesars Arena",
                      "link" : "/api/v1/venues/5145",
                      "city" : "Detroit",
                      "timeZone" : {
                        "id" : "America/Detroit",
                        "offset" : -4,
                        "tz" : "EDT"
                      }
                    },
                    "abbreviation" : "DET",
                    "teamName" : "Red Wings",
                    "locationName" : "Detroit",
                    "firstYearOfPlay" : "1926",
                    "division" : {
                      "id" : 17,
                      "name" : "Atlantic",
                      "nameShort" : "ATL",
                      "link" : "/api/v1/divisions/17",
                      "abbreviation" : "A"
                    },
                    "conference" : {
                      "id" : 6,
                      "name" : "Eastern",
                      "link" : "/api/v1/conferences/6"
                    },
                    "franchise" : {
                      "franchiseId" : 12,
                      "teamName" : "Red Wings",
                      "link" : "/api/v1/franchises/12"
                    },
                    "shortName" : "Detroit",
                    "officialSiteUrl" : "http://www.detroitredwings.com/",
                    "franchiseId" : 12,
                    "active" : true
                  }]
                }"""));
        final TeamsDao dao = new TeamsDao(this.mockHttpServer.getEndpoint());
        final Team team = dao.loadByName(Team.Name.DETROIT);
        assertNotNull(team);
        assertEquals("DET", team.getAbbreviation());
        assertEquals("Detroit Red Wings", team.getName());
    }

    @Test
    public void when_team_not_found_throw_exception() {
        final Team.Name name = Team.Name.DETROIT;

        new MockServerClient(this.mockHttpServer.getHost(), this.mockHttpServer.getServerPort())
            .when(request().withMethod("GET").withPath("/api/v1/teams/" + name.getId()))
            .respond(response().withBody("""
                {
                  "copyright" : "NHL and the NHL Shield are registered trademarks of the National Hockey League. NHL and NHL team marks are the property of the NHL and its teams. © NHL 2021. All Rights Reserved.",
                  "teams" : []
                }"""));
        final TeamsDao dao = new TeamsDao(this.mockHttpServer.getEndpoint());
        assertThrows(IllegalStateException.class, () -> dao.loadByName(name));
    }

    @Test
    public void when_team_null_throw_exception() {
        final NullPointerException expected = assertThrows(NullPointerException.class,
            () -> new TeamsDao().loadByName(null));
        assertEquals("teamName cannot be null.", expected.getMessage());
    }
}
