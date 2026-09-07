Bài 3 – Chuyển SOA → Microservice REST API
1. BookClientService đã sửa
@Service
public class BookClientService {

    private final RestTemplate restTemplate;

    public BookClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getBookTitle(Long bookId) {
        String url = "http://book-service/api/books/" + bookId;

        try {
            return restTemplate.getForObject(url, String.class);

        } catch (RestClientException e) {
            return "Book service is unavailable";
        }
    }
}
Cấu hình @LoadBalanced RestTemplate
@Configuration
public class RestTemplateConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

Điểm sửa chính:

// Sai
http://192.168.1.15:8082/api/books/

// Đúng
http://book-service/api/books/

book-service là tên logic được Service Discovery sử dụng. @LoadBalanced giúp request được phân phối tới các instance đang hoạt động.

2. REST API cho notifyOverdue
DTO
public record NotifyOverdueRequest(
        Long memberId,
        Long bookId,
        LocalDate dueDate
) {
}
Controller
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/overdue")
    public ResponseEntity<String> notifyOverdue(
            @RequestBody NotifyOverdueRequest request) {

        notificationService.notifyOverdue(request);

        return ResponseEntity.ok("Overdue notification sent");
    }
}
Service
@Service
public class NotificationService {

    public void notifyOverdue(NotifyOverdueRequest request) {

        System.out.println(
                "Notify member " + request.memberId()
                + " about overdue book " + request.bookId()
                + ", due date: " + request.dueDate()
        );
    }
}
Request mẫu
POST /api/notifications/overdue
Content-Type: application/json
{
    "memberId": 1,
    "bookId": 10,
    "dueDate": "2026-09-01"
}
3. Phân tích SOA và MSA
SOA → MSA trong bối cảnh LibraX

Việc chuyển LibraX từ SOA sang Microservice Architecture mang lại lợi ích rõ ràng về tốc độ phát triển và khả năng mở rộng. Trong SOA, các service thường giao tiếp thông qua ESB. ESB cung cấp khả năng định tuyến, chuyển đổi và quản lý message tập trung, nhưng khi số lượng service tăng lên, ESB có thể trở thành thành phần trung tâm làm tăng độ phức tạp. Với MSA, borrowing-service có thể gọi trực tiếp book-service thông qua REST API. Cách tiếp cận này đơn giản hơn, dễ hiểu và phù hợp với các API HTTP phổ biến. Các service của LibraX cũng có thể được phát triển, kiểm thử và triển khai độc lập, giúp đội phát triển nhanh chóng thay đổi book-service hoặc notification-service mà không cần triển khai toàn bộ hệ thống.

Tuy nhiên, MSA không đơn giản hơn về mọi mặt. Việc loại bỏ ESB đồng nghĩa LibraX phải tự giải quyết nhiều vấn đề phân tán như Service Discovery, Load Balancing, timeout, retry, circuit breaker, monitoring và distributed tracing. Khi book-service được scale thành nhiều instance, việc gọi trực tiếp IP cố định sẽ không còn phù hợp; vì vậy cần sử dụng tên logic book-service kết hợp @LoadBalanced RestTemplate. Điều này làm tăng độ phức tạp vận hành so với một ứng dụng hoặc hệ thống SOA nhỏ.

Về khả năng chịu lỗi, MSA có ưu điểm là lỗi của một service có thể được cô lập. Ví dụ, nếu notification-service gặp sự cố, book-service và các chức năng khác vẫn có thể tiếp tục hoạt động. Tuy nhiên, nếu borrowing-service phụ thuộc trực tiếp vào book-service, lỗi mạng hoặc book-service không phản hồi vẫn có thể lan truyền. Vì vậy cần xử lý exception, timeout và các cơ chế resilience phù hợp.

Tóm lại, với LibraX, MSA phù hợp khi hệ thống đã đủ lớn và cần scale độc lập từng domain. Đổi lại, LibraX phải chấp nhận chi phí vận hành và quản lý hệ thống phân tán cao hơn SOA.