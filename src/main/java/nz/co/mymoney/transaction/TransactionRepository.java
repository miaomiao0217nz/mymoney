package nz.co.mymoney.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("select t from transaction t where t.userId = ?1 and date>=?2 and date<?3")
    List<Transaction> findByUser(long userId, LocalDate from, LocalDate to);
}
