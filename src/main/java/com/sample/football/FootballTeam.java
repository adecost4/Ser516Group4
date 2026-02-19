package com.sample.football;

// sample class for testing - private/public fields, methods, and constructors
public class FootballTeam {
    public String name;
    public String location;
    private int wins;
    private int losses;

    public FootballTeam(String name, String location) {
        this.name = name;
        this.location = location;
        this.wins = 0;
        this.losses = 0;
    }

    public FootballTeam() {
        this.name = "Unknown Team";
        this.location = "Unknown Location";
        this.wins = 0;
        this.losses = 0;
    }

    public void addWin() {
        this.wins++;
    }

    public void addLoss() {
        this.losses++;
    }

    public void resetRecord() {
        this.wins = 0;
        this.losses = 0;
    }

    public int getWins() {
        return this.wins;
    }

    public int getLosses() {
        return this.losses;
    }

    public void randomizeTeam() {
        this.name = createRandomTeamName();
        this.location = createRandomLocation();
        int[] records = createRandomRecord();
        this.wins = records[0];
        this.losses = records[1];
    }

    public static String getRandomTeamName() {
        return new FootballTeam().createRandomTeamName();
    }

    private String createRandomTeamName() {
        String[] adjectives = {"Fast", "Furious", "Mighty", "Brave", "Cunning"};
        String[] nouns = {"Lions", "Tigers", "Bears", "Wolves", "Eagles"};
        int adjIndex = (int) (Math.random() * adjectives.length);
        int nounIndex = (int) (Math.random() * nouns.length);
        return adjectives[adjIndex] + " " + nouns[nounIndex];
    }

    private String createRandomLocation() {
        String[] cities = {"New York", "Los Angeles", "Chicago", "Houston", "Phoenix"};
        int cityIndex = (int) (Math.random() * cities.length);
        return cities[cityIndex];
    }

    private int[] createRandomRecord() {
        int wins = (int) (Math.random() * 16);
        int losses = 16 - wins;
        return new int[]{wins, losses};
    }
}
