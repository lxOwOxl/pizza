package com.example.pizza.repository;

import com.example.pizza.entity.CrustPrice;
import com.example.pizza.entity.Product;
import com.example.pizza.entity.ProductPrice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import com.example.pizza.enums.Crust;
import com.example.pizza.enums.Size;

@Repository
public interface CrustPriceRepository extends JpaRepository<CrustPrice, Integer> {
    List<CrustPrice> findBySize(Size size);

    @Query("SELECT cp.crust FROM CrustPrice cp WHERE cp.size = :size")
    List<Crust> findCrustsBySize(@Param("size") Size size);

    CrustPrice findByCrustAndSize(Crust crust, Size size);

    @Query("SELECT p FROM CrustPrice p WHERE p.size = :size AND p.crust = :crust")
    CrustPrice findBySizeAndCrust(@Param("size") Size size, @Param("crust") Crust crust);
}