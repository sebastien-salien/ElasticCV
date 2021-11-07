package com.daar.elasticCV.controller;

import com.daar.elasticCV.UtilFiles;
import com.daar.elasticCV.model.CV;
import com.daar.elasticCV.service.CVSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:8100")
@RequestMapping("/api/cv")
public class CVController {
    private static final Logger LOG = LogManager.getLogger(CVController.class.getName());

    @Autowired
    private final CVSearchService searchService;

    public CVController(CVSearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping
    public CV insertCV(@RequestBody CV cv) {
        return searchService.createCVIndex(cv);
    }

    @RequestMapping(path = "/upload", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public CV insertCVFile(@RequestPart("file") MultipartFile cvFile, HttpServletResponse response) {
        String id = UUID.randomUUID().toString();
        File file;
        try {
            file = UtilFiles.convertMultipartFromFile(cvFile, id);
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            LOG.error("Exception " + e);
            return null;
        }
        if(file == null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            LOG.error("Error : Wrong file");
            return null;
        }
        else{
            ArrayList<String> arrayListFile;
            try {
                arrayListFile = UtilFiles.fileToArrayList(file);
            } catch (IOException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                LOG.error("Exception " + e);
                return null;
            }
            CV cv = new CV();
            cv.setId(id);
            cv.setTitle(arrayListFile.remove(0));
            String[] skills = new String[arrayListFile.size()];
            skills = arrayListFile.toArray(skills);
            cv.setSkill(skills);
            LOG.log(Level.INFO, "Upload file" + cv.getId() + "with title " + cv.getTitle());
            return searchService.createCVIndex(cv);
        }
    }

    @GetMapping
    public List<CV> getAll(){
        LOG.log(Level.INFO, "Get all cv");
        return searchService.getAll();
    }

    @GetMapping("/{id}")
    public CV getCVById(@PathVariable String id){
        LOG.log(Level.INFO, "Get cv " + id);
        return searchService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteCVById(@PathVariable String id){
        LOG.log(Level.INFO, "Delete cv " + id);
        searchService.deleteCVById(id);
    }

    @GetMapping("/search")
    public List<CV> getCVBySkill(@RequestParam(value = "q", required = false) String skill){
        LOG.log(Level.INFO, "Get cv with skills " + skill);
        return searchService.findBySkill(skill) ;
    }

    @GetMapping("/searchTitle")
    public List<CV> getCVByTitle(@RequestParam(value = "q", required = false) String title){
        LOG.log(Level.INFO, "Get cv with title " + title);
        return searchService.findByTitle(title);
    }
}