package com.jangbee.accounts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUsername(String username);

    Account findByEmail(String email);

    Page<Account> findAll(Specification<Account> spec, Pageable pageable);

    @Query(value ="select a.adtsOrderNum from Account a where a.id = :accountID")
    Integer getOrderNumAdTS(@Param("accountID") Long accountID);

    @Modifying
    @Query(value ="update Account a set a.adtsOrderNum = :nextOrderNum where a.id = :accountID")
    void setNextOrderNumAdTS(@Param("accountID") Long accountID, @Param("nextOrderNum") Integer nextOrderNum);


    //    @Query( value="select a from Account a left join Estimate s on a.id = s.accountId where s.bowl.id = :bowlId")
    //    Page<Account> findByBowl(@Param("bowlId") Long bowlId, Pageable pageable);
}
