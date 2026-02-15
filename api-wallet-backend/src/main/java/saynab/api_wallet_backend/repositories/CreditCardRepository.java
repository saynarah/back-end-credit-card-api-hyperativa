package saynab.api_wallet_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saynab.api_wallet_backend.entities.CreditCard;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, UUID> {

   // @Query("SELECT c FROM CreditCard c WHERE c.user.id = :userId")
//Optional<List<CreditCard>> findByUserId(@Param("userId") UUID userId);

    Optional<List<CreditCard>> findByUserId(UUID userId);

}
