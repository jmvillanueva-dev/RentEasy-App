package models;

public class Property {
    private int id;
    private int ownerId;
    private String title;
    private String city;
    private String country;
    private double price;
    private String propertyType;
    private int rooms;
    private int maxPeople;
    private String address;

    // Constructor para registrar una nueva propiedad (sin ID)
    public Property(int ownerId, String title, String city, String country, double price, String propertyType, int rooms, int maxPeople, String address) {
        this.ownerId = ownerId;
        this.title = title;
        this.city = city;
        this.country = country;
        this.price = price;
        this.propertyType = propertyType;
        this.rooms = rooms;
        this.maxPeople = maxPeople;
        this.address = address;
    }

    // Constructor para propiedades recuperadas de la base de datos (con ID)
    public Property(int id, int ownerId, String title, String city, String country, double price, String propertyType, int rooms, int maxPeople, String address) {
        this.id = id;
        this.ownerId = ownerId;
        this.title = title;
        this.city = city;
        this.country = country;
        this.price = price;
        this.propertyType = propertyType;
        this.rooms = rooms;
        this.maxPeople = maxPeople;
        this.address = address;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOwnerId() { return ownerId; }
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getPropertyType() { return propertyType; }
    public void setPropertyType(String propertyType) { this.propertyType = propertyType; }

    public int getRooms() { return rooms; }
    public void setRooms(int rooms) { this.rooms = rooms; }

    public int getMaxPeople() { return maxPeople; }
    public void setMaxPeople(int maxPeople) { this.maxPeople = maxPeople; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}