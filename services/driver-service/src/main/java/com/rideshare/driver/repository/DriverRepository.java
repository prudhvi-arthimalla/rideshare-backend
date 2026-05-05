package com.rideshare.driver.repository;

import com.rideshare.driver.domain.Driver;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    Optional<Driver> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Driver> findFirstByStatusOrderByUpdatedAtAsc(Driver.DriverStatus status);
}
