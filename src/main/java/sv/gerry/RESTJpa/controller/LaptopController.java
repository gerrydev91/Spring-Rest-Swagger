package sv.gerry.RESTJpa.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


import sv.gerry.RESTJpa.entities.Laptop;
import sv.gerry.RESTJpa.repositories.LaptopRepository;

import java.util.List;
import java.util.Optional;

@RestController
public class LaptopController {
    
    private final Logger log = LoggerFactory.getLogger(LaptopController.class);

    private LaptopRepository laptopRepository;

    public LaptopController(LaptopRepository laptopRepository) {
        this.laptopRepository = laptopRepository;
    }

    @GetMapping("/api/laptops")
    public List<Laptop> findAll(){
        // Read all Laptops from DB
        return laptopRepository.findAll();
    }

    @GetMapping("/api/laptops/{id}")
    
    public ResponseEntity<Laptop> findOneById(@PathVariable Long id){
        Optional<Laptop> laptopOpt = laptopRepository.findById(id);
        if(laptopOpt.isPresent())
        return ResponseEntity.ok(laptopOpt.get());
        else
        return ResponseEntity.notFound().build();
    }


    @PostMapping("/api/laptops")
    public ResponseEntity<Laptop> create(@RequestBody Laptop laptop, @RequestHeader HttpHeaders headers){
        System.out.println(headers.get("User-Agent"));
        if(laptop.getId() != null){ // There's no need to receive an Id, they are automatically created. 
            log.warn("trying to create a laptop with id");
            System.out.println("trying to create a laptop with id");
            return ResponseEntity.badRequest().build();
        }
        Laptop result = laptopRepository.save(laptop);
        return ResponseEntity.ok(result); 
    }

    @PutMapping("/api/laptops/{id}")
    public ResponseEntity<Laptop> update(@PathVariable Long id, @RequestBody Laptop updatedLaptop){ //@RequestBody Receives a JSON object and maps it to a Java object. 
        
        Optional<Laptop> optionalLaptop = laptopRepository.findById(id);

        if(optionalLaptop.isEmpty() ){ 
            log.warn("Trying to update a non existent laptop");
            return ResponseEntity.badRequest().build();
        }
        if(!laptopRepository.existsById(updatedLaptop.getId())){
            log.warn("Trying to update a non existent laptop");
            return ResponseEntity.notFound().build();
            
        }

        Laptop laptop = optionalLaptop.get();
        laptop.setBrand(updatedLaptop.getBrand());
        laptop.setHardDrive(updatedLaptop.getHardDrive());
        laptop.setInStock(updatedLaptop.getInStock());
        laptop.setMemory(updatedLaptop.getMemory());
        laptop.setModel(updatedLaptop.getModel());
        laptop.setPrice(updatedLaptop.getPrice());

        laptop = laptopRepository.save(laptop);
        
        return new ResponseEntity<>(updatedLaptop, HttpStatus.OK);
    }



    @DeleteMapping("/api/laptops/{id}")
    public ResponseEntity<Laptop> delete(@PathVariable Long id){

        if(laptopRepository.existsById(id)==false){
            log.warn("Trying to delete a non existent laptop");
            return ResponseEntity.notFound().build();
        }

        laptopRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

   // Swagger will ignore this
    @DeleteMapping("/api/laptops")
    public ResponseEntity<Laptop> deleteAll(){
        log.info("REST Request for delete all laptops");
        laptopRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }


    
    

    
}
