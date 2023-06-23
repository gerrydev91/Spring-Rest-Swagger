package sv.gerry.RESTJpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import sv.gerry.RESTJpa.entities.Laptop;

public interface LaptopRepository extends JpaRepository<Laptop, Long>{
    
}
