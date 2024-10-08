package com.lazis.lazissultanagung.repository;

import com.lazis.lazissultanagung.model.Coa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoaRepository extends JpaRepository<Coa, Long> {

    @Query("SELECT c FROM Coa c where c.accountType = Asset")
    List<Coa> getCoaAsset();

    @Query("SELECT c FROM Coa c where c.accountType = Liability")
    List<Coa> getCoaLiability();

    @Query("SELECT c FROM Coa c where c.accountType = Equity")
    List<Coa> getCoaEquity();

    @Query("SELECT c FROM Coa c where c.accountType = Revenue")
    List<Coa> getCoaRevenue();

    @Query("SELECT c FROM Coa c where c.accountType = Expense")
    List<Coa> getCoaExpense();

}
