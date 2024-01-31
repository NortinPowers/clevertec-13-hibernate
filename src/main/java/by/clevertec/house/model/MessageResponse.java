package by.clevertec.house.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse extends BaseResponse {

    private String message;

    @JsonIgnore
    private Object object;

    public MessageResponse(Integer status, String message, Object object) {
        super(status);
        this.message = message;
        this.object = object;
    }
}
