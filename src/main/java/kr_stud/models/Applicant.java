package kr_stud.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "applicants")
public class Applicant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Column(name = "middle_name")
    private String middleName;

    @NotNull
    @Column(name = "phone")
    private String phone;

    @NotNull
    @Column(name = "gender")
    private int gender;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @NotNull
    @Column(name = "identity", unique = true)
    private String identity;

    @OneToMany(mappedBy = "applicant")
    private List<Mark> marks;

    public Applicant() {
    }

    public Applicant(long id) {
        this.id = id;
    }

    public Applicant(String firstName,
                   String lastName,
                   String middleName,
                   String phone,
                   int gender,
                   Country country,
                   String identity,
                   List<Mark> marks) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.phone = phone;
        this.gender = gender;
        this.country = country;
        this.identity = identity;
        this.marks = marks;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Country getCountry() {
        return country;
    }

    @JsonProperty(value = "country_id")
    public long getCountryId() {
        return country.getId();
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public List<Mark> getMarks() {
        return marks;
    }

    public void setMarks(List<Mark> marks) {
        this.marks = marks;
    }

    @JsonProperty(value = "score")
    public double getScore() {
        if(marks != null) {
            double sum = marks
                    .stream()
                    .mapToDouble(Mark::getMark)
                    .sum();

            return sum;
        }

        return 0;
    }
}
