package com.springboot.assetsphere.controller;

import java.io.IOException;



import java.security.Principal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.assetsphere.dto.HRDashboardStatsDto;
import com.springboot.assetsphere.dto.HrDTO;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Hr;
import com.springboot.assetsphere.service.HRService;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/hr")
public class HRController {

    @Autowired
    private HRService hrService;

    @PostMapping("/add")
    public ResponseEntity<Hr> addHR(@RequestBody Hr hr) {
        return ResponseEntity.ok(hrService.addHR(hr));
    }

    @GetMapping("/all")
    public ResponseEntity<List<HrDTO>> getAll() {
        return ResponseEntity.ok(hrService.getAllHR());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<HrDTO> getByUsername(@PathVariable String username) throws ResourceNotFoundException {
        return ResponseEntity.ok(hrService.getHRByUsername(username));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<HrDTO> getById(@PathVariable int id) throws ResourceNotFoundException {
        return ResponseEntity.ok(hrService.getById(id));
    }
    
    @GetMapping("/get")
    public Hr getHRInfo(Principal principal) throws ResourceNotFoundException {
        return hrService.getHRInfo(principal.getName());
    }
    
    @PutMapping("/update/{username}")
    public ResponseEntity<Hr> updateHRByUsername(@PathVariable String username, @RequestBody Hr updatedHr) throws ResourceNotFoundException {
        return ResponseEntity.ok(hrService.updateHRByUsername(username, updatedHr));
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<?> deleteHRByUsername(@PathVariable String username) throws ResourceNotFoundException {
        hrService.deleteHRByUsername(username);
        Map<String, String> map = new HashMap<>();
        map.put("message", "HR deleted successfully for username: " + username);
        return ResponseEntity.ok(map);
    }
    
    @PostMapping("/upload-image/profilepic")
    public Hr uploadpic(Principal principal, @RequestParam("file") MultipartFile file) throws IOException, ResourceNotFoundException {
    	 return hrService.uploadProfilePic(file, principal.getName());
    }
    
    @GetMapping("/dashboard/stats")
    public ResponseEntity<HRDashboardStatsDto> getDashboardStats(Principal principal) {
        try {
            HRDashboardStatsDto stats = hrService.getDashboardStats(principal.getName());
            return ResponseEntity.ok(stats);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
