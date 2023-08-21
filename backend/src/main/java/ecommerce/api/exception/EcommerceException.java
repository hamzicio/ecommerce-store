package ecommerce.api.exception;


import ecommerce.api.enums.ResultEnum;


public class EcommerceException extends RuntimeException {

    private Integer code;

    public EcommerceException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());

        this.code = resultEnum.getCode();
    }

    public EcommerceException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
