package cn.lauevan.easy.dds.core.exception;

/**
 * <p>配置错误异常</p>
 *
 * @author Lauevan (noah.coder@gmail.com)
 * Create at November 13, 2020 at 16:31:38 GMT+8
 */
public class DDSConfigurationErrorException extends RuntimeException {

    public DDSConfigurationErrorException() {
    }

    public DDSConfigurationErrorException(String message) {
        super(message);
    }

    public DDSConfigurationErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public DDSConfigurationErrorException(Throwable cause) {
        super(cause);
    }

    public DDSConfigurationErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
