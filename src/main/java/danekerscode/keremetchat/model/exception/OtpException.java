package danekerscode.keremetchat.model.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class OtpException extends RuntimeException {
    private final OtpExceptionType otpExceptionType;

    @AllArgsConstructor
    @Getter
    public enum OtpExceptionType {
        OTP_EXPIRED("OTP expired", HttpStatusCode.valueOf(400)),
        OTP_INVALID("OTP invalid", HttpStatusCode.valueOf(400)),
        OTP_NOT_FOUND("OTP not found", HttpStatusCode.valueOf(404));
        private final String message;
        private final HttpStatusCode statusCode;
    }

    public OtpException(OtpExceptionType otpExceptionType) {
        this.otpExceptionType = otpExceptionType;
    }
}
