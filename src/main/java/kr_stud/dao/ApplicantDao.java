package kr_stud.dao;

import kr_stud.models.Applicant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ApplicantDao extends CrudRepository<Applicant, Long>{

    @Query("SELECT s FROM Applicant s, Mark m WHERE s.id = m.applicant.id AND s.country.name like '%Ukraine%' GROUP BY s.id HAVING SUM(m.mark) >= 520.0")
    public List<Applicant> findAllUkrainianApplicantsWhoEnrolled();

    @Query("SELECT s FROM Applicant s, Mark m WHERE s.id = m.applicant.id AND s.country.name not like '%Ukraine%' GROUP BY s.id HAVING SUM(m.mark) >= 520.0")
    public List<Applicant> findAllForeignApplicantsWhoEnrolled();
}
