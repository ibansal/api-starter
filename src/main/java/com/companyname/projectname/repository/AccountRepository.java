package com.companyname.projectname.repository;

import com.companyname.projectname.domain.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by ishan.bansal on 6/29/15.
 */
public interface AccountRepository extends JpaRepository<Account, Long>, AccountRepositoryCustom {
    Account findByUserId(String userId);

    Account findByAdvertiserId(String advertiserId);

    @Query("select r from account r where r.name like ?1")
    Page<Account> searchByName(String searchBy, Pageable pageable);

    @Query("select count(r) from account r where r.name like ?1")
    int searchByNameCount(String searchBy);
}