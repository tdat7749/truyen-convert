package truyenconvert.server.modules.donation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import truyenconvert.server.models.Book;
import truyenconvert.server.models.Donation;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Integer> {

    @Query("SELECT d FROM Donation as d WHERE d.book =:book")
    Page<Donation> getAllDonationForBook(Pageable paging, Book book);
}
