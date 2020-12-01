package cn.lauevan.easy.dds.core.exception;

/**
 * <p>配置错误异常</p>
 *
 * @author Lauevan (noah.coder@gmail.com)
 * Create at November 13, 2020 at 16:31:38 GMT+8
 */
public class DDSLookupKeyCreateException extends RuntimeException {

    public DDSLookupKeyCreateException() {
    }

    public DDSLookupKeyCreateException(String message) {
        super(message);
    }

    public DDSLookupKeyCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public DDSLookupKeyCreateException(Throwable cause) {
        super(cause);
    }

    public DDSLookupKeyCreateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
