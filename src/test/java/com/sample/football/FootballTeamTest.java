package com.sample.football;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FootballTeamTest {

    @Test
    void parameterizedConstructor_initializesCorrectly() {
        FootballTeam team = new FootballTeam("Eagles", "Phoenix");

        assertEquals("Eagles", team.name);
        assertEquals("Phoenix", team.location);
        assertEquals(0, team.getWins());
        assertEquals(0, team.getLosses());
    }

    @Test
    void defaultConstructor_setsDefaults() {
        FootballTeam team = new FootballTeam();

        assertEquals("Unknown Team", team.name);
        assertEquals("Unknown Location", team.location);
        assertEquals(0, team.getWins());
        assertEquals(0, team.getLosses());
    }

    @Test
    void addWin_incrementsWins() {
        FootballTeam team = new FootballTeam();
        team.addWin();
        team.addWin();

        assertEquals(2, team.getWins());
    }

    @Test
    void addLoss_incrementsLosses() {
        FootballTeam team = new FootballTeam();
        team.addLoss();

        assertEquals(1, team.getLosses());
    }

    @Test
    void resetRecord_resetsWinsAndLosses() {
        FootballTeam team = new FootballTeam();
        team.addWin();
        team.addLoss();

        team.resetRecord();

        assertEquals(0, team.getWins());
        assertEquals(0, team.getLosses());
    }

    @Test
    void randomizeTeam_setsValidData() {
        FootballTeam team = new FootballTeam();

        team.randomizeTeam();

        assertNotNull(team.name);
        assertNotNull(team.location);

        assertTrue(team.getWins() >= 0);
        assertTrue(team.getWins() <= 16);

        assertTrue(team.getLosses() >= 0);
        assertTrue(team.getLosses() <= 16);

        assertEquals(16, team.getWins() + team.getLosses());
    }

    @Test
    void getRandomTeamName_returnsNonNullString() {
        String name = FootballTeam.getRandomTeamName();

        assertNotNull(name);
        assertTrue(name.contains(" "));
    }
}