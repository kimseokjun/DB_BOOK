package bookDB.demo.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity
public class Seat {

    @Id
    private Long seatId; // 좌석 ID

    private String status; // 사용 현황 (사용 중 / 빈 좌석)

    @Transient
    private String maskedName; // 마스킹된 이름 (프로시저 결과)

    // Getter & Setter
    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMaskedName() {
        return maskedName;
    }

    public void setMaskedName(String maskedName) {
        this.maskedName = maskedName;
    }
}

