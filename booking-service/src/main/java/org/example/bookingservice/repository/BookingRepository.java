package org.example.bookingservice.repository;

import org.example.bookingservice.dto.booking.BookingRevenueSummaryResponse;
import org.example.bookingservice.dto.booking.BookingSearchResponse;
import org.example.bookingservice.entity.Booking;
import org.example.bookingservice.entity.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByStatus(BookingStatus status);

    @Query("""
            select new org.example.bookingservice.dto.booking.BookingSearchResponse(
                b.id,
                b.unitId,
                concat(g.firstName, ' ', g.lastName),
                g.email,
                p.policyName,
                b.startDate,
                b.endDate,
                b.totalPrice,
                b.status
            )
            from Booking b
            join b.guest g
            left join b.policy p
            where (:status is null or b.status = :status)
              and (:guestEmail is null or lower(g.email) like lower(concat('%', :guestEmail, '%')))
              and (:startFrom is null or b.startDate >= :startFrom)
              and (:endTo is null or b.endDate <= :endTo)
            order by b.startDate asc, b.id asc
            """)
    List<BookingSearchResponse> searchBookings(BookingStatus status,
                                               String guestEmail,
                                               LocalDate startFrom,
                                               LocalDate endTo);

    @Query("""
            select new org.example.bookingservice.dto.booking.BookingRevenueSummaryResponse(
                b.status,
                count(b),
                coalesce(sum(b.totalPrice), 0),
                coalesce((
                    select sum(a.price)
                    from AddOnItem a
                    where a.booking.status = b.status
                ), 0),
                coalesce(sum(b.totalPrice), 0) + coalesce((
                    select sum(a.price)
                    from AddOnItem a
                    where a.booking.status = b.status
                ), 0)
            )
            from Booking b
            group by b.status
            order by b.status asc
            """)
    List<BookingRevenueSummaryResponse> summarizeRevenueByStatus();

    long countByUnitIdAndStatusIn(Long unitId, List<BookingStatus> statuses);

    Optional<Booking> findFirstByUnitIdAndStatusInAndStartDateLessThanEqualAndEndDateGreaterThan(Long unitId,
                                                                                                  List<BookingStatus> statuses,
                                                                                                  LocalDate startDate,
                                                                                                  LocalDate endDate);
}
