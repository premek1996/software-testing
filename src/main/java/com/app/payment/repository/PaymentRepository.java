package com.app.payment.repository;

import com.app.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query(value = " select * from payment " +
            " where customer_id = :customer_id",
            nativeQuery = true)
    List<Payment> getAllByCustomerId(@Param("customer_id") String customerId);
}
