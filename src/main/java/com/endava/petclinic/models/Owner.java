package com.endava.petclinic.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;
//in momentul in care gaseste ceva ce nu poate mapa ignora
@JsonIgnoreProperties(ignoreUnknown = true)
public class Owner {
    //atributele din HaspMap

    private Integer id;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String telephone;

    //constructor fara parametrii

    public Owner() {
    }

    //constructor cu toti parametrii

    public Owner(Integer id, String firstName, String lastName, String address, String city, String telephone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.telephone = telephone;
    }

    //constructor cu toti parametrii mai putin id

    public Owner(String firstName, String lastName, String address, String city, String telephone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.telephone = telephone;
    }

    //Getters si setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    //equals si hashCode, equals fara id
    //deoarece id este unic, fiind autoincrementat
    //la comparare testul va pica din cauza id-ului care este unic
    //de aceea nu il punem
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Owner)) return false;
        Owner owner = (Owner) o;
        return Objects.equals(firstName, owner.firstName) && Objects.equals(lastName, owner.lastName) && Objects.equals(address, owner.address) && Objects.equals(city, owner.city) && Objects.equals(telephone, owner.telephone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, address, city, telephone);
    }

    //pentru a intelege ce se afiseaza folosim toString
    @Override
    public String toString() {
        return "Owner{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }
}
