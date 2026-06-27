package com.JIMS.integration.controller;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.JIMS.integration.controller.VTS_vtypecontroller.ResponseMessage;
import com.JIMS.integration.entity.City;
import com.JIMS.integration.entity.VMS_statemastermodel;
import com.JIMS.integration.repository.CityRepository;
import com.JIMS.integration.repository.VMS_StateRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/location")
public class VMS_locationmastercontroller {

    @Autowired
    private CityRepository cityRepo;

    @Autowired
    private VMS_StateRepository stateRepo;

    // ---------- City APIs ----------

    @GetMapping("/cities")
    public List<City> listCities() {
        return cityRepo.findAll();
    }
    
    @GetMapping("/cities/paging")
    public Page<City> listCities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search
    ) {
        Pageable pageable = PageRequest.of(page, size);
        if (search == null || search.trim().isEmpty()) {
            return cityRepo.findAll(pageable);
        }
        // Search by cityName containing (case-insensitive)
        return cityRepo.findByCityNameContainingIgnoreCase(search.trim(), pageable);
    }
    
    
    @GetMapping("/cities/{id}")
    public ResponseEntity<City> getCityById(@PathVariable("id") int cityId) {
        Optional<City> city = cityRepo.findById(cityId);
        if (city.isPresent()) {
            return new ResponseEntity<>(city.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/cities/states/{stateId}")
    public List<City> getCitiesByState(@PathVariable("stateId") int stateId) {
        return cityRepo.findByStateId(stateId);
    }
	/*
	 * @PostMapping("/cities") public City addCity(@RequestBody City city) {
	 * city.setCreatedDate(LocalDateTime.now()); return cityRepo.save(city); }
	 */
    
    @PostMapping("/cities") 
	  public ResponseEntity<Object> addOrUpdateItem(@RequestBody City item) {
	        try {
	        	City savedItem = cityRepo.save(item);
	            return ResponseEntity.status(HttpStatus.CREATED)
	                    .body(new ResponseMessage("Success", "Record inserted successfully"));
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body(new ResponseMessage("Error", "Record not inserted. Please try again"));
	        }
	    }

    @GetMapping("/checkcity")
	public ResponseEntity<Map<String, Object>> checkcityExists(@RequestParam("City_name") String City_name) {
	    String sql = "SELECT COUNT(*) FROM VMS_Citymaster WHERE City_name = ?";
	    Map<String, Object> response = new HashMap<>();
	    try (Connection connection = jimsDataSource.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        preparedStatement.setString(1, City_name);
	        ResultSet resultSet = preparedStatement.executeQuery();
	        if (resultSet.next()) {
	            int count = resultSet.getInt(1);
	            response.put("exists", count > 0); // true if exists, false otherwise
	            return new ResponseEntity<>(response, HttpStatus.OK);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    response.put("exists", false);
	    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
    
    @PutMapping("/cities/{id}")
    public ResponseEntity<Object> updateCityById(@PathVariable int id, @RequestBody City updatedCity) {
        Optional<City> existingCityOpt = cityRepo.findById(id);

        if (existingCityOpt.isPresent()) {
            City existingCity = existingCityOpt.get();

            if (updatedCity.getCityName() != null) {
                existingCity.setCityName(updatedCity.getCityName());
            }
            if (updatedCity.getStateId() != 0) {
                existingCity.setStateId(updatedCity.getStateId());
            }
            if (updatedCity.getCreatedBy() != null) {
                existingCity.setCreatedBy(updatedCity.getCreatedBy());
            }
            if (updatedCity.getCreatedDate() != null) {
                existingCity.setCreatedDate(updatedCity.getCreatedDate());
            }
            if (updatedCity.getModifiedBy() != 0) {
                existingCity.setModifiedBy(updatedCity.getModifiedBy());
            }
            if (updatedCity.getModifiedDate() != null) {
                existingCity.setModifiedDate(updatedCity.getModifiedDate());
            }

            cityRepo.save(existingCity);
            return ResponseEntity.ok("City updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("City not found.");
        }
    }

    // ---------- State APIs ----------

    @GetMapping("/states")
    public List<VMS_statemastermodel> listStates() {
        return stateRepo.findAll();
    }
    
    @GetMapping("/states/paging")
    public Page<VMS_statemastermodel> listStatesPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search
    ) {
        Pageable pageable = PageRequest.of(page, size);
        if (search == null || search.trim().isEmpty()) {
            return stateRepo.findAll(pageable);
        }
        return stateRepo.searchByStateName(search.trim(), pageable);
    }
    
    
    
    @GetMapping("/states/{id}")
    public ResponseEntity<VMS_statemastermodel> getItemById(@PathVariable("id") int stateId) {
        Optional<VMS_statemastermodel> item = stateRepo.findById(stateId);
        if (item.isPresent()) {
            return new ResponseEntity<>(item.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @Autowired
    @Qualifier("jimsDataSource")
    private DataSource jimsDataSource;
	@GetMapping("/check")
	public ResponseEntity<Map<String, Object>> checkstateExists(@RequestParam("State_name") String State_name) {
	    String sql = "SELECT COUNT(*) FROM VMS_Statemaster WHERE State_name = ?";
	    Map<String, Object> response = new HashMap<>();
	    try (Connection connection = jimsDataSource.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        preparedStatement.setString(1, State_name);
	        ResultSet resultSet = preparedStatement.executeQuery();
	        if (resultSet.next()) {
	            int count = resultSet.getInt(1);
	            response.put("exists", count > 0); // true if exists, false otherwise
	            return new ResponseEntity<>(response, HttpStatus.OK);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    response.put("exists", false);
	    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	/*
	 * @PostMapping("/states") public VMS_statemastermodel addState(@RequestBody
	 * VMS_statemastermodel state) { state.setCreatedDate(LocalDateTime.now());
	 * return stateRepo.save(state); }
	 */
	 
	
	  @PostMapping("/states") 
	  public ResponseEntity<Object> addOrUpdateItem(@RequestBody VMS_statemastermodel item) {
	        try {
	            VMS_statemastermodel savedItem = stateRepo.save(item);
	            return ResponseEntity.status(HttpStatus.CREATED)
	                    .body(new ResponseMessage("Success", "Record inserted successfully"));
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body(new ResponseMessage("Error", "Record not inserted. Please try again"));
	        }
	    }
	 

    @PutMapping("/states/{id}")
    public ResponseEntity<Object> updateStateById(@PathVariable int id, @RequestBody VMS_statemastermodel updatedState) {
        Optional<VMS_statemastermodel> existingStateOpt = stateRepo.findById(id);

        if (existingStateOpt.isPresent()) {
            VMS_statemastermodel existingState = existingStateOpt.get();

            if (updatedState.getStateName() != null) {
                existingState.setStateName(updatedState.getStateName());
            }
            if (updatedState.getCreatedBy() != null) {
                existingState.setCreatedBy(updatedState.getCreatedBy());
            }
            if (updatedState.getCreatedDate() != null) {
                existingState.setCreatedDate(updatedState.getCreatedDate());
            }
            if (updatedState.getModifiedBy() != null) {
                existingState.setModifiedBy(updatedState.getModifiedBy());
            }
            if (updatedState.getModifiedDate() != null) {
                existingState.setModifiedDate(updatedState.getModifiedDate());
            }

            stateRepo.save(existingState);
            return ResponseEntity.ok("State updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("State not found.");
        }
    }
    
    
    @GetMapping("/state/is-used/{stateId}")
    public ResponseEntity<Map<String, Boolean>> isStateUsed(
            @PathVariable int stateId) {

        String sql = "SELECT COUNT(*) FROM VMS_Citymaster WHERE State_id = ?";

        try (Connection con = jimsDataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, stateId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                boolean used = rs.getInt(1) > 0;
                return ResponseEntity.ok(Map.of("used", used));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(Map.of("used", false));
    }

}
