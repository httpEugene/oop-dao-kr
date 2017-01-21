package kr_stud.controllers;

import kr_stud.dao.ApplicantDao;
import kr_stud.models.Applicant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/applicants")
public class ApplicantController {

    @Autowired
    private ApplicantDao applicantDao;

    @RequestMapping(value = "/", produces = "application/json")
    @ResponseBody
    public List<Applicant> findAll() {
        return (List<Applicant>) applicantDao.findAll();
    }

    @RequestMapping(value = "/{id}", produces = "application/json")
    @ResponseBody
    public Applicant getApplicant(@PathVariable long id) {
        return applicantDao.findOne(id);
    }

    @RequestMapping(value = "/enrolled/ukrainian", produces = "application/json")
    @ResponseBody
    public List<Applicant> findAllUkrainianApplicantsWhoEnrolled() {
        return (List<Applicant>) applicantDao.findAllUkrainianApplicantsWhoEnrolled();
    }

    @RequestMapping(value = "/enrolled/foreign", produces = "application/json")
    @ResponseBody
    public List<Applicant> findAllForeignApplicantsWhoEnrolled() {
        return (List<Applicant>) applicantDao.findAllForeignApplicantsWhoEnrolled();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Applicant createApplicant(@RequestBody Applicant applicant) {
        applicantDao.save(applicant);

        return applicant;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public String deleteApplicant(@PathVariable long id) {
        applicantDao.delete(id);

        return "{}";
    }
}
