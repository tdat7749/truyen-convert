package truyenconvert.server.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import truyenconvert.server.models.enums.ReportStatus;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reports",indexes = {
        @Index(name = "idx_reports_user_id", columnList = "user_id"),
        @Index(name = "idx_reports_handler_id", columnList = "handler_id"),
        @Index(name = "idx_reports_report_type_id", columnList = "report_type_id"),
})
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false,length = 1000)
    private String content;

    @Column(nullable = false)
    @ColumnDefault(value = "'0'")
    private ReportStatus status;

    @Column(nullable = false,name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false,name = "updated_at")
    private LocalDateTime updatedAt;

    // User
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    @JsonBackReference
    private User user;

    // Handler
    @ManyToOne
    @JoinColumn(name = "handler_id")
    @JsonBackReference
    private User handler;

    // Report type
    @ManyToOne
    @JoinColumn(name = "report_type_id")
    @JsonBackReference
    private ReportType reportType;
}
