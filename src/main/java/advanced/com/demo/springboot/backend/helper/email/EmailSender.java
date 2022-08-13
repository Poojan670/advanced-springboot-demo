package advanced.com.demo.springboot.backend.helper.email;

public interface EmailSender {
    void send(String to, String email);
}