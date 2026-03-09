package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Recipe {
    private String first;
    private String second;
    private String result;

    @JsonIgnore
    private int level;

    public Recipe() {}

    public Recipe(String first, String second, String result, int level) {
        this.first = first;
        this.second = second;
        this.result = result;
        this.level = level;
    }

    public String getFirst() { return first; }
    public void setFirst(String first) { this.first = first; }

    public String getSecond() { return second; }
    public void setSecond(String second) { this.second = second; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    @Override
    public String toString() {
        return first + " + " + second + " = " + result + " (ур." + level + ")";
    }
}