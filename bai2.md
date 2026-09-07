Bài 2 – SOA với Service Contract và ESB Simulator
1. Lỗi == khi so sánh String

Trong Java, == so sánh reference (địa chỉ đối tượng), không so sánh nội dung của String.

Ví dụ:

String service = new String("NotificationService");

service == "NotificationService"; // false
service.equals("NotificationService"); // true

Vì vậy phải dùng .equals() để kiểm tra nội dung chuỗi.

2. ESB Simulator đã sửa
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EsbSimulator {

    private static final Logger log =
            LoggerFactory.getLogger(EsbSimulator.class);

    private final NotificationService notificationService;
    private final PaymentService paymentService;

    public EsbSimulator(
            NotificationService notificationService,
            PaymentService paymentService) {
        this.notificationService = notificationService;
        this.paymentService = paymentService;
    }

    public void routeMessage(
            String toService,
            String operation,
            String payload) {

        if ("NotificationService".equals(toService)) {

            log.info("Routing message to NotificationService");
            notificationService.handle(operation, payload);

        } else if ("PaymentService".equals(toService)) {

            log.info("Routing message to PaymentService");
            paymentService.handle(operation, payload);

        } else {

            log.warn(
                "Routing failed: service '{}' is not registered",
                toService
            );
        }
    }
}

Dùng "NotificationService".equals(toService) còn an toàn hơn toService.equals(...) vì tránh NullPointerException nếu toService == null.

3. Service Contract – notifyOverdue

Có thể mô tả WSDL rút gọn như sau:

<?xml version="1.0" encoding="UTF-8"?>

<serviceContract
        name="NotificationService"
        namespace="http://librax.com/notification">

    <operation name="notifyOverdue">

        <input name="NotifyOverdueRequest">
            <field name="memberId" type="long"/>
            <field name="bookId" type="long"/>
            <field name="dueDate" type="date"/>
        </input>

        <output name="NotifyOverdueResponse">
            <field name="success" type="boolean"/>
            <field name="message" type="string"/>
        </output>

    </operation>

</serviceContract>
Contract
Thành phần	Kiểu	Ý nghĩa
memberId	long	ID độc giả
bookId	long	ID sách quá hạn
dueDate	date	Hạn trả sách
success	boolean	Kết quả xử lý
message	string	Thông báo kết quả
4. NotificationService
public class NotificationService {

    public void handle(String operation, String payload) {

        if ("notifyOverdue".equals(operation)) {
            notifyOverdue(payload);
        }
    }

    private void notifyOverdue(String payload) {
        System.out.println(
            "Processing overdue notification: " + payload
        );
    }
}
5. BorrowingService gửi thông điệp
public class BorrowingService {

    private final EsbSimulator esbSimulator;

    public BorrowingService(EsbSimulator esbSimulator) {
        this.esbSimulator = esbSimulator;
    }

    public void checkOverdue(
            Long memberId,
            Long bookId,
            String dueDate) {

        String payload = String.format(
                "{\"memberId\":%d,\"bookId\":%d,\"dueDate\":\"%s\"}",
                memberId,
                bookId,
                dueDate
        );

        esbSimulator.routeMessage(
                "NotificationService",
                "notifyOverdue",
                payload
        );
    }
}
6. Luồng xử lý
BorrowingService
      │
      │ Phát hiện sách quá hạn
      ▼
Tạo message
{
  memberId,
  bookId,
  dueDate
}
      │
      ▼
ESB Simulator
      │
      │ toService = "NotificationService"
      ▼
Kiểm tra .equals()
      │
      ▼
NotificationService
      │
      │ operation = "notifyOverdue"
      ▼
Xử lý thông báo
      │
      ▼
Gửi thông báo nhắc độc giả trả sách

Nếu service không tồn tại:

BorrowingService
      │
      ▼
ESB Simulator
      │
      │ toService = "UnknownService"
      ▼
Không tìm thấy service
      │
      ▼
log.warn(...)