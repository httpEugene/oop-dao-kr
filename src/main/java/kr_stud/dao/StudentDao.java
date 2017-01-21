package kr_stud.dao;

import kr_stud.models.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface StudentDao extends CrudRepository<Student, Long>{

    @Query("SELECT s FROM Student s, Mark m WHERE s.id = m.student.id AND s.country.name like '%Ukraine%' GROUP BY s.id HAVING SUM(m.mark) >= 520.0")
    public List<Student> findAllApplicantsWhoEnrolled();

    @Query("SELECT s FROM Student s, Mark m WHERE s.id = m.student.id AND s.country.name not like '%Ukraine%' GROUP BY s.id HAVING SUM(m.mark) >= 520.0")
    public List<Student> findGoodForeign();
}
