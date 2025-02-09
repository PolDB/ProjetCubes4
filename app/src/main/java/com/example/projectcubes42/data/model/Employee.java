package com.example.projectcubes42.data.model;
//classe pour un employé
public class Employee {


    private Long id;
    private String firstname;
    private String mail;
    private String name;
    private String phone;
    private Long idDepartment;
    private Long idSite;

    // Getters et Setters
    public Employee(String name, String firstname, String phone, String mail, Long idDepartment, Long idSite) {
        this.name = name;
        this.firstname = firstname;
        this.phone = phone;
        this.mail = mail;
        this.idDepartment = idDepartment;
        this.idSite = idSite;
    }

    // Constructeur complet pour updateEmployee
    public Employee(Long id, String name, String firstname, String phone, String mail, Long idDepartment, Long idSite) {
        this.id = id;
        this.name = name;
        this.firstname = firstname;
        this.phone = phone;
        this.mail = mail;
        this.idDepartment = idDepartment;
        this.idSite = idSite;
    }

    public Long getId() {return id;   }

    public void setId(Long id) {this.id = id; }

    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Long idDepartment() { return idDepartment; }
    public void setId_departmentDepartment(Long id_department) { this.idDepartment = id_department; }

    public Long idSite() { return idSite; }
    public void setId_site(Long idSite) { this.idSite = idSite; }
}
