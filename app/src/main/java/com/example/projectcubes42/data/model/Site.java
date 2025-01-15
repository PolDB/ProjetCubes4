package com.example.projectcubes42.data.model;

public class Site {
    private Long idSite;
    private String city;

    public Site(Long idSite, String city){
        this.idSite = idSite;
        this.city = city;

    }
    public String getCity() {
        return city;
    }
    public Long getIdSite(){ return  idSite; }


    public void setId_site(Long idSite) {
        this.idSite = idSite;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return city; // Retourne le nom du site pour le Spinner
    }
}
