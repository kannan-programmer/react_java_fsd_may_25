package com.springboot.assetsphere.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.assetsphere.dto.ITSupportDTO;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Hr;
import com.springboot.assetsphere.model.ITSupport;
import com.springboot.assetsphere.service.ITSupportService;

@RestController
@RequestMapping("/api/itsupport")
@CrossOrigin(origins = "http://localhost:5173")
public class ITSupportController {

    @Autowired
    private ITSupportService itSupportService;

    @PostMapping("/add")
    public ResponseEntity<ITSupport> addITSupport(@RequestBody ITSupport itSupport) {
        return ResponseEntity.ok(itSupportService.addITSupport(itSupport));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ITSupportDTO>> getAll() {
        return ResponseEntity.ok(itSupportService.getAllITSupport());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<ITSupportDTO> getByUsername(@PathVariable String username) throws ResourceNotFoundException {
        return ResponseEntity.ok(itSupportService.getITSupportByUsername(username));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ITSupportDTO> getById(@PathVariable int id) throws ResourceNotFoundException {
        return ResponseEntity.ok(itSupportService.getById(id));
    }
    
    @PutMapping("/update/{username}")
    public ResponseEntity<ITSupport> updateITSupportByUsername(@PathVariable String username,
                                                                @RequestBody ITSupport updatedIT) throws ResourceNotFoundException {
        return ResponseEntity.ok(itSupportService.updateITSupportByUsername(username, updatedIT));
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<?> deleteITSupportByUsername(@PathVariable String username) throws ResourceNotFoundException {
        itSupportService.deleteITSupportByUsername(username);
        Map<String, String> response = new HashMap<>();
        response.put("message", "IT Support user deleted successfully for username: " + username);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/upload-image/profilepic")
    public ITSupport uploadpic(Principal principal, @RequestParam("file") MultipartFile file) throws IOException, ResourceNotFoundException {
    	 return itSupportService.uploadProfilePic(file, principal.getName());
    }
}
