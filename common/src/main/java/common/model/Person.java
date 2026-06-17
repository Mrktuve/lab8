package common.model;

import common.enums.Country;
import common.enums.EyeColor;
import common.enums.HairColor;
import java.io.Serializable;

public class Person implements Serializable {
    private String passportID;
    private EyeColor eyeColor;
    private HairColor hairColor;
    private Country nationality;

    public Person(String passportID, EyeColor eyeColor, HairColor hairColor, Country nationality) {
        this.passportID = passportID;
        this.eyeColor = eyeColor;
        this.hairColor = hairColor;
        this.nationality = nationality;
    }

    public String getPassportID() { return passportID; }
    public EyeColor getEyeColor() { return eyeColor; }
    public HairColor getHairColor() { return hairColor; }
    public Country getNationality() { return nationality; }

    @Override
    public String toString() {
        return "Person{" +
                "passportID='" + passportID + '\'' +
                ", eyeColor=" + eyeColor +
                ", hairColor=" + hairColor +
                ", nationality=" + nationality +
                '}';
    }
}