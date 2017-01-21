package kr_stud.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "countries")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "country")
    @JsonBackReference
    private List<Applicant> applicant;

    public Country() {
    }

    public Country(long id) {
        this.id = id;
    }

    public Country(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long value) {
        this.id = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public List<Applicant> getApplicant() {
        return applicant;
    }

    public void setApplicant(List<Applicant> applicant) {
        this.applicant = applicant;
    }
}
